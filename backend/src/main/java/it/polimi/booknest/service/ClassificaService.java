package it.polimi.booknest.service;

import it.polimi.booknest.classifica.CriterioClassifica;
import it.polimi.booknest.exception.CriterioNonValidoException;
import it.polimi.booknest.model.Libro;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contesto del pattern Strategy: riceve il nome di un criterio, individua la strategia
 * corrispondente e delega ad essa il compito di ordinare i libri.
 *
 * <p><strong>Come ottiene le strategie:</strong> Spring inietta automaticamente tutte le
 * implementazioni dell'interfaccia {@link CriterioClassifica} all'interno di una lista. Il costruttore
 * si occupa di trasformare questa lista in una mappa (nome → strategia) per consentire una ricerca
 * rapida ed efficiente in tempo costante.
 *
 * <p><strong>Vantaggio del pattern:</strong> L'aggiunta di un nuovo criterio di ordinamento
 * richiede unicamente la scrittura di una nuova classe annotata con {@code @Component};
 * il codice di questo service rimane completamente invariato, garantendo la massima estensibilità
 * senza modifiche.
 */
@Service
public class ClassificaService {

    private final Map<String, CriterioClassifica> criteriPerNome = new HashMap<>();

    /**
     * Inizializza il service ricevendo la lista di tutte le strategie disponibili iniettate da Spring
     * e popolando la mappa interna di indicizzazione.
     *
     * @param criteri la lista di tutte le strategie concrete di classificazione individuate nel contesto
     */
    public ClassificaService(List<CriterioClassifica> criteri) {
        for (CriterioClassifica c : criteri) {
            criteriPerNome.put(c.getNome(), c);
        }
    }

    /**
     * Genera la classifica dei libri applicando la strategia corrispondente al nome specificato.
     *
     * <p>Nota: il service non conosce l'algoritmo o la logica di ordinamento sottostante,
     * ma si limita a delegare l'esecuzione all'oggetto strategia.
     *
     * @param nomeCriterio l'identificativo testuale del criterio richiesto (es. "piu-catalogati")
     * @return la lista dei libri ordinata secondo la strategia selezionata
     * @throws CriterioNonValidoException se il nome del criterio passato non corrisponde ad alcuna strategia registrata
     */
    public List<Libro> classifica(String nomeCriterio) {
        CriterioClassifica criterio = criteriPerNome.get(nomeCriterio);

        if (criterio == null) {
            throw new CriterioNonValidoException(nomeCriterio);
        }

        return criterio.ordina();
    }

    /**
     * Restituisce l'elenco di tutti i nomi identificativi dei criteri di classifica attualmente disponibili.
     *
     * <p>Questo metodo viene utilizzato dal client (frontend) per costruire dinamicamente il menu a tendina
     * delle opzioni di ordinamento, evitando di dover scrivere i nomi manualmente.
     *
     * @return una lista di stringhe contenente i nomi di tutte le strategie registrate
     */
    public List<String> nomiCriteriDisponibili() {
        return List.copyOf(criteriPerNome.keySet());
    }
}