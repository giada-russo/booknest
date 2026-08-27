package it.polimi.booknest.repository;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LikeLibro;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per la gestione della persistenza dei like ai libri.
 */
public interface LikeLibroRepository extends JpaRepository<LikeLibro, Long> {

    /**
     * Verifica se l'utente ha già espresso un apprezzamento per il libro.
     *
     * @param utente l'utente da verificare
     * @param libro  il libro da verificare
     * @return {@code true} se il like esiste già
     */
    boolean existsByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Rimuove il like dell'utente sul libro, se presente.
     *
     * @param utente l'utente che ritira l'apprezzamento
     * @param libro  il libro interessato
     */
    void deleteByUtenteAndLibro(Utente utente, Libro libro);

    /**
     * Conta quanti utenti hanno apprezzato il libro.
     *
     * @param libro il libro di cui contare i like
     * @return il numero di like ricevuti
     */
    long countByLibro(Libro libro);
}