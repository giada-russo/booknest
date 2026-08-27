package it.polimi.booknest.service;

import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.LikeGiaEspressoException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LikeLibro;
import it.polimi.booknest.model.LikeRecensione;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.LikeLibroRepository;
import it.polimi.booknest.repository.LikeRecensioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.RecensioneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service che gestisce la logica applicativa relativa ai like (apprezzamenti)
 * sia per i libri che per le recensioni nel sistema BookNest.
 */
@Service
public class LikeService {

    private final LikeLibroRepository likeLibroRepository;
    private final LikeRecensioneRepository likeRecensioneRepository;
    private final LibroRepository libroRepository;
    private final RecensioneRepository recensioneRepository;
    private final UtenteService utenteService;

    public LikeService(LikeLibroRepository likeLibroRepository,
                       LikeRecensioneRepository likeRecensioneRepository,
                       LibroRepository libroRepository,
                       RecensioneRepository recensioneRepository,
                       UtenteService utenteService) {
        this.likeLibroRepository = likeLibroRepository;
        this.likeRecensioneRepository = likeRecensioneRepository;
        this.libroRepository = libroRepository;
        this.recensioneRepository = recensioneRepository;
        this.utenteService = utenteService;
    }

    /**
     * Permette a un utente di esprimere un apprezzamento (like) per un libro.
     * <p>
     * Verifica l'esistenza dell'utente e del libro, controlla che non sia già stato
     * espresso un like precedente e persiste la nuova associazione.
     *
     * @param idUtente l'identificativo dell'utente che esprime il like
     * @param idLibro  l'identificativo del libro apprezzato
     * @throws UtenteNonTrovatoException se l'utente non esiste
     * @throws LibroNonTrovatoException  se il libro non esiste
     * @throws LikeGiaEspressoException  se l'utente ha già messo like a questo libro
     */
    public void metteLikeLibro(Long idUtente, Long idLibro) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        if (likeLibroRepository.existsByUtenteAndLibro(utente, libro)) {
            throw new LikeGiaEspressoException(idUtente, idLibro);
        }

        LikeLibro like = new LikeLibro(utente, libro);
        likeLibroRepository.save(like);
    }

    /**
     * Rimuove il like espresso da un utente su un libro, se presente.
     * <p>
     * L'operazione è idempotente: se il like non esiste, l'azione non ha effetto.
     *
     * @param idUtente l'identificativo dell'utente che ritira il like
     * @param idLibro  l'identificativo del libro
     * @throws UtenteNonTrovatoException se l'utente non esiste
     * @throws LibroNonTrovatoException  se il libro non esiste
     */
    @Transactional
    public void togliLikeLibro(Long idUtente, Long idLibro) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        likeLibroRepository.deleteByUtenteAndLibro(utente, libro);
    }

    /**
     * Conta gli apprezzamenti ricevuti da un libro.
     * <p>
     * Endpoint di sola lettura, accessibile anche ai visitatori.
     *
     * @param idLibro l'identificativo del libro
     * @return il numero di like ricevuti
     * @throws LibroNonTrovatoException se il libro non esiste
     */
    public long contaLikeLibro(Long idLibro) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));
        return likeLibroRepository.countByLibro(libro);
    }

    /**
     * Permette a un utente di esprimere un apprezzamento (like) per una recensione.
     * <p>
     * Verifica l'esistenza dell'utente e della recensione, controlla che non sia già stato
     * espresso un like precedente e persiste la nuova associazione.
     *
     * @param idUtente      l'identificativo dell'utente che esprime il like
     * @param idRecensione  l'identificativo della recensione apprezzata
     * @throws UtenteNonTrovatoException     se l'utente non esiste
     * @throws RecensioneNonTrovataException se la recensione non esiste
     * @throws LikeGiaEspressoException      se l'utente ha già messo like a questa recensione
     */
    public void metteLikeRecensione(Long idUtente, Long idRecensione) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Recensione recensione = recensioneRepository.findById(idRecensione)
                .orElseThrow(() -> new RecensioneNonTrovataException(idRecensione));

        if (likeRecensioneRepository.existsByUtenteAndRecensione(utente, recensione)) {
            throw new LikeGiaEspressoException(idUtente, idRecensione);
        }

        LikeRecensione like = new LikeRecensione(utente, recensione);
        likeRecensioneRepository.save(like);
    }

    /**
     * Rimuove il like espresso da un utente su una recensione, se presente.
     * <p>
     * L'operazione è idempotente: se il like non esiste, l'azione non ha effetto.
     *
     * @param idUtente     l'identificativo dell'utente che ritira il like
     * @param idRecensione l'identificativo della recensione
     * @throws UtenteNonTrovatoException     se l'utente non esiste
     * @throws RecensioneNonTrovataException se la recensione non esiste
     */
    @Transactional
    public void togliLikeRecensione(Long idUtente, Long idRecensione) {
        Utente utente = utenteService.trovaPerId(idUtente);
        Recensione recensione = recensioneRepository.findById(idRecensione)
                .orElseThrow(() -> new RecensioneNonTrovataException(idRecensione));

        likeRecensioneRepository.deleteByUtenteAndRecensione(utente, recensione);
    }

    /**
     * Conta gli apprezzamenti ricevuti da una recensione.
     * <p>
     * Endpoint di sola lettura, accessibile anche ai visitatori.
     *
     * @param idRecensione l'identificativo della recensione
     * @return il numero di like ricevuti
     * @throws RecensioneNonTrovataException se la recensione non esiste
     */
    public long contaLikeRecensione(Long idRecensione) {
        Recensione recensione = recensioneRepository.findById(idRecensione)
                .orElseThrow(() -> new RecensioneNonTrovataException(idRecensione));
        return likeRecensioneRepository.countByRecensione(recensione);
    }
}