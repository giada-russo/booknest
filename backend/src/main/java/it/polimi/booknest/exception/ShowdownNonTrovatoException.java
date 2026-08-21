package it.polimi.booknest.exception;

/**
 * Eccezione lanciata quando si tenta di recuperare o interagire con uno Showdown
 * che non esiste all'interno del database.
 * <p>
 * Essendo una {@link RuntimeException} (unchecked exception), interrompe il flusso
 * di esecuzione corrente e delega la gestione dell'errore ai livelli superiori
 * (ad esempio al Controller), senza costringere il codice chiamante a gestire
 * l'errore con blocchi try-catch espliciti.
 * </p>
 */
public class ShowdownNonTrovatoException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione {@code ShowdownNonTrovatoException} includendo
     * l'identificativo dello Showdown mancante nel messaggio di errore.
     * <p>
     * Passare l'ID è fondamentale per facilitare il debugging e tracciare esattamente
     * quale risorsa specifica ha generato il problema all'interno dei log.
     * </p>
     *
     * @param id L'identificativo (chiave primaria) dello Showdown non trovato.
     */
    public ShowdownNonTrovatoException(Long id) {
        super("Impossibile trovare lo Showdown con id: " + id);
    }
}