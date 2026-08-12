package it.polimi.booknest.exception;

/**
 * Segnala che la password fornita non corrisponde a quella dell'utente indicato.
 * <p>
 * A differenza di {@link UtenteNonTrovatoException}, l'utente esiste ma la
 * verifica del digest non ha avuto esito positivo. Il messaggio non riporta
 * alcun dato relativo alle credenziali inserite.
 */
public class CredenzialiNonValideException extends RuntimeException {
    public CredenzialiNonValideException() {
        super("Credenziali non valide");
    }
}
