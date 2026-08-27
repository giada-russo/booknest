package it.polimi.booknest.service;

import it.polimi.booknest.exception.CatalogazioneNonTrovataException;
import it.polimi.booknest.exception.LibroGiaCatalogatoException;
import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.TransizioneNonValidaException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.exception.VotoNonConsentitoException;
import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.StatoLettura;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service per la gestione della logica applicativa relativa alle catalogazioni dei libri.
 * <p>
 * Le regole del ciclo di vita e delle transizioni vivono nell'entità e nelle classi di stato
 * (Design Pattern State), mentre questo service si limita a risolvere le entità e a delegare.
 * </p>
 */
@Service
public class CatalogazioneService {

    private final CatalogazioneRepository catalogazioneRepository;
    private final LibroRepository libroRepository;
    private final UtenteService utenteService;

    /**
     * Costruttore per l'iniezione delle dipendenze del service.
     *
     * @param catalogazioneRepository Repository per le catalogazioni.
     * @param libroRepository Repository per i libri.
     * @param utenteService Service per la gestione degli utenti.
     */
    public CatalogazioneService(CatalogazioneRepository catalogazioneRepository,
                                LibroRepository libroRepository,
                                UtenteService utenteService) {
        this.catalogazioneRepository = catalogazioneRepository;
        this.libroRepository = libroRepository;
        this.utenteService = utenteService;
    }

    /**
     * Metodo di supporto privato per evitare la duplicazione della logica di risoluzione:
     * recupera l'utente, verifica l'esistenza del libro e cerca la catalogazione associata.
     *
     * @param idUtente L'identificativo dell'utente.
     * @param idLibro L'identificativo del libro.
     * @return L'oggetto {@link Catalogazione} trovato.
     * @throws UtenteNonTrovatoException se l'utente non esiste.
     * @throws LibroNonTrovatoException se il libro non esiste.
     * @throws CatalogazioneNonTrovataException se la catalogazione non esiste.
     */
    private Catalogazione trovaCatalogazione(Long idUtente, Long idLibro) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));
        return catalogazioneRepository.findByUtenteAndLibro(utente, libro)
                .orElseThrow(() -> new CatalogazioneNonTrovataException(idUtente, idLibro));
    }

    /**
     * Aggiunge un nuovo libro alla libreria personale dell'utente.
     *
     * @param idUtente L'identificativo dell'utente.
     * @param idLibro L'identificativo del libro da catalogare.
     * @return La catalogazione creata e salvata.
     * @throws UtenteNonTrovatoException se l'utente non esiste.
     * @throws LibroNonTrovatoException se il libro non esiste.
     * @throws LibroGiaCatalogatoException se il libro è già presente nella libreria.
     */
    public Catalogazione cataloga(Long idUtente, Long idLibro) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        if (catalogazioneRepository.existsByUtenteAndLibro(utente, libro)) {
            throw new LibroGiaCatalogatoException(idUtente, idLibro);
        }

        return catalogazioneRepository.save(new Catalogazione(utente, libro));
    }

    /**
     * Modifica lo stato di lettura di un libro catalogato.
     *
     * @param idUtente L'identificativo dell'utente.
     * @param idLibro L'identificativo del libro.
     * @param nuovoStato Lo stato desiderato.
     * @return La catalogazione aggiornata.
     * @throws UtenteNonTrovatoException se l'utente non esiste.
     * @throws LibroNonTrovatoException se il libro non esiste.
     * @throws CatalogazioneNonTrovataException se la catalogazione non esiste.
     * @throws TransizioneNonValidaException se la transizione di stato non è consentita.
     */
    public Catalogazione cambiaStato(Long idUtente, Long idLibro, StatoLettura nuovoStato) {
        Catalogazione c = trovaCatalogazione(idUtente, idLibro);

        // Delega la validazione e il cambio di stato al Pattern State per incapsulare le logiche di dominio.
        c.cambiaStato(nuovoStato);

        catalogazioneRepository.save(c);
        return c;
    }

    /**
     * Assegna un voto a un libro catalogato.
     *
     * @param idUtente L'identificativo dell'utente.
     * @param idLibro L'identificativo del libro.
     * @param voto Il valore del voto (da 1 a 5).
     * @return La catalogazione aggiornata.
     * @throws UtenteNonTrovatoException se l'utente non esiste.
     * @throws LibroNonTrovatoException se il libro non esiste.
     * @throws CatalogazioneNonTrovataException se la catalogazione non esiste.
     * @throws VotoNonConsentitoException se lo stato corrente vieta la valutazione.
     * @throws IllegalArgumentException se il voto non è compreso tra 1 e 5.
     */
    public Catalogazione assegnaVoto(Long idUtente, Long idLibro, int voto) {
        Catalogazione c = trovaCatalogazione(idUtente, idLibro);
        c.assegnaVoto(voto);
        catalogazioneRepository.save(c);
        return c;
    }

    /**
     * Recupera l'intera libreria personale di un utente.
     *
     * @param idUtente L'identificativo dell'utente.
     * @return Una lista di catalogazioni associate all'utente.
     * @throws UtenteNonTrovatoException se l'utente non esiste.
     */
    public List<Catalogazione> trovaLibreria(Long idUtente) {
        Utente utente = utenteService.trovaPerId(idUtente);
        return catalogazioneRepository.findByUtente(utente);
    }

    /**
     * Restituisce il diario di lettura dell'utente, ovvero l'elenco delle catalogazioni
     * completate (stato LETTO), ordinate dalla più recente alla meno recente.
     *
     * @param idUtente l'identificativo dell'utente di cui recuperare il diario
     * @return una lista di {@link Catalogazione} che rappresentano le letture completate
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     */
    public List<Catalogazione> trovaDiario(Long idUtente) {
        Utente utente = utenteService.trovaPerId(idUtente);
        return catalogazioneRepository.findByUtenteAndStatoOrderByDataCompletamentoDesc(utente, StatoLettura.LETTO);
    }
}