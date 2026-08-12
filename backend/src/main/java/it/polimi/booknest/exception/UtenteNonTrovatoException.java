package it.polimi.booknest.exception;

/**
 * Eccezione di dominio sollevata quando si tenta di recuperare un utente inesistente.
 * <p>
 * Questa eccezione viene lanciata dal Service quando, interrogando il database,
 * il repository restituisce un {@code Optional} vuoto. Non è la query in sé a fallire,
 * ma è il Service a interpretare l'assenza di dati come un errore di dominio.
 * <p>
 * Copre due scenari di ricerca:
 * <ul>
 *   <li>Tramite username, tipicamente durante la fase di accesso.</li>
 *   <li>Tramite identificativo univoco (ID), utilizzato per validare l'identità
 *       necessaria per le operazioni di dominio.</li>
 * </ul>
 */
public class UtenteNonTrovatoException extends RuntimeException {

    /**
     * Costruisce l'eccezione indicando che la ricerca tramite username ha fallito.
     *
     * @param username lo username fornito che non corrisponde ad alcun utente
     */
    public UtenteNonTrovatoException(String username) {
        super("Utente non trovato con username: " + username);
    }

    /**
     * Costruisce l'eccezione indicando che la ricerca tramite ID ha fallito.
     *
     * @param id l'identificativo fornito che non corrisponde ad alcun utente
     */
    public UtenteNonTrovatoException(Long id) {
        super("Utente non trovato con id: " + id);
    }
}