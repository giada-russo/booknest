package it.polimi.booknest.repository;

import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Fornisce l'accesso ai dati degli utenti registrati.
 * <p>
 * Oltre alle operazioni CRUD ereditate da {@link JpaRepository}, dichiara tre
 * interrogazioni derivate dal nome del metodo, utilizzate rispettivamente
 * dall'autenticazione e dal controllo di unicità in fase di registrazione.
 */

public interface UtenteRepository extends JpaRepository<Utente, Long> {

     Optional<Utente> findByUsername(String username);
     boolean existsByUsername(String username);
     boolean existsByEmail(String email);

     /**
      * Recupera un utente caricando insieme l'insieme degli utenti che segue.
      * <p>
      * Il {@code LEFT JOIN FETCH} è necessario perché la relazione {@code seguiti}
      * è LAZY e la sessione Hibernate si chiude al termine del service. Il
      * {@code LEFT} garantisce che l'utente venga trovato anche quando non segue
      * nessuno.
      *
      * @param id l'identificativo dell'utente
      * @return un {@link Optional} contenente l'utente con i seguiti già caricati
      */
     @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.seguiti WHERE u.id = :id")
     Optional<Utente> trovaConSeguiti(@Param("id") Long id);
}