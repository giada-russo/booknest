package it.polimi.booknest.dto;

import it.polimi.booknest.model.Recensione;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) di risposta per l'entità {@link Recensione}.
 * <p>
 * Espone in forma piatta solo le informazioni necessarie al client (inclusi i dettagli
 * dell'autore e del libro), proteggendo i dati sensibili dell'utente (come l'email)
 * specialmente nelle visualizzazioni pubbliche per i visitatori.
 * </p>
 */
public class RecensioneDTO {

    private Long id;
    private String testo;
    private boolean pubblica;
    private LocalDateTime dataCreazione;
    private String usernameAutore;
    private Long idAutore;
    private String titoloLibro;

    /**
     * Costruisce un nuovo {@code RecensioneDTO} a partire da un'entità {@link Recensione}.
     * Estrae e appiattisce i campi necessari.
     *
     * @param recensione l'entità da cui estrarre i dati
     */
    public RecensioneDTO(Recensione recensione) {
        this.id = recensione.getId();
        this.testo = recensione.getTesto();
        this.pubblica = recensione.isPubblica();
        this.dataCreazione = recensione.getDataCreazione();
        this.usernameAutore = recensione.getUtente().getUsername();
        this.idAutore = recensione.getUtente().getId();
        this.titoloLibro = recensione.getLibro().getTitolo();
    }

    public Long getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }

    public boolean isPubblica() {
        return pubblica;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public String getUsernameAutore() {
        return usernameAutore;
    }

    public Long getIdAutore() {
        return idAutore;
    }

    public String getTitoloLibro() {
        return titoloLibro;
    }
}