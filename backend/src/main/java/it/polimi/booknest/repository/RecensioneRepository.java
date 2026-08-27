package it.polimi.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository JPA per la gestione e la persistenza delle entità {@link Recensione}.
 */
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /**
     * Trova tutte le recensioni pubbliche associate a uno specifico libro (utilizzato dai visitatori),
     * caricando esplicitamente anche il libro e l'utente associati tramite {@code JOIN FETCH}.
     *
     * @param libro il libro di cui cercare le recensioni
     * @return una lista di recensioni pubbliche con relazioni risolte
     */
    @Query("SELECT r FROM Recensione r JOIN FETCH r.libro JOIN FETCH r.utente WHERE r.libro = :libro AND r.pubblica = true")
    List<Recensione> findByLibroAndPubblicaTrue(@Param("libro") Libro libro);

    /**
     * Trova una specifica recensione scritta da un determinato utente per un determinato libro,
     * caricando esplicitamente anche il libro e l'utente associati tramite {@code JOIN FETCH}.
     *
     * @param utente l'utente autore della recensione
     * @param libro  il libro recensito
     * @return un {@link Optional} contenente la recensione se esiste
     */
    @Query("SELECT r FROM Recensione r JOIN FETCH r.libro JOIN FETCH r.utente WHERE r.utente = :utente AND r.libro = :libro")
    Optional<Recensione> findByUtenteAndLibro(@Param("utente") Utente utente, @Param("libro") Libro libro);

    /**
     * Verifica se esiste già una recensione scritta da un determinato utente per un determinato libro.
     *
     * @param utente l'utente da verificare
     * @param libro  il libro da verificare
     * @return true se la recensione esiste, false altrimenti
     */
    boolean existsByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Trova tutte le recensioni scritte da un determinato utente (le sue recensioni),
     * caricando esplicitamente anche il libro e l'utente associati tramite {@code JOIN FETCH}
     * per evitare problemi di lazy loading a sessione chiusa.
     *
     * @param utente l'utente di cui recuperare le recensioni
     * @return una lista delle recensioni dell'utente con relazioni risolte
     */
    @Query("SELECT r FROM Recensione r JOIN FETCH r.libro JOIN FETCH r.utente WHERE r.utente = :utente")
    List<Recensione> findByUtente(@Param("utente") Utente utente);

    /**
     * Recupera l'elenco dei libri ordinati in base al numero di recensioni ricevute, in ordine decrescente.
     * La query raggruppa le recensioni per libro, ne conta le occorrenze e ordina i risultati dal più recensito al meno recensito.
     *
     * @return una lista di oggetti {@link Libro} ordinata per popolarità basata sulle recensioni
     */
    @Query("SELECT r.libro FROM Recensione r GROUP BY r.libro ORDER BY COUNT(r) DESC")
    List<Libro> trovaLibriPiuRecensiti();
}