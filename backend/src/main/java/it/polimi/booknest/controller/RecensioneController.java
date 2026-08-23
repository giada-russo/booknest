package it.polimi.booknest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.polimi.booknest.dto.RecensioneDTO;
import it.polimi.booknest.dto.RecensioneRequest;
import it.polimi.booknest.exception.CatalogazioneNonTrovataException;
import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.RecensioneGiaEsistenteException;
import it.polimi.booknest.exception.RecensioneNonConsentitaException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.service.RecensioneService;

/**
 * Controller REST per la gestione delle recensioni dei libri.
 * <p>
 * Questo controller funge da adattatore tra il client e i servizi di business,
 * senza contenere logica applicativa. Le eccezioni sollevate dal service si propagano al chiamante.
 * </p>
 * <p>
 * <b>Nota sull'accesso:</b> Gli endpoint di scrittura, modifica e consultazione delle proprie recensioni
 * richiedono l'intestazione HTTP {@code X-Utente-Id} per identificare l'utente. L'endpoint di consultazione
 * delle recensioni di un libro è invece pubblico e accessibile ai visitatori senza autenticazione.
 * </p>
 */
@RestController
@RequestMapping("/api/recensioni")
public class RecensioneController {

    private final RecensioneService recensioneService;

    /**
     * Costruttore unico per l'iniezione delle dipendenze del service.
     *
     * @param recensioneService il service che gestisce la logica di business delle recensioni
     */
    public RecensioneController(RecensioneService recensioneService) {
        this.recensioneService = recensioneService;
    }

    /**
     * Scrive una nuova recensione per un libro catalogato.
     *
     * @param utenteId  l'identificativo dell'utente estratto dall'header HTTP
     * @param idLibro   l'identificativo del libro recensito estratto dall'URL
     * @param richiesta il {@link RecensioneRequest} contenente il testo e il flag di visibilità
     * @return il {@link RecensioneDTO} della nuova recensione creata
     * @throws UtenteNonTrovatoException         se l'utente non viene trovato
     * @throws LibroNonTrovatoException          se il libro non viene trovato
     * @throws RecensioneGiaEsistenteException   se l'utente ha già recensito il libro
     * @throws CatalogazioneNonTrovataException  se il libro non risulta catalogato dall'utente
     * @throws RecensioneNonConsentitaException  se lo stato di lettura non permette la recensione
     */
    @PostMapping("/{idLibro}")
    public RecensioneDTO scrivi(@RequestHeader("X-Utente-Id") Long utenteId,
                                @PathVariable Long idLibro,
                                @RequestBody RecensioneRequest richiesta) {
        return new RecensioneDTO(
                recensioneService.scrivi(utenteId, idLibro, richiesta.getTesto(), richiesta.isPubblica())
        );
    }

    /**
     * Modifica una recensione esistente.
     *
     * @param utenteId  l'identificativo dell'utente estratto dall'header HTTP
     * @param idLibro   l'identificativo del libro recensito estratto dall'URL
     * @param richiesta il {@link RecensioneRequest} contenente il nuovo testo e visibilità
     * @return il {@link RecensioneDTO} aggiornato
     * @throws UtenteNonTrovatoException       se l'utente non viene trovato
     * @throws LibroNonTrovatoException        se il libro non viene trovato
     * @throws RecensioneNonTrovataException   se la recensione non viene trovata
     */
    @PutMapping("/{idLibro}")
    public RecensioneDTO modifica(@RequestHeader("X-Utente-Id") Long utenteId,
                                  @PathVariable Long idLibro,
                                  @RequestBody RecensioneRequest richiesta) {
        return new RecensioneDTO(
                recensioneService.modifica(utenteId, idLibro, richiesta.getTesto(), richiesta.isPubblica())
        );
    }

    /**
     * Restituisce tutte le recensioni pubbliche associate a uno specifico libro.
     * Endpoint pubblico per i visitatori (nessun header richiesto).
     *
     * @param idLibro l'identificativo del libro di cui consultare le recensioni
     * @return una lista di {@link RecensioneDTO} pubbliche
     * @throws LibroNonTrovatoException se il libro non viene trovato
     */
    @GetMapping("/libro/{idLibro}")
    public List<RecensioneDTO> trovaPubblichePerLibro(@PathVariable Long idLibro) {
        return recensioneService.trovaPubblichePerLibro(idLibro)
                .stream()
                .map(RecensioneDTO::new)
                .toList();
    }

    /**
     * Restituisce tutte le recensioni scritte dall'utente autenticato (le mie recensioni).
     *
     * @param utenteId l'identificativo dell'utente estratto dall'header HTTP
     * @return una lista di {@link RecensioneDTO} scritte dall'utente
     * @throws UtenteNonTrovatoException se l'utente non viene trovato
     */
    @GetMapping("/mie")
    public List<RecensioneDTO> trovaMieRecensioni(@RequestHeader("X-Utente-Id") Long utenteId) {
        return recensioneService.trovaMieRecensioni(utenteId)
                .stream()
                .map(RecensioneDTO::new)
                .toList();
    }
}