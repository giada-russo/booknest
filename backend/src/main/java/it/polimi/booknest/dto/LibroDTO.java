package it.polimi.booknest.dto;

import it.polimi.booknest.model.Libro;

/**
 * Data Transfer Object (DTO) che rappresenta i dati essenziali di un libro
 * destinati al trasferimento verso il client (es. per la visualizzazione nelle classifiche).
 *
 * <p>Contiene le informazioni principali quali identificativo, titolo, autore, ISBN
 * e opzionalmente la media dei voti ricevuti dagli utenti.
 */
public class LibroDTO {

    private Long id;
    private String titolo;
    private String autore;
    private String isbn;
    private Double votoMedio;

    /**
     * Costruisce una nuova istanza di {@code LibroDTO} estraendo i dati
     * direttamente dall'entità {@link Libro} passata come parametro.
     * Il voto medio resta {@code null}.
     *
     * @param libro l'entità da cui estrarre i dati
     */
    public LibroDTO(Libro libro) {
        this.id = libro.getId();
        this.titolo = libro.getTitolo();
        this.autore = libro.getAutore();
        this.isbn = libro.getIsbn();
    }

    /**
     * Costruisce un {@code LibroDTO} includendo la media dei voti ricevuti dal libro.
     *
     * @param libro      l'entità da cui estrarre i dati
     * @param votoMedio  la media dei voti, {@code null} se nessun utente ha ancora votato
     */
    public LibroDTO(Libro libro, Double votoMedio) {
        this(libro);
        this.votoMedio = votoMedio;
    }

    public Long getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public Double getVotoMedio() {
        return votoMedio;
    }
}