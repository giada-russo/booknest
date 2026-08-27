package it.polimi.booknest.dto;

import it.polimi.booknest.model.Catalogazione;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) di risposta per l'entità {@link Catalogazione}.
 * <p>
 * Questa classe viene utilizzata per inviare al client le informazioni essenziali
 * e formattate relative alla catalogazione di un libro, evitando di esporre
 * direttamente le entità JPA e le loro relazioni complesse.
 * </p>
 */
public class CatalogazioneDTO {

    private Long id;
    private Long idLibro;
    private String titoloLibro;
    private String autoreLibro;
    private String stato;
    private Integer voto;
    private LocalDateTime dataCompletamento;

    /**
     * Costruisce un nuovo {@code CatalogazioneDTO} a partire da un'entità {@link Catalogazione}.
     * Estrae e appiattisce i dati necessari, inclusi i dettagli del libro associato.
     *
     * @param catalogazione l'entità da cui estrarre i dati
     */
    public CatalogazioneDTO(Catalogazione catalogazione) {
        this.id = catalogazione.getId();
        this.idLibro = catalogazione.getLibro().getId();
        this.titoloLibro = catalogazione.getLibro().getTitolo();
        this.autoreLibro = catalogazione.getLibro().getAutore();
        this.stato = catalogazione.getStato().name();
        this.voto = catalogazione.getVoto();
        this.dataCompletamento = catalogazione.getDataCompletamento();
    }

    public Long getId() {
        return id;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public String getTitoloLibro() {
        return titoloLibro;
    }

    public String getAutoreLibro() {
        return autoreLibro;
    }

    public String getStato() {
        return stato;
    }

    public Integer getVoto() {
        return voto;
    }

    public LocalDateTime getDataCompletamento() {
        return dataCompletamento;
    }
}