package it.polimi.booknest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.polimi.booknest.exception.CatalogazioneNonTrovataException;
import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.RecensioneGiaEsistenteException;
import it.polimi.booknest.exception.RecensioneNonConsentitaException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.RecensioneRepository;

/**
 * Service per la gestione della logica di business relativa alle recensioni dei libri.
 * <p>
 * Gestisce la creazione, la modifica e il recupero delle recensioni, applicando
 * i controlli di validità sullo stato di lettura (pattern State) e sull'esistenza delle risorse.
 * </p>
 */
@Service
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final LibroRepository libroRepository;
    private final CatalogazioneRepository catalogazioneRepository;
    private final UtenteService utenteService;

    /**
     * Costruttore unico per l'iniezione delle dipendenze.
     *
     * @param recensioneRepository  il repository delle recensioni
     * @param libroRepository       il repository dei libri
     * @param catalogazioneRepository il repository delle catalogazioni
     * @param utenteService         il service per la gestione degli utenti
     */
    public RecensioneService(RecensioneRepository recensioneRepository,
                             LibroRepository libroRepository,
                             CatalogazioneRepository catalogazioneRepository,
                             UtenteService utenteService) {
        this.recensioneRepository = recensioneRepository;
        this.libroRepository = libroRepository;
        this.catalogazioneRepository = catalogazioneRepository;
        this.utenteService = utenteService;
    }

    /**
     * Crea e salva una nuova recensione per un libro, verificando che l'utente l'abbia catalogato
     * e che lo stato di lettura consenta la recensione.
     *
     * @param idUtente l'identificativo dell'utente che scrive la recensione
     * @param idLibro  l'identificativo del libro da recensire
     * @param testo    il contenuto testuale della recensione
     * @param pubblica flag che indica se la recensione è pubblica
     * @return la {@link Recensione} creata e salvata
     * @throws UtenteNonTrovatoException          se l'utente non viene trovato
     * @throws LibroNonTrovatoException           se il libro non viene trovato
     * @throws RecensioneGiaEsistenteException    se l'utente ha già recensito il libro
     * @throws CatalogazioneNonTrovataException   se il libro non risulta catalogato dall'utente
     * @throws RecensioneNonConsentitaException   se lo stato di lettura non permette la recensione
     */
    public Recensione scrivi(Long idUtente, Long idLibro, String testo, boolean pubblica) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        if (recensioneRepository.existsByUtenteAndLibro(utente, libro)) {
            throw new RecensioneGiaEsistenteException(idUtente, idLibro);
        }

        Catalogazione c = catalogazioneRepository.findByUtenteAndLibro(utente, libro)
                .orElseThrow(() -> new CatalogazioneNonTrovataException(idUtente, idLibro));

        if (!c.getStatoCorrente().permetteRecensione()) {
            throw new RecensioneNonConsentitaException(c.getStato());
        }

        return recensioneRepository.save(new Recensione(utente, libro, testo, pubblica));
    }

    /**
     * Restituisce tutte le recensioni pubbliche associate a uno specifico libro (per i visitatori).
     *
     * @param idLibro l'identificativo del libro di cui cercare le recensioni
     * @return una lista di {@link Recensione} pubbliche
     * @throws LibroNonTrovatoException se il libro non viene trovato
     */
    public List<Recensione> trovaPubblichePerLibro(Long idLibro) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));
        return recensioneRepository.findByLibroAndPubblicaTrue(libro);
    }

    /**
     * Restituisce tutte le recensioni scritte da un determinato utente (le sue recensioni).
     *
     * @param idUtente l'identificativo dell'utente di cui recuperare le recensioni
     * @return una lista di {@link Recensione} scritte dall'utente
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     */
    public List<Recensione> trovaMieRecensioni(Long idUtente) {
        Utente utente = utenteService.trovaPerId(idUtente);
        return recensioneRepository.findByUtente(utente);
    }

    /**
     * Modifica il testo e la visibilità di una recensione esistente.
     *
     * @param idUtente l'identificativo dell'utente proprietario della recensione
     * @param idLibro  l'identificativo del libro recensito
     * @param testo    il nuovo testo della recensione
     * @param pubblica il nuovo flag di visibilità pubblica
     * @return la {@link Recensione} modificata e salvata
     * @throws UtenteNonTrovatoException          se l'utente non viene trovato
     * @throws LibroNonTrovatoException           se il libro non viene trovato
     * @throws RecensioneNonTrovataException      se la recensione non viene trovata
     */
    public Recensione modifica(Long idUtente, Long idLibro, String testo, boolean pubblica) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        Recensione recensione = recensioneRepository.findByUtenteAndLibro(utente, libro)
                .orElseThrow(() -> new RecensioneNonTrovataException(idUtente, idLibro));

        recensione.setTesto(testo);
        recensione.setPubblica(pubblica);

        return recensioneRepository.save(recensione);
    }
}