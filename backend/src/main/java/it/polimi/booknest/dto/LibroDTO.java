package it.polimi.booknest.dto;

import it.polimi.booknest.model.Libro;

/**
 * Data Transfer Object (DTO) che rappresenta i dati essenziali di un libro
 * destinati al trasferimento verso il client (es. per la visualizzazione nelle classifiche).
 *
 * <p>Contiene le informazioni principali quali identificativo, titolo e autore.
 */
public class LibroDTO {
    private Long id;
    private String titolo;
    private String autore;

    /**
     * Costruisce una nuova istanza di {@code LibroDTO} estraendo i dati
     * direttamente dall'entità {@link Libro} passata come parametro.
     *
     * @param libro l'entità da cui estrarre i dati
     */
    public LibroDTO(Libro libro) {
        this.id = libro.getId();
        this.titolo = libro.getTitolo();
        this.autore = libro.getAutore();
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
}