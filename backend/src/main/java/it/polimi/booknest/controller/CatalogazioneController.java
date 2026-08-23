package it.polimi.booknest.controller;

import it.polimi.booknest.dto.CatalogazioneDTO;
import it.polimi.booknest.dto.CambioStatoRequest;
import it.polimi.booknest.dto.VotoLibroRequest;
import it.polimi.booknest.exception.CatalogazioneNonTrovataException;
import it.polimi.booknest.exception.LibroGiaCatalogatoException;
import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.TransizioneNonValidaException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.exception.VotoNonConsentitoException;
import it.polimi.booknest.service.CatalogazioneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per la gestione delle catalogazioni dei libri.
 * <p>
 * Questo controller funge da adattatore tra il client e i servizi di business,
 * senza contenere logica applicativa. Le eccezioni sollevate dal service si propagano al chiamante.
 * </p>
 * <p>
 * <b>Nota:</b> Trattandosi di una libreria personale, tutti gli endpoint
 * richiedono l'intestazione HTTP {@code X-Utente-Id} per identificare l'utente.
 * </p>
 */
@RestController
@RequestMapping("/api/catalogazioni")
public class CatalogazioneController {

    private final CatalogazioneService catalogazioneService;

    /**
     * Costruttore unico per l'iniezione delle dipendenze del service.
     *
     * @param catalogazioneService il service che gestisce la logica di business delle catalogazioni
     */
    public CatalogazioneController(CatalogazioneService catalogazioneService) {
        this.catalogazioneService = catalogazioneService;
    }

    /**
     * Restituisce la libreria personale dell'utente autenticato.
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @return una lista di {@link CatalogazioneDTO} che rappresentano i libri catalogati dall'utente
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     */
    @GetMapping
    public List<CatalogazioneDTO> trovaLibreria(@RequestHeader("X-Utente-Id") Long utenteId) {
        return catalogazioneService.trovaLibreria(utenteId)
                .stream()
                .map(CatalogazioneDTO::new)
                .toList();
    }

    /**
     * Aggiunge un libro alla libreria personale dell'utente (catalogazione).
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @param idLibro  l'identificativo del libro da catalogare estratto dall'URL
     * @return il {@link CatalogazioneDTO} della nuova catalogazione creata
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     * @throws LibroNonTrovatoException se il libro non viene trovato
     * @throws LibroGiaCatalogatoException se il libro è già stato catalogato dall'utente
     */
    @PostMapping("/{idLibro}")
    public CatalogazioneDTO cataloga(@RequestHeader("X-Utente-Id") Long utenteId,
                                     @PathVariable Long idLibro) {
        return new CatalogazioneDTO(catalogazioneService.cataloga(utenteId, idLibro));
    }

    /**
     * Aggiorna lo stato di lettura di un libro catalogato.
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @param idLibro  l'identificativo del libro di cui aggiornare lo stato
     * @param richiesta il {@link CambioStatoRequest} contenente il nuovo stato di lettura
     * @return il {@link CatalogazioneDTO} aggiornato
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     * @throws LibroNonTrovatoException se il libro non viene trovato
     * @throws CatalogazioneNonTrovataException se la catalogazione non viene trovata
     * @throws TransizioneNonValidaException se il cambio di stato non è consentito
     */
    @PutMapping("/{idLibro}/stato")
    public CatalogazioneDTO cambiaStato(@RequestHeader("X-Utente-Id") Long utenteId,
                                        @PathVariable Long idLibro,
                                        @RequestBody CambioStatoRequest richiesta) {
        return new CatalogazioneDTO(catalogazioneService.cambiaStato(utenteId, idLibro, richiesta.getNuovoStato()));
    }

    /**
     * Assegna o modifica il voto di un libro catalogato.
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @param idLibro  l'identificativo del libro a cui assegnare il voto
     * @param richiesta il {@link VotoLibroRequest} contenente il valore del voto
     * @return il {@link CatalogazioneDTO} aggiornato con il nuovo voto
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     * @throws LibroNonTrovatoException se il libro non viene trovato
     * @throws CatalogazioneNonTrovataException se la catalogazione non viene trovata
     * @throws VotoNonConsentitoException se il voto non è consentito
     * @throws IllegalArgumentException se il voto non rispetta i vincoli previsti
     */
    @PutMapping("/{idLibro}/voto")
    public CatalogazioneDTO assegnaVoto(@RequestHeader("X-Utente-Id") Long utenteId,
                                        @PathVariable Long idLibro,
                                        @RequestBody VotoLibroRequest richiesta) {
        return new CatalogazioneDTO(catalogazioneService.assegnaVoto(utenteId, idLibro, richiesta.getVoto()));
    }

    /**
     * Restituisce il diario di lettura dell'utente autenticato (libri con stato LETTO),
     * ordinati dalla data di completamento più recente.
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @return una lista di {@link CatalogazioneDTO} che rappresentano la cronologia delle letture
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     */
    @GetMapping("/diario")
    public List<CatalogazioneDTO> trovaDiario(@RequestHeader("X-Utente-Id") Long utenteId) {
        return catalogazioneService.trovaDiario(utenteId)
                .stream()
                .map(CatalogazioneDTO::new)
                .toList();
    }
}