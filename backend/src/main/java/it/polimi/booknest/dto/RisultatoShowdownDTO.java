package it.polimi.booknest.dto;

/**
 * Data Transfer Object (DTO) che rappresenta il risultato finale o parziale di uno showdown.
 * Contiene l'identificativo dello showdown e i conteggi dei voti
 * ottenuti dalle due opzioni in gara (A e B).
 */
public class RisultatoShowdownDTO {
    private Long idShowdown;
    private int conteggioA;
    private int conteggioB;

    /**
     * Costruisce un nuovo oggetto {@code RisultatoShowdownDTO} con i valori specificati.
     *
     * @param idShowdown L'identificatore univoco dello showdown.
     * @param conteggioA Il punteggio o numero di voti ottenuto dalla prima opzione (A).
     * @param conteggioB Il punteggio o numero di voti ottenuto dalla seconda opzione (B).
     */
    public RisultatoShowdownDTO(Long idShowdown, int conteggioA, int conteggioB) {
        this.idShowdown = idShowdown;
        this.conteggioA = conteggioA;
        this.conteggioB = conteggioB;
    }

    public Long getIdShowdown() {
        return idShowdown;
    }
    public int  getConteggioA() {
        return conteggioA;
    }
    public int getConteggioB() {
        return conteggioB;
    }
}
