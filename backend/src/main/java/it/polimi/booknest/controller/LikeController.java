package it.polimi.booknest.controller;

import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.LikeGiaEspressoException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.service.LikeService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per la gestione degli apprezzamenti (like)
 * sia per i libri che per le recensioni nel sistema BookNest.
 */
@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    /**
     * Inizializza il controller con il servizio di gestione dei like.
     *
     * @param likeService il servizio {@link LikeService} utilizzato per la logica di business dei like
     */
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Permette a un utente di esprimere un apprezzamento (like) per un libro.
     *
     * @param utenteId l'identificativo dell'utente ricavato dall'header della richiesta
     * @param idLibro  l'identificativo del libro apprezzato
     * @throws UtenteNonTrovatoException se l'utente non esiste
     * @throws LibroNonTrovatoException  se il libro non esiste
     * @throws LikeGiaEspressoException  se l'utente ha già messo like a questo libro
     */
    @PostMapping("/libri/{idLibro}")
    public void metteLikeLibro(@RequestHeader("X-Utente-Id") Long utenteId,
                               @PathVariable Long idLibro) {
        likeService.metteLikeLibro(utenteId, idLibro);
    }

    /**
     * Rimuove il like espresso da un utente su un libro, se presente.
     *
     * @param utenteId l'identificativo dell'utente ricavato dall'header della richiesta
     * @param idLibro  l'identificativo del libro
     * @throws UtenteNonTrovatoException se l'utente non esiste
     * @throws LibroNonTrovatoException  se il libro non esiste
     */
    @DeleteMapping("/libri/{idLibro}")
    public void togliLikeLibro(@RequestHeader("X-Utente-Id") Long utenteId,
                               @PathVariable Long idLibro) {
        likeService.togliLikeLibro(utenteId, idLibro);
    }

    /**
     * Conta gli apprezzamenti ricevuti da un libro.
     * <p>
     * Endpoint di sola lettura, accessibile anche ai visitatori senza autenticazione.
     *
     * @param idLibro l'identificativo del libro
     * @return il numero di like ricevuti
     * @throws LibroNonTrovatoException se il libro non esiste
     */
    @GetMapping("/libri/{idLibro}/conteggio")
    public long contaLikeLibro(@PathVariable Long idLibro) {
        return likeService.contaLikeLibro(idLibro);
    }

    /**
     * Permette a un utente di esprimere un apprezzamento (like) per una recensione.
     *
     * @param utenteId     l'identificativo dell'utente ricavato dall'header della richiesta
     * @param idRecensione l'identificativo della recensione apprezzata
     * @throws UtenteNonTrovatoException     se l'utente non esiste
     * @throws RecensioneNonTrovataException se la recensione non esiste
     * @throws LikeGiaEspressoException      se l'utente ha già messo like a questa recensione
     */
    @PostMapping("/recensioni/{idRecensione}")
    public void metteLikeRecensione(@RequestHeader("X-Utente-Id") Long utenteId,
                                    @PathVariable Long idRecensione) {
        likeService.metteLikeRecensione(utenteId, idRecensione);
    }

    /**
     * Rimuove il like espresso da un utente su una recensione, se presente.
     *
     * @param utenteId     l'identificativo dell'utente ricavato dall'header della richiesta
     * @param idRecensione l'identificativo della recensione
     * @throws UtenteNonTrovatoException     se l'utente non esiste
     * @throws RecensioneNonTrovataException se la recensione non esiste
     */
    @DeleteMapping("/recensioni/{idRecensione}")
    public void togliLikeRecensione(@RequestHeader("X-Utente-Id") Long utenteId,
                                    @PathVariable Long idRecensione) {
        likeService.togliLikeRecensione(utenteId, idRecensione);
    }

    /**
     * Conta gli apprezzamenti ricevuti da una recensione.
     * <p>
     * Endpoint di sola lettura, accessibile anche ai visitatori senza autenticazione.
     *
     * @param idRecensione l'identificativo della recensione
     * @return il numero di like ricevuti
     * @throws RecensioneNonTrovataException se la recensione non esiste
     */
    @GetMapping("/recensioni/{idRecensione}/conteggio")
    public long contaLikeRecensione(@PathVariable Long idRecensione) {
        return likeService.contaLikeRecensione(idRecensione);
    }
}