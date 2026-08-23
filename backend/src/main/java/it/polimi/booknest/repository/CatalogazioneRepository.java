package it.polimi.booknest.repository;

import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.StatoLettura;
import it.polimi.booknest.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per la gestione della persistenza delle entità {@link Catalogazione}.
 * Fornisce i metodi CRUD ereditati da {@link JpaRepository} e le query di ricerca personalizzate
 * per interrogare le librerie personali degli utenti.
 */
public interface CatalogazioneRepository extends JpaRepository<Catalogazione, Long> {

    /**
     * Recupera l'elenco di tutte le catalogazioni associate a un determinato utente.
     *
     * @param utente L'utente proprietario della libreria.
     * @return Una lista contenente le catalogazioni trovate (potrebbe essere vuota).
     */
    List<Catalogazione> findByUtente(Utente utente);

    /**
     * Cerca una specifica catalogazione associando un utente a un libro.
     * Dato il vincolo di unicità sulla coppia utente-libro, restituisce al massimo un risultato.
     *
     * @param utente L'utente che ha effettuato la catalogazione.
     * @param libro Il libro cercato.
     * @return Un {@link Optional} contenente la catalogazione se esiste, altrimenti vuoto.
     */
    Optional<Catalogazione> findByUtenteAndLibro(Utente utente, Libro libro);

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
     * ordinate cronologicamente dalla più recente alla meno recente in base alla data di completamento.
     * Utilizzato per alimentare il diario di lettura.
     *
     * @param utente l'utente di cui recuperare la cronologia
     * @param stato  lo stato di lettura da filtrare (es. {@link StatoLettura#LETTO})
     * @return una lista di catalogazioni ordinate per data di completamento discendente
     */
    List<Catalogazione> findByUtenteAndStatoOrderByDataCompletamentoDesc(Utente utente, StatoLettura stato);

}