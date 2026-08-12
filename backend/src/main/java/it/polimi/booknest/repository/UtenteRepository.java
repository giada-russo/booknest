package it.polimi.booknest.repository;

import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Fornisce l'accesso ai dati degli utenti registrati.
 * <p>
 * Oltre alle operazioni CRUD ereditate da {@link JpaRepository}, dichiara tre
 * interrogazioni derivate dal nome del metodo, utilizzate rispettivamente
 * dall'autenticazione e dal controllo di unicità in fase di registrazione.
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

     Optional<Utente> findByUsername(String username);
     boolean existsByUsername(String username);
     boolean existsByEmail(String email);
}
