package it.polimi.booknest.exception;

/**
 * Eccezione personalizzata sollevata quando viene richiesto un criterio di classifica
 * inesistente o non riconosciuto dal sistema.
 *
 /**
 * Eccezione personalizzata sollevata quando viene richiesto un criterio di classifica
 * inesistente o non riconosciuto dal sistema.
 *
 * <p>Estende {@link RuntimeException} configurandosi come eccezione di tipo unchecked.</p>
 */
public class CriterioNonValidoException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione indicando il criterio non valido che ha generato l'errore.
     *
     * @param nomeCriterio l'identificativo testuale del criterio non valido passato dal client
     */
    public CriterioNonValidoException(String nomeCriterio) {
        super("Criterio di classifica non valido: " + nomeCriterio);
    }
}
