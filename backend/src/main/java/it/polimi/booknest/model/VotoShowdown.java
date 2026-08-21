package it.polimi.booknest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Rappresenta il voto espresso da un utente in uno specifico Showdown.
 * <p>
 * Nota architetturale: la classe è deliberatamente priva di setter per garantire
 * l'immutabilità del dato storico (un voto espresso non muta il proprio stato).
 * Inoltre, il vincolo di unicità ({@code utente_id}, {@code showdown_id})
 * garantisce a livello di database l'impossibilità di voti doppi.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"utente_id", "showdown_id"}))
public class VotoShowdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Utente utente;
    @ManyToOne(optional = false)
    private Showdown showdown;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LibroScelto libroScelto;
    @Column(nullable = false)
    private LocalDateTime dataVoto;

    /**
     * Costruttore vuoto (no-args).
     * <p>
     * Richiesto da JPA per l'istanziazione dinamica tramite reflection.
     */
    public VotoShowdown() {
    }

    /**
     * Crea un nuovo voto.
     * <p>
     * Il voto nasce in uno stato valido: la data viene generata
     * automaticamente all'istante della creazione, sollevando il chiamante
     * dalla responsabilità di fornire un timestamp corretto.
     *
     * @param utente l'utente che esprime il voto
     * @param showdown il sondaggio a cui partecipa
     * @param libroScelto la scelta effettuata (A o B)
     */
    public VotoShowdown(Utente utente, Showdown showdown, LibroScelto libroScelto) {
        this.utente = utente;
        this.showdown = showdown;
        this.libroScelto = libroScelto;
        this.dataVoto = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Utente getUtente() {
        return utente;
    }

    public Showdown getShowdown() {
        return showdown;
    }

    public LibroScelto getLibroScelto() {
        return libroScelto;
    }

    public LocalDateTime getDataVoto() {
        return dataVoto;
    }
}
