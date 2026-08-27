package it.polimi.booknest.model;

import jakarta.persistence.*;

/**
 * Rappresenta l'apprezzamento espresso da un utente verso un libro.
 * <p>
 * Il vincolo di unicità sulla coppia utente-libro garantisce a livello di
 * database che uno stesso utente non possa mettere due like allo stesso libro,
 * indipendentemente dai controlli applicativi.
 */
@Entity
@Table(
        name = "like_libro",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_like_libro_utente_libro",
                columnNames = {"utente_id", "libro_id"}
        )
)
public class LikeLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public LikeLibro() {}

    /**
     * @param utente l'utente che esprime l'apprezzamento
     * @param libro  il libro apprezzato
     */
    public LikeLibro(Utente utente, Libro libro) {
        this.utente = utente;
        this.libro = libro;
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
}