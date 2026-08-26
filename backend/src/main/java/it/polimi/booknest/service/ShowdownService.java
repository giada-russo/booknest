package it.polimi.booknest.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.polimi.booknest.dto.RisultatoShowdownDTO;
import it.polimi.booknest.exception.ShowdownNonAttivoException;
import it.polimi.booknest.exception.ShowdownNonTrovatoException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.exception.VotoGiaEspressoException;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LibroScelto;
import it.polimi.booknest.model.Showdown;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.model.VotoShowdown;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.ShowdownRepository;
import it.polimi.booknest.repository.VotoShowdownRepository;

/**
 * Servizio principale per la gestione della logica di business degli Showdown.
 * Si occupa di orchestrare i voti, proteggere lo stato della votazione dalle
 * race condition (tramite gestione esplicita della concorrenza) e interfacciarsi
 * con il livello di persistenza.
 */
@Service
public class ShowdownService {

    private final ShowdownRepository showdownRepository;
    private final VotoShowdownRepository votoShowdownRepository;
    private final UtenteService utenteService;
    private final CatalogazioneRepository catalogazioneRepository;
    private final LibroRepository libroRepository;

    /**
     * Registro centrale in memoria che associa a ogni Showdown (identificato dal suo ID)
     * il suo stato corrente e il relativo lucchetto (Lock).
     * <p>
     * <b>Note Architetturali:</b>
     * <ul>
     *   <li><b>Chiave Long (ID):</b> Si usa l'ID dello Showdown e non l'entità JPA. JPA re-istanzia gli oggetti
     *       a ogni caricamento, compromettendo l'uguaglianza dei riferimenti. L'ID garantisce un'identificazione stabile.</li>
     *   <li><b>ConcurrentHashMap:</b> Poiché il Service è un Singleton, questa mappa è condivisa tra tutti i thread
     *       delle richieste HTTP. Una mappa concorrente è fondamentale per evitare corruzioni strutturali durante gli
     *       inserimenti simultanei.</li>
     *   <li><b>final:</b> Il riferimento al contenitore della mappa non deve mai essere sovrascritto.</li>
     * </ul>
     * </p>
     */
    private final Map<Long, StatoShowdown> stati = new ConcurrentHashMap<>();

    /**
     * Costruttore per l'iniezione delle dipendenze di Spring.
     *
     * @param showdownRepository      Repository per recuperare e salvare i dati degli Showdown.
     * @param votoShowdownRepository  Repository per tracciare storicamente i voti effettuati.
     * @param utenteService           Servizio per la gestione degli utenti. Fondamentale per accedere a
     *                                {@code trovaPerId}, che rappresenta il punto unico di risoluzione
     *                                dell'id ricevuto nell'header HTTP (decisione architetturale del 10 agosto).
     * @param catalogazioneRepository Repository delle catalogazioni per la popolarità dei libri.
     * @param libroRepository         Repository dei libri per il meccanismo di ripiego.
     */
    public ShowdownService(ShowdownRepository showdownRepository,
                           VotoShowdownRepository votoShowdownRepository,
                           UtenteService utenteService,
                           CatalogazioneRepository catalogazioneRepository,
                           LibroRepository libroRepository) {
        this.showdownRepository = showdownRepository;
        this.votoShowdownRepository = votoShowdownRepository;
        this.utenteService = utenteService;
        this.catalogazioneRepository = catalogazioneRepository;
        this.libroRepository = libroRepository;
    }

    /**
     * Registra il voto di un utente per un determinato Showdown.
     * È il metodo cruciale per la gestione della concorrenza e garantisce
     * l'integrità dei voti in caso di accessi simultanei.
     * <p>
     * <b>Architettura della sincronizzazione:</b>
     * <ul>
     *   <li><b>Letture fuori dal lock:</b> Il caricamento di Utente e Showdown avviene prima di acquisire
     *       il lock perché sono operazioni in sola lettura che non alterano lo stato e possono parallelizzarsi.
     *       Inoltre, l'entità Showdown (o il suo ID) è necessaria proprio per determinare quale lock acquisire.</li>
     *   <li><b>Atomicità del lucchetto (computeIfAbsent):</b> Per ottenere il lock si usa {@code stati.computeIfAbsent}.
     *       Questo garantisce un'operazione atomica di check-and-insert sulla mappa. Se si usasse un semplice
     *       {@code if(!contains) put(...)}, due thread potrebbero superare il controllo simultaneamente creando
     *       due lock diversi per lo stesso Showdown, vanificando la sincronizzazione.</li>
     *   <li><b>Operazioni dentro il lock:</b> Verifica dello stato attivo, verifica del duplicato, salvataggio sul database e incremento dei
     *       contatori avvengono in un singolo blocco indivisibile (Check-then-Act). Il controllo sullo stato è nel lock per prevenire l'inserimento
     *       di voti in una race condition in cui un altro thread sta contestualmente chiudendo lo showdown. Se il salvataggio fosse esterno,
     *       un secondo thread potrebbe superare il controllo nella finestra temporale tra la verifica e la scrittura.</li>
     *   <li><b>Il blocco finally:</b> Il rilascio del lock ({@code unlock}) è racchiuso in un blocco finally obbligatorio.
     *       Poiché la verifica lancia un'eccezione se l'utente ha già votato (o se lo showdown è inattivo), in assenza di un finally il lock resterebbe
     *       acquisito indefinitamente, causando un deadlock perenne su quello Showdown.</li>
     * </ul>
     * </p>
     *
     * @param idUtente   L'ID dell'utente che esprime il voto (estratto dall'Header HTTP).
     * @param idShowdown L'ID dello Showdown oggetto della votazione (estratto dall'URL).
     * @param scelta     L'opzione scelta dall'utente (es. Libro A o Libro B).
     *
     * @throws UtenteNonTrovatoException   Se l'ID utente non esiste nel sistema.
     * @throws ShowdownNonTrovatoException Se l'ID dello Showdown non corrisponde a un'entità a database.
     * @throws ShowdownNonAttivoException  Se lo Showdown richiesto è chiuso e non accetta più voti (isAttivo() == false).
     * @throws VotoGiaEspressoException    Se l'utente risulta aver già partecipato a questa votazione.
     */
    public void vota(Long idUtente, Long idShowdown, LibroScelto scelta) {

        Utente utente = utenteService.trovaPerId(idUtente);
        Showdown showdown = showdownRepository.findById(idShowdown)
                .orElseThrow(() -> new ShowdownNonTrovatoException(idShowdown));

        StatoShowdown stato = stati.computeIfAbsent(idShowdown, k -> new StatoShowdown());

        stato.lock.lock();
        try {
            if (!showdown.isAttivo()) {
                throw new ShowdownNonAttivoException(idShowdown);
            }

            if (votoShowdownRepository.existsByUtenteAndShowdown(utente, showdown)) {
                throw new VotoGiaEspressoException();
            }

            votoShowdownRepository.save(new VotoShowdown(utente, showdown, scelta));

            if (scelta == LibroScelto.A) {
                stato.votiA++;
            } else {
                stato.votiB++;
            }
        } finally {
            stato.lock.unlock();
        }
    }

    /**
     * Restituisce i risultati correnti per lo showdown specificato, leggendoli dallo stato in memoria.
     * <p>
     * L'acquisizione del lock è necessaria anche per una semplice operazione di lettura per due motivi:
     * 1) Visibilità: la JVM non garantisce che le modifiche fatte da un thread siano immediatamente
     *    visibili agli altri. Senza sincronizzazione, si rischierebbe di leggere valori stantii.
     * 2) Coerenza: leggendo i due contatori in istanti diversi senza sincronizzazione,
     *    si rischierebbe di comporre e restituire una coppia di voti che non è mai realmente esistita
     *    (es. leggere votiA, subire un'interruzione in cui un altro thread modifica entrambi, e poi leggere votiB).
     * </p>
     *
     * @param idShowdown L'identificatore univoco dello showdown.
     * @return Un DTO contenente i conteggi coerenti e aggiornati per le opzioni A e B.
     */
    public RisultatoShowdownDTO getRisultati(Long idShowdown) {
        StatoShowdown stato = stati.computeIfAbsent(idShowdown, k -> new StatoShowdown());

        stato.lock.lock();
        try {
            return new RisultatoShowdownDTO(idShowdown, stato.votiA, stato.votiB);
        } finally {
            stato.lock.unlock();
        }
    }

    /**
     * Recupera tutti gli showdown attualmente attivi dal database.
     * Questo metodo non necessita di lock poiché interroga direttamente il database
     * e non accede allo stato in memoria condiviso.
     *
     * @return Una lista contenente gli {@code Showdown} attivi.
     */
    public List<Showdown> trovaAttivi() {
        return showdownRepository.findByAttivoTrue();
    }

    /**
     * Genera automaticamente un nuovo duello Showdown ogni ora, selezionando una coppia
     * di libri disponibili tramite il metodo {@link #selezionaCoppiaLibri()}.
     * <p>
     * Questo metodo è annotato con {@code @Scheduled} e viene invocato periodicamente dal framework
     * ogni ora (intervallo di 3.600.000 millisecondi). Se la selezione non restituisce esattamente
     * due libri liberi (ad esempio in caso di carenza di risorse nel sistema), il metodo non compie alcuna azione.
     * </p>
     */
    @Scheduled(fixedRate = 3600000)
    public void generaShowdownAutomatico() {
        List<Libro> libri = selezionaCoppiaLibri();
        if (libri.size() == 2) {
            showdownRepository.save(new Showdown(libri.get(0), libri.get(1)));
        }
    }

    /**
     * Seleziona una coppia di libri disponibili per un nuovo duello Showdown.
     * <p>
     * Cerca prioritariamente tra i libri più catalogati dalla community, escludendo
     * quelli già impegnati in duelli attualmente attivi. Qualora i candidati idonei
     * fossero inferiori a due, il sistema attiva un meccanismo di ripiego attingendo
     * all'intero catalogo generale. In questo modo il sistema garantisce sempre
     * almeno un sondaggio attivo anche con community inattiva.
     * </p>
     *
     * @return una lista contenente esattamente 2 libri liberi, oppure una lista vuota
     *         se non ci sono abbastanza libri disponibili nel sistema
     */
    private List<Libro> selezionaCoppiaLibri() {
        // 1. Raccogliamo gli ID dei libri attualmente impegnati in duelli attivi
        Set<Long> occupati = new HashSet<>();
        for (Showdown s : showdownRepository.findByAttivoTrue()) {
            occupati.add(s.getLibroA().getId());
            occupati.add(s.getLibroB().getId());
        }

        // 2. Prendiamo i candidati ordinati per popolarità e filtriamo i liberi
        List<Libro> candidati = catalogazioneRepository.trovaLibriPiuCatalogati()
                .stream()
                .filter(l -> !occupati.contains(l.getId()))
                .toList();

        // 3. Se abbiamo almeno 2 libri popolari liberi, prendiamo i primi due
        if (candidati.size() >= 2) {
            return List.of(candidati.get(0), candidati.get(1));
        }

        // 4. Ripiego: se i popolari non bastano, peschiamo da tutto il catalogo
        List<Libro> riserva = libroRepository.findAll()
                .stream()
                .filter(l -> !occupati.contains(l.getId()))
                .toList();

        if (riserva.size() >= 2) {
            return List.of(riserva.get(0), riserva.get(1));
        }

        // Se non ci sono proprio abbastanza libri in tutto il sistema
        return List.of();
    }

    /**
     * Classe interna dedicata a incapsulare lo stato transiente di una votazione e il relativo
     * meccanismo di sincronizzazione in memoria.
     * <p>
     * <b>Note di Design:</b>
     * <ul>
     *   <li><b>private:</b> È un dettaglio implementativo interno al service, nascosto all'esterno (Information Hiding).</li>
     *   <li><b>static:</b> È un puro contenitore di dati. Non necessita di accedere ai campi dell'istanza esterna
     *       di {@code ShowdownService}, evitando così di tenere un riferimento inutile in memoria.</li>
     * </ul>
     * </p>
     */
    private static class StatoShowdown {

        /**
         * Lock per garantire la mutua esclusione durante l'operazione di check-then-act sui voti.
         * È marcato come {@code final} per impedire riassegnazioni accidentali che lascerebbero
         * eventuali thread in attesa permanentemente orfani sul vecchio lucchetto.
         *
         * L'utilizzo di un oggetto {@link ReentrantLock} è stato preferito al blocco {@code synchronized}
         * perché essendo a tutti gli effetti un oggetto, permette di mantenere in modo naturale e pulito
         * un lock separato per ogni istanza di Showdown all'interno della Mappa nel Service,
         * isolando la contesa dei thread solo sui voti di uno stesso sondaggio.
         */
        private final ReentrantLock lock = new ReentrantLock();

        private int votiA = 0;
        private int votiB = 0;
    }
}