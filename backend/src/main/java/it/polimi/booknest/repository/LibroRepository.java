package it.polimi.booknest.repository;

import it.polimi.booknest.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Fornisce l'accesso ai dati dei libri persistiti nel database.
 * <p>
 * Estende {@link JpaRepository} per ereditare le operazioni CRUD di base;
 * l'implementazione è generata automaticamente da Spring Data all'avvio
 * dell'applicazione.
 */
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
