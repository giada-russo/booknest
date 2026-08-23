package it.polimi.booknest.exception;

import it.polimi.booknest.model.StatoLettura;

/**
 * Segnala un tentativo di violazione delle regole di transizione del Design Pattern State.
 * Viene sollevata quando si richiede un passaggio di stato per un libro che non è
 * previsto o consentito dalle attuali logiche di dominio.
 */
public class TransizioneNonValidaException extends RuntimeException {

    /**
     * Costruisce l'eccezione formulando automaticamente il messaggio di errore
     * in base agli stati coinvolti nel tentativo fallito.
     *
     * @param statoAttuale Lo stato in cui si trova attualmente il libro.
     * @param statoRichiesto Lo stato verso il quale si tentava di transitare.
     */
    public TransizioneNonValidaException(StatoLettura statoAttuale, StatoLettura statoRichiesto) {
        super("Transizione non consentita da " + statoAttuale + " a " + statoRichiesto);
    }
}