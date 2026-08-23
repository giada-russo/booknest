package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando si tenta di inserire una recensione per un libro
 * che l'utente ha già recensito in precedenza.
 */
public class RecensioneGiaEsistenteException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione con un messaggio dettagliato basato sugli identificativi.
     *
     * @param utenteId l'identificativo dell'utente
     * @param libroId  l'identificativo del libro
     */
    public RecensioneGiaEsistenteException(Long utenteId, Long libroId) {
        super("L'utente " + utenteId + " ha già recensito il libro " + libroId);
    }
}