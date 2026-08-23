package it.polimi.booknest.exception;

import it.polimi.booknest.model.StatoLettura;

/**
 * Eccezione sollevata quando si tenta di recensire un libro che si trova
 * in uno stato di lettura non idoneo (ad esempio, non ancora completato).
 */
public class RecensioneNonConsentitaException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione con un messaggio dettagliato basato sullo stato di lettura.
     *
     * @param stato lo stato di lettura non consentito
     */
    public RecensioneNonConsentitaException(StatoLettura stato) {
        super("Non è possibile recensire un libro nello stato: " + stato);
    }
}