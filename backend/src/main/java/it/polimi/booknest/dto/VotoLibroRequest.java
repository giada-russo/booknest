package it.polimi.booknest.dto;

/**
 * DTO (Data Transfer Object) di richiesta per l'assegnazione o la modifica del voto di un libro.
 * <p>
 * Questa classe viene utilizzata per ricevere dal client il valore intero del voto
 * da associare a una catalogazione esistente tramite una chiamata HTTP.
 * </p>
 */
public class VotoLibroRequest {
    private int voto;

    /**
     * Costruttore vuoto richiesto da Jackson per la deserializzazione del JSON in arrivo dal client.
     */
    public VotoLibroRequest(){}

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}