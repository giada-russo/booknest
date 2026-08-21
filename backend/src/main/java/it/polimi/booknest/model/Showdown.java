package it.polimi.booknest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Rappresenta un sondaggio comparativo tra due libri.
 * <p>
 * Implementa due campi {@code @ManyToOne} distinti ({@code libroA} e {@code libroB})
 * invece di una collezione. Il motivo architetturale è che uno Showdown ha esattamente
 * due libri e i ruoli non sono intercambiabili (il voto dell'utente esprime esplicitamente A o B).
 * Se si utilizzasse una collezione, si permetterebbe l'inserimento di zero, tre o più libri,
 * perdendo la capacità di esprimere questo vincolo in modo forte tramite il tipo di dato.
 */
@Entity
public class Showdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Libro libroA;
    @ManyToOne(optional = false)
    private Libro libroB;
    private LocalDateTime dataCreazione;
    private boolean attivo;

    /**
     * Costruttore vuoto (no-args).
     * <p>
     * È un requisito formale delle specifiche JPA. Framework come Hibernate
     * lo utilizzano per istanziare dinamicamente l'oggetto tramite reflection
     * in fase di estrazione dal database, per poi popolarne i campi.
     */
    public Showdown(){}

    /**
     * Inizializza un nuovo sondaggio impostando i due libri in competizione.
     * <p>
     * Il sondaggio nasce in uno stato valido: viene impostato automaticamente
     * come attivo ({@code attivo = true}) e la data di creazione viene registrata
     * all'istante corrente. Questo garantisce che l'oggetto sia subito pronto
     * per essere salvato e per ricevere i voti.
     *
     * @param libroA il primo libro che partecipa al sondaggio
     * @param libroB il secondo libro che partecipa al sondaggio
     */
    public Showdown(Libro libroA, Libro libroB) {
        this.libroA = libroA;
        this.libroB = libroB;
        this.dataCreazione = LocalDateTime.now();
        this.attivo = true;
    }

    public Long getId() {
        return id;
    }

    public Libro getLibroA() {
        return libroA;
    }

    public Libro getLibroB() {
        return libroB;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }
}
