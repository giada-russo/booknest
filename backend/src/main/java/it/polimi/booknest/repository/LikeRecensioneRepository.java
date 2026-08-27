package it.polimi.booknest.repository;

import it.polimi.booknest.model.LikeRecensione;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per la gestione della persistenza dei like alle recensioni.
 */
public interface LikeRecensioneRepository extends JpaRepository<LikeRecensione, Long> {

    /**
     * Verifica se l'utente ha già espresso un apprezzamento per la recensione.
     *
     * @param utente     l'utente da verificare
     * @param recensione la recensione da verificare
     * @return {@code true} se il like esiste già
     */
    boolean existsByUtenteAndRecensione(Utente utente, Recensione recensione);

    /**
     * Rimuove il like dell'utente sulla recensione, se presente.
     *
     * @param utente     l'utente che ritira l'apprezzamento
     * @param recensione la recensione interessata
     */
    void deleteByUtenteAndRecensione(Utente utente, Recensione recensione);

    /**
     * Conta quanti utenti hanno apprezzato la recensione.
     *
     * @param recensione la recensione di cui contare i like
     * @return il numero di like ricevuti
     */
    long countByRecensione(Recensione recensione);
}