package it.polimi.booknest.exception;

/**
 * Eccezione lanciata quando un utente tenta di votare più di una volta
 * per lo stesso Showdown.
 * <p>
 * Questa eccezione serve a far rispettare la regola di business che garantisce
 * l'unicità del voto per ogni partecipante. Essendo una {@link RuntimeException}
 * (unchecked exception), delega la sua gestione ai livelli superiori, permettendo
 * ad esempio a un Controller di intercettarla e gestire la risposta di errore
 * in modo appropriato.
 * </p>
 */
public class VotoGiaEspressoException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione {@code VotoGiaEspressoException} fornendo
     * un messaggio di errore descrittivo predefinito, utile per i log di sistema
     * o per le risposte client.
     */
    public VotoGiaEspressoException() {
        super("L'utente ha già espresso il suo voto per questo Showdown");
    }
}