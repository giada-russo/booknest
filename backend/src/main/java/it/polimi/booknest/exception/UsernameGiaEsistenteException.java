package it.polimi.booknest.exception;

/**
 * Segnala il tentativo di registrare un utente con uno username già presente
 * nel sistema.
 * <p>
 * Lo username identifica univocamente un utente in fase di accesso, quindi
 * non può essere condiviso da più account. L'eccezione viene sollevata dal
 * livello di servizio durante la registrazione, prima che l'utente venga
 * persistito.
 */
public class UsernameGiaEsistenteException extends RuntimeException {

    /**
     * @param username lo username già in uso che ha causato il conflitto
     */
    public UsernameGiaEsistenteException(String username) {

        super("Username già in uso: " + username);
    }
}