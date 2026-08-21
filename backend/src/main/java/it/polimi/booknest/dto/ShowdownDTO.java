package it.polimi.booknest.dto;

import it.polimi.booknest.model.Showdown;

import java.time.LocalDateTime;

/**
 * Data Transfer Object per il trasferimento dei dati di sintesi di uno Showdown.
 * Contiene l'identificativo, la data di creazione e le informazioni testuali
 * dei due libri sfidanti, omettendo l'esposizione delle entità JPA.
 */
public class ShowdownDTO {
    private Long id;
    private String titoloLibroA;
    private String titoloLibroB;
    private String autoreLibroA;
    private String autoreLibroB;
    private LocalDateTime dataCreazione;

    /**
     * Costruisce un nuovo oggetto {@code ShowdownDTO} estraendo i dati
     * necessari direttamente dall'entità {@code Showdown}.
     *
     * @param showdown L'entità JPA sorgente da cui mappare i dati.
     */
    public ShowdownDTO(Showdown showdown) {
        this.id = showdown.getId();
        this.titoloLibroA = showdown.getLibroA().getTitolo();
        this.titoloLibroB = showdown.getLibroB().getTitolo();
        this.autoreLibroA = showdown.getLibroA().getAutore();
        this.autoreLibroB = showdown.getLibroB().getAutore();
        this.dataCreazione = showdown.getDataCreazione();
    }

    public Long getId() {
        return id;
    }

    public String getTitoloLibroA() {
        return titoloLibroA;
    }

    public String getTitoloLibroB() {
        return titoloLibroB;
    }

    public String getAutoreLibroA() {
        return autoreLibroA;
    }

    public String getAutoreLibroB() {
        return autoreLibroB;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
}
