package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando un utente tenta di seguire se stesso.
 * <p>
 * Rappresenta una violazione delle regole di dominio e viene mappata
 * a livello HTTP sullo stato {@code 409 Conflict}.
 * </p>
 */
public class AutoFollowException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione specificando l'id dell'utente che ha tentato l'auto-follow.
     *
     * @param utenteId l'identificativo dell'utente che tenta di seguire se stesso
     */
    public AutoFollowException(Long utenteId) {
        super("L'utente con id " + utenteId + " non può seguire se stesso");
    }
}