package it.polimi.booknest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entità JPA che rappresenta la recensione di un libro scritta da un utente.
 * <p>
 * È vincolata da un vincolo di unicità ({@link UniqueConstraint}) sulla coppia
 * utente-libro, garantendo che ciascun utente possa recensire un determinato libro una sola volta.
 * </p>
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"utente_id", "libro_id"}))
public class Recensione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Utente utente;

    @ManyToOne(optional = false)
    private Libro libro;

    /**
     * Il testo della recensione. La lunghezza è impostata a 2000 caratteri
     * poiché il default di 255 caratteri fornito da JPA sarebbe insufficiente
     * per una recensione articolata.
     */
    @Column(nullable = false, length = 2000)
    private String testo;

    private boolean pubblica;
    private LocalDateTime dataCreazione;

    /**
     * Costruttore vuoto richiesto da JPA per la creazione e la persistenza delle entità.
     */
    public Recensione() {}

    /**
     * Costruttore per la creazione di una nuova recensione.
     * Inizializza automaticamente la data di creazione con il timestamp corrente.
     *
     * @param utente   l'utente che scrive la recensione
     * @param libro    il libro recensito
     * @param testo    il contenuto testuale della recensione (massimo 2000 caratteri)
     * @param pubblica flag che indica se la recensione è visibile pubblicamente
     */
    public Recensione(Utente utente, Libro libro, String testo, boolean pubblica) {
        this.utente = utente;
        this.libro = libro;
        this.testo = testo;
        this.dataCreazione = LocalDateTime.now();
        this.pubblica = pubblica;
    }

    public Long getId() {
        return id;
    }

    public Utente getUtente() {
        return utente;
    }

    public Libro getLibro() {
        return libro;
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

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setPubblica(boolean pubblica) {
        this.pubblica = pubblica;
    }
}