package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando si cerca una recensione associata a un determinato utente
 * e a un determinato libro, ma questa non viene trovata nel sistema.
 */
public class RecensioneNonTrovataException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione con un messaggio dettagliato basato sugli identificativi.
     *
     * @param utenteId l'identificativo dell'utente
     * @param libroId  l'identificativo del libro
     */
    public RecensioneNonTrovataException(Long utenteId, Long libroId) {
        super("Recensione non trovata per l'utente " + utenteId + " e il libro " + libroId);
    }
}