package it.polimi.booknest.repository;

import it.polimi.booknest.model.Showdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per l'accesso ai dati dell'entità Showdown.
 */
@Repository
public interface ShowdownRepository extends JpaRepository<Showdown, Long> {

    /**
     * Recupera tutti i sondaggi attualmente aperti.
     * <p>
     * Spring Data JPA deriva automaticamente la query SQL dal nome del metodo:
     * cercherà tutti i record in cui il campo 'attivo' è uguale a true.
     *
     * @return una lista di sondaggi attivi, pronti per essere inviati al frontend
     */
    List<Showdown> findByAttivoTrue();
}
