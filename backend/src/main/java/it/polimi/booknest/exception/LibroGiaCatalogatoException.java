package it.polimi.booknest.exception;

/**
 * Segnala un tentativo di inserimento duplicato nella libreria.
 * Viene sollevata quando si cerca di catalogare un libro che è già stato
 * precedentemente associato all'utente, violando il vincolo di unicità.
 */
public class LibroGiaCatalogatoException extends RuntimeException {

    /**
     * Costruisce l'eccezione formulando automaticamente il messaggio di errore.
     *
     * @param utenteId L'identificativo dell'utente.
     * @param libroId L'identificativo del libro già presente.
     */
    public LibroGiaCatalogatoException(Long utenteId, Long libroId) {
        super("Impossibile catalogare: il libro con ID " + libroId +
                " è già presente nella libreria dell'utente con ID " + utenteId);
    }
}
