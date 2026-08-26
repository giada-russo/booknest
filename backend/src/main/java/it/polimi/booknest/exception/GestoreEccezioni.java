package it.polimi.booknest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestore globale delle eccezioni per intercettare gli errori di business
 * e tradurli in risposte HTTP coerenti con messaggi leggibili dal client.
 */
@RestControllerAdvice
public class GestoreEccezioni {

    /**
     * Scatta quando una risorsa richiesta non viene trovata nel sistema.
     */
    @ExceptionHandler({
            CatalogazioneNonTrovataException.class,
            LibroNonTrovatoException.class,
            RecensioneNonTrovataException.class,
            ShowdownNonTrovatoException.class,
            UtenteNonTrovatoException.class
    })
    public ResponseEntity<String> gestisciNonTrovato(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Gestisce i conflitti con lo stato corrente della risorsa: duplicati e operazioni non consentite dallo stato della catalogazione.
     */
    @ExceptionHandler({
            EmailGiaEsistenteException.class,
            LibroGiaCatalogatoException.class,
            RecensioneGiaEsistenteException.class,
            UsernameGiaEsistenteException.class,
            VotoGiaEspressoException.class,
            ShowdownNonAttivoException.class,
            TransizioneNonValidaException.class,
            RecensioneNonConsentitaException.class,
            VotoNonConsentitoException.class
    })
    public ResponseEntity<String> gestisciConflitto(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * Scatta quando viene fornito un criterio o un parametro di input non valido.
     */
    @ExceptionHandler({
            CriterioNonValidoException.class
    })
    public ResponseEntity<String> gestisciRichiestaNonValida(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Scatta quando il tentativo di autenticazione fallisce per credenziali errate.
     */
    @ExceptionHandler({
            CredenzialiNonValideException.class
    })
    public ResponseEntity<String> gestisciCredenzialiNonValide(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}