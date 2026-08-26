package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando un utente tenta di seguire un altro utente
 * che sta già seguendo.
 * <p>
 * Rappresenta una violazione delle regole di dominio e viene mappata
 * a livello HTTP sullo stato {@code 409 Conflict}.
 * </p>
 */
public class GiaSeguitoException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione specificando gli id coinvolti nel tentativo di follow duplicato.
     *
     * @param utenteId  l'identificativo dell'utente che tenta di seguire
     * @param seguitoId l'identificativo dell'utente già seguito
     */
    public GiaSeguitoException(Long utenteId, Long seguitoId) {
        super("L'utente con id " + utenteId + " segue già l'utente con id " + seguitoId);
    }
}