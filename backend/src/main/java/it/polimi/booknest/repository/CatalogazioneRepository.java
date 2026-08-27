package it.polimi.booknest.repository;

import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.StatoLettura;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository per la gestione della persistenza delle entità {@link Catalogazione}.
 * Fornisce i metodi CRUD ereditati da {@link JpaRepository} e le query di ricerca personalizzate
 * per interrogare le librerie personali degli utenti.
 */
public interface CatalogazioneRepository extends JpaRepository<Catalogazione, Long> {

    /**
     * Recupera l'elenco delle catalogazioni di un utente, caricando insieme
     * il libro associato.
     * <p>
     * Il {@code JOIN FETCH} è necessario perché la relazione con {@link Libro}
     * è LAZY e la sessione Hibernate si chiude al termine del service
     * ({@code open-in-view=false}): senza, la conversione in DTO fallirebbe.
     *
     * @param utente l'utente proprietario della libreria
     * @return le catalogazioni dell'utente, con il libro già caricato
     */
    @Query("SELECT c FROM Catalogazione c JOIN FETCH c.libro WHERE c.utente = :utente")
    List<Catalogazione> findByUtente(@Param("utente") Utente utente);

    /**
     * Cerca una specifica catalogazione associando un utente a un libro,
     * caricando esplicitamente anche il libro associato tramite {@code JOIN FETCH}.
     * Dato il vincolo di unicità sulla coppia utente-libro, restituisce al massimo un risultato.
     *
     * @param utente L'utente che ha effettuato la catalogazione.
     * @param libro Il libro cercato.
     * @return Un {@link Optional} contenente la catalogazione se esiste, altrimenti vuoto.
     */
    @Query("SELECT c FROM Catalogazione c JOIN FETCH c.libro WHERE c.utente = :utente AND c.libro = :libro")
    Optional<Catalogazione> findByUtenteAndLibro(@Param("utente") Utente utente, @Param("libro") Libro libro);

    /**
     * Verifica l'esistenza di una catalogazione per la coppia utente-libro data,
     * utile per prevenire inserimenti duplicati.
     *
     * @param utente L'utente da verificare.
     * @param libro Il libro da verificare.
     * @return {@code true} se la catalogazione esiste già, {@code false} altrimenti.
     */
    boolean existsByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Trova tutte le catalogazioni di un determinato utente associate a uno specifico stato di lettura,
     * ordinate cronologicamente dalla più recente alla meno recente in base alla data di completamento,
     * caricando esplicitamente anche il libro associato tramite {@code JOIN FETCH}.
     * Utilizzato per alimentare il diario di lettura.
     *
     * @param utente l'utente di cui recuperare la cronologia
     * @param stato  lo stato di lettura da filtrare (es. {@link StatoLettura#LETTO})
     * @return una lista di catalogazioni ordinate per data di completamento discendente
     */
    @Query("""
        SELECT c FROM Catalogazione c JOIN FETCH c.libro
        WHERE c.utente = :utente AND c.stato = :stato
        ORDER BY c.dataCompletamento DESC
        """)
    List<Catalogazione> findByUtenteAndStatoOrderByDataCompletamentoDesc(
            @Param("utente") Utente utente, @Param("stato") StatoLettura stato);

    /**
     * Restituisce l'elenco dei libri ordinati in base alla loro popolarità (numero di catalogazioni),
     * dal più catalogato al meno catalogato.
     * <p>
     * Sfrutta una query JPQL (Java Persistence Query Language) per raggruppare e contare le occorrenze.
     * Questa classifica viene utilizzata per la generazione automatica delle sfide nei duelli Showdown.
     * </p>
     *
     * @return una lista di {@link Libro} ordinata per numero di catalogazioni decrescente
     */
    @Query("SELECT c.libro FROM Catalogazione c GROUP BY c.libro ORDER BY COUNT(c) DESC")
    List<Libro> trovaLibriPiuCatalogati();

    /**
     * Recupera l'elenco dei libri ordinati in base alla media dei voti ricevuti nelle catalogazioni, in ordine decrescente.
     * Vengono prese in considerazione solo le catalogazioni che presentano un voto valido (diverso da null),
     * raggruppando i risultati per libro e calcolando la media aritmetica dei voti.
     *
     * @return una lista di oggetti {@link Libro} ordinata per media voti decrescente
     */
    @Query("SELECT c.libro FROM Catalogazione c WHERE c.voto IS NOT NULL GROUP BY c.libro ORDER BY AVG(c.voto) DESC")
    List<Libro> trovaLibriMiglioreVoto();

    /**
     * Calcola la media dei voti assegnati a un libro dagli utenti che lo hanno catalogato.
     * Le catalogazioni prive di voto vengono escluse dal calcolo.
     *
     * @param libro il libro di cui calcolare la media
     * @return la media dei voti, oppure {@code null} se nessun utente ha ancora votato il libro
     */
    @Query("SELECT AVG(c.voto) FROM Catalogazione c WHERE c.libro = :libro AND c.voto IS NOT NULL")
    Double calcolaVotoMedio(@Param("libro") Libro libro);

    /**
     * Trova i libri correlati a un dato libro in base alle preferenze degli utenti.
     * <p>
     * La query individua tutti gli utenti che hanno catalogato il libro di partenza
     * e restituisce gli altri libri presenti nelle loro catalogazioni, ordinati per
     * frequenza decrescente di comparizione.
     * </p>
     *
     * @param libro il {@link Libro} di riferimento per cui cercare affinità
     * @return una lista di {@link Libro} affini, ordinata per popolarità tra gli stessi lettori
     */
    @Query("""
        SELECT c2.libro FROM Catalogazione c2
        WHERE c2.libro <> :libro
          AND c2.utente IN (SELECT c1.utente FROM Catalogazione c1 WHERE c1.libro = :libro)
        GROUP BY c2.libro
        ORDER BY COUNT(c2) DESC
        """)
    List<Libro> trovaLibriSimili(@Param("libro") Libro libro);
}