package it.polimi.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository JPA per la gestione e la persistenza delle entità {@link Recensione}.
 */
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /**
     * Trova tutte le recensioni pubbliche associate a uno specifico libro (utilizzato dai visitatori).
     *
     * @param libro il libro di cui cercare le recensioni
     * @return una lista di recensioni pubbliche
     */
    List<Recensione> findByLibroAndPubblicaTrue(Libro libro);

    /**
     * Trova una specifica recensione scritta da un determinato utente per un determinato libro.
     *
     * @param utente l'utente autore della recensione
     * @param libro  il libro recensito
     * @return un {@link Optional} contenente la recensione se esiste
     */
    Optional<Recensione> findByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Verifica se esiste già una recensione scritta da un determinato utente per un determinato libro.
     *
     * @param utente l'utente da verificare
     * @param libro  il libro da verificare
     * @return true se la recensione esiste, false altrimenti
     */
    boolean existsByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Trova tutte le recensioni scritte da un determinato utente (le sue recensioni).
     *
     * @param utente l'utente di cui recuperare le recensioni
     * @return una lista delle recensioni dell'utente
     */
    List<Recensione> findByUtente(Utente utente);

    /**
     * Recupera l'elenco dei libri ordinati in base al numero di recensioni ricevute, in ordine decrescente.
     * La query raggruppa le recensioni per libro, ne conta le occorrenze e ordina i risultati dal più recensito al meno recensito.
     *
     * @return una lista di oggetti {@link Libro} ordinata per popolarità basata sulle recensioni
     */
    @Query("SELECT r.libro FROM Recensione r GROUP BY r.libro ORDER BY COUNT(r) DESC")
    List<Libro> trovaLibriPiuRecensiti();
}