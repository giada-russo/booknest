package it.polimi.booknest.model;

import jakarta.persistence.*;

/**
 * Rappresenta l'apprezzamento espresso da un utente verso una recensione.
 * <p>
 * Il vincolo di unicità sulla coppia utente-recensione garantisce a livello di
 * database che uno stesso utente non possa mettere due like alla stessa recensione,
 * indipendentemente dai controlli applicativi.
 */
@Entity
@Table(
        name = "like_recensione",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_like_recensione_utente_recensione",
                columnNames = {"utente_id", "recensione_id"}
        )
)
public class LikeRecensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recensione_id", nullable = false)
    private Recensione recensione;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public LikeRecensione() {}

    /**
     * @param utente     l'utente che esprime l'apprezzamento
     * @param recensione la recensione apprezzata
     */
    public LikeRecensione(Utente utente, Recensione recensione) {
        this.utente = utente;
        this.recensione = recensione;
    }

    public Long getId() {
        return id;
    }

    public Utente getUtente() {
        return utente;
    }

    public Recensione getRecensione() {
        return recensione;
    }
}