package it.polimi.booknest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Rappresenta un libro del catalogo di BookNest.
 * <p>
 * Un libro è l'entità centrale del dominio: gli utenti possono catalogarlo,
 * assegnargli un voto, scrivere recensioni e metterlo a confronto con altri
 * libri all'interno di uno Showdown.
 * L'identificativo è generato automaticamente dal database al momento
 * del salvataggio.
 */
@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titolo;
    private String autore;
    private String isbn;

    public Libro() {
    }

    public Libro(String titolo, String autore, String isbn) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
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
}

