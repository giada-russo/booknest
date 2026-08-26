package it.polimi.booknest.classifica;

import it.polimi.booknest.model.Libro;
import java.util.List;

/**
 * Rappresenta il contratto per le strategie di ordinamento della classifica nell'ambito del pattern Strategy.
 *
 * <p>I criteri di ordinamento sono intercambiabili e scelti dinamicamente dall'utente:
 * per questo motivo, ciascuna modalità di ordinamento è incapsulata in una classe separata
 * anziché essere gestita tramite rami condizionali (if-else o switch).
 */
public interface CriterioClassifica {

    /**
     * Restituisce la lista dei libri ordinata secondo la specifica strategia del criterio.
     * Ciascuna implementazione concreta applicherà la propria logica di recupero e ordinamento (es. tramite query).
     *
     * @return una lista di oggetti {@link Libro} ordinata in base al criterio corrente
     */
    List<Libro> ordina();

    /**
     * Restituisce l'identificativo testuale univoco del criterio, utilizzabile all'interno di URL
     * (privo di spazi e caratteri accentati, es. "piu-catalogati").
     *
     * <p>Questo identificativo viene impiegato dal service per la costruzione della mappa di routing
     * e dal client per riconoscere i criteri disponibili.
     *
     * @return una stringa che rappresenta il nome identificativo del criterio
     */
    String getNome();
}