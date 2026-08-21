package it.polimi.booknest.repository;

import it.polimi.booknest.model.LibroScelto;
import it.polimi.booknest.model.Showdown;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.model.VotoShowdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per l'accesso e la gestione dei voti espressi nei sondaggi.
 */
@Repository
public interface VotoShowdownRepository extends JpaRepository<VotoShowdown, Long> {

    /**
     * Verifica se un determinato utente ha già votato in un determinato sondaggio.
     * <p>
     * Questo metodo accetta direttamente le entità (Utente e Showdown) anziché i loro ID.
     * Architetturalmente, questo avviene perché il livello Service ha già
     * risolto l'utente a partire dall'header della richiesta HTTP prima di
     * invocare questo controllo.
     *
     * @param utente l'utente che sta tentando di votare
     * @param showdown il sondaggio in questione
     * @return true se il record del voto esiste già (l'utente ha già votato), false altrimenti
     */
    boolean existsByUtenteAndShowdown(Utente utente, Showdown showdown);

    /**
     * Conta il numero totale di voti per una specifica opzione (A o B) in un sondaggio.
     * <p>
     * È fondamentale per ricostruire lo stato dell'applicazione al riavvio del server.
     * Poiché i contatori in tempo reale vivono nella memoria RAM per questioni
     * di performance, al riavvio verrebbero azzerati. Questo metodo permette
     * di interrogare la vera "sorgente di verità" (il database) e riallineare i contatori in memoria.
     *
     * @param showdown il sondaggio da conteggiare
     * @param libroScelto l'opzione votata (A o B)
     * @return il numero totale di voti per l'opzione indicata in quello showdown
     */
    long countByShowdownAndLibroScelto(Showdown showdown, LibroScelto libroScelto);
}
