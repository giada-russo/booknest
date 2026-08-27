package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LoginRequest;
import it.polimi.booknest.dto.RegistrazioneRequest;
import it.polimi.booknest.dto.UtenteDTO;
import it.polimi.booknest.exception.AutoFollowException;
import it.polimi.booknest.exception.GiaSeguitoException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.service.UtenteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per la gestione degli utenti, inclusi registrazione,
 * autenticazione (login) e relazioni di follow tra utenti.
 */
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {
    private final UtenteService utenteService;

    /**
     * Inizializza il controller con il servizio di gestione degli utenti.
     *
     * @param utenteService il servizio {@link UtenteService} utilizzato per la logica di business degli utenti
     */
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param richiesta il DTO contenente i dati di registrazione (nome, cognome, username, email, password)
     * @return un {@link UtenteDTO} con i dati dell'utente appena registrato
     */
    @PostMapping("/registrazione")
    public UtenteDTO registra(@RequestBody RegistrazioneRequest richiesta){

        Utente utente = utenteService.registra(
                richiesta.getNome(),
                richiesta.getCognome(),
                richiesta.getUsername(),
                richiesta.getEmail(),
                richiesta.getPassword()
        );
        return new UtenteDTO(utente);
    }

    /**
     * Effettua l'autenticazione di un utente esistente.
     *
     * @param richiesta il DTO contenente le credenziali di accesso (username e password)
     * @return un {@link UtenteDTO} con i dati dell'utente autenticato
     */
    @PostMapping("/login")
    public UtenteDTO login(@RequestBody LoginRequest richiesta){

        Utente utente = utenteService.login(
                richiesta.getUsername(),
                richiesta.getPassword()
        );
        return new UtenteDTO(utente);
    }

    /**
     * Registra il fatto che l'utente autenticato inizi a seguire un altro utente.
     *
     * @param utenteId  l'identificativo dell'utente che segue, dall'header HTTP
     * @param idSeguito l'identificativo dell'utente da seguire
     * @throws UtenteNonTrovatoException se uno dei due utenti non esiste
     * @throws AutoFollowException       se l'utente tenta di seguire se stesso
     * @throws GiaSeguitoException       se lo segue già
     */
    @PostMapping("/seguiti/{idSeguito}")
    public void segui(@RequestHeader("X-Utente-Id") Long utenteId,
                      @PathVariable Long idSeguito) {
        utenteService.segui(utenteId, idSeguito);
    }

    /**
     * Interrompe la relazione di follow verso un altro utente.
     *
     * @param utenteId  l'identificativo dell'utente che smette di seguire, dall'header HTTP
     * @param idSeguito l'identificativo dell'utente da non seguire più
     * @throws UtenteNonTrovatoException se uno dei due utenti non esiste
     */
    @DeleteMapping("/seguiti/{idSeguito}")
    public void smettiDiSeguire(@RequestHeader("X-Utente-Id") Long utenteId,
                                @PathVariable Long idSeguito) {
        utenteService.smettiDiSeguire(utenteId, idSeguito);
    }

    /**
     * Restituisce gli utenti seguiti dall'utente autenticato.
     *
     * @param utenteId l'identificativo dell'utente, dall'header HTTP
     * @return la lista degli {@link UtenteDTO} seguiti
     * @throws UtenteNonTrovatoException se l'utente non esiste
     */
    @GetMapping("/seguiti")
    public List<UtenteDTO> trovaSeguiti(@RequestHeader("X-Utente-Id") Long utenteId) {
        return utenteService.trovaSeguiti(utenteId)
                .stream()
                .map(UtenteDTO::new)
                .toList();
    }
}