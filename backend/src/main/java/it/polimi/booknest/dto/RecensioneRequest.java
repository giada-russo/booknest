package it.polimi.booknest.dto;

/**
 * DTO (Data Transfer Object) di richiesta per la creazione o la modifica di una recensione.
 * <p>
 * Questa classe viene utilizzata per ricevere dal client il testo e lo stato di visibilità
 * (pubblica o privata), condivisi sia dall'endpoint di scrittura che da quello di modifica.
 * </p>
 */
public class RecensioneRequest {

    private String testo;
    private boolean pubblica;

    /**
     * Costruttore vuoto richiesto da Jackson per la deserializzazione del JSON in arrivo dal client.
     */
    public RecensioneRequest() {}

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public boolean isPubblica() {
        return pubblica;
    }

    public void setPubblica(boolean pubblica) {
        this.pubblica = pubblica;
    }
}