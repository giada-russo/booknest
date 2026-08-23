package it.polimi.booknest.model;

import it.polimi.booknest.exception.TransizioneNonValidaException;
import it.polimi.booknest.exception.VotoNonConsentitoException;
import it.polimi.booknest.model.stato.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entità che modella la catalogazione di un libro all'interno della libreria personale di un utente.
 * <p>
 * Nel contesto del <b>Design Pattern State</b>, questa classe funge da <i>Context</i>:
 * mantiene il riferimento allo stato attuale dell'opera e delega a esso le decisioni
 * relative alle regole di business (possibilità di voto, recensione e validità delle transizioni).
 * </p>
 * <p>
 * La combinazione di {@code utente} e {@code libro} è soggetta a un vincolo di unicità
 * a livello di tabella. Entità resa parzialmente immutabile: utente e libro non possono
 * essere modificati dopo la creazione.
 * </p>
 */
@Entity
@Table(
        name = "catalogazione",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_catalogazione_utente_libro",
                        columnNames = {"utente_id", "libro_id"}
                )
        }
)
public class Catalogazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private StatoLettura stato;

    @Column(name = "voto")
    private Integer voto;

    @Column(name = "data_completamento")
    private LocalDateTime dataCompletamento;

    public Catalogazione() {
    }

    public Catalogazione(Utente utente, Libro libro) {
        this.utente = utente;
        this.libro = libro;
        this.stato = StatoLettura.DA_LEGGERE;
    }

    /**
     * Restituisce l'istanza concreta di {@link StatoCatalogazione} corrispondente allo stato attuale.
     * <p>
     * Questo metodo funge da ponte (factory/adapter) tra il valore persistito su database
     * (l'enum {@link StatoLettura}) e l'oggetto di stato che incapsula le regole del pattern State.
     * Il pattern elimina i condizionali sulla logica di business, centralizzando qui
     * la pura conversione tecnica.
     * </p>
     *
     * @return L'oggetto {@link StatoCatalogazione} incaricato di gestire il comportamento corrente.
     */
    public StatoCatalogazione getStatoCorrente() {
        return switch (this.stato) {
            case DA_LEGGERE -> new StatoDaLeggere();
            case IN_LETTURA -> new StatoInLettura();
            case LETTO -> new StatoLetto();
            case ABBANDONATO -> new StatoAbbandonato();
        };
    }

    /**
     * Tenta di far evolvere il libro verso un nuovo stato di lettura.
     * L'operazione è delegata al pattern State che ne verifica la liceità. Se la transizione
     * è valida e porta a "LETTO", il metodo si occupa anche di storicizzare l'evento
     * popolando la data di completamento.
     *
     * @param nuovoStato Lo stato desiderato.
     * @throws TransizioneNonValidaException se le regole di dominio vietano il passaggio richiesto.
     */
    public void cambiaStato(StatoLettura nuovoStato) {
        if (!getStatoCorrente().puoPassareA(nuovoStato)) {
            throw new TransizioneNonValidaException(this.stato, nuovoStato);
        }

        this.stato = nuovoStato;

        if (nuovoStato == StatoLettura.LETTO) {
            this.dataCompletamento = LocalDateTime.now();
        }
    }

    /**
     * Assegna un voto al libro, validandone sia il permesso tramite il pattern State,
     * sia l'intervallo di validità (scala 1-5).
     * <p>
     * L'uso del tipo primitivo {@code int} come parametro impedisce logicamente
     * di scavalcare i controlli passando {@code null}.
     * </p>
     *
     * @param voto Il voto da assegnare (da 1 a 5).
     * @throws VotoNonConsentitoException se lo stato attuale del libro non permette l'inserimento.
     * @throws IllegalArgumentException se il voto non è compreso tra 1 e 5.
     */
    public void assegnaVoto(int voto) {
        if (!getStatoCorrente().permetteVoto()) {
            throw new VotoNonConsentitoException(this.stato);
        }

        if (voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5. Ricevuto: " + voto);
        }

        this.voto = voto;
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

    public StatoLettura getStato() {
        return stato;
    }

    public Integer getVoto() {
        return voto;
    }

    public LocalDateTime getDataCompletamento() {
        return dataCompletamento;
    }
}