package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando si cerca una recensione associata a un determinato utente
 * e a un determinato libro, o tramite identificativo, ma questa non viene trovata nel sistema.
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

    /**
     * Costruisce una nuova eccezione specificando l'identificativo della recensione non trovata.
     *
     * @param idRecensione l'identificativo della recensione non trovata
     */
    public RecensioneNonTrovataException(Long idRecensione) {
        super("Nessuna recensione trovata con id: " + idRecensione);
    }
}