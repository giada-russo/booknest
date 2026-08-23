package it.polimi.booknest.dto;

import it.polimi.booknest.model.StatoLettura;

/**
 * DTO (Data Transfer Object) di richiesta per l'aggiornamento dello stato di lettura di un libro.
 * <p>
 * Questa classe viene utilizzata per ricevere dal client il nuovo {@link StatoLettura}
 * da applicare a una catalogazione esistente tramite una chiamata HTTP di aggiornamento.
 * </p>
 */
public class CambioStatoRequest {
    private StatoLettura nuovoStato;

    /**
     * Costruttore vuoto richiesto da Jackson per la deserializzazione del JSON in arrivo dal client.
     */
    public CambioStatoRequest(){}

    public StatoLettura getNuovoStato() {
        return nuovoStato;
    }

    public void setNuovoStato(StatoLettura nuovoStato) {
        this.nuovoStato = nuovoStato;
    }
}