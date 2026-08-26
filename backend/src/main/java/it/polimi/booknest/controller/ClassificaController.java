package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.exception.CriterioNonValidoException;
import it.polimi.booknest.service.ClassificaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST che funge da adattatore per la gestione delle classifiche dei libri.
 * <p>
 * Espone endpoint pubblici accessibili ai visitatori per consultare i libri ordinati
 * secondo diverse strategie e per ottenere l'elenco dei criteri di ordinamento disponibili.
 */
@RestController
@RequestMapping("/api/classifica")
public class ClassificaController {

    private final ClassificaService classificaService;

    public ClassificaController(ClassificaService classificaService) {
        this.classificaService = classificaService;
    }

    /**
     * Restituisce la classifica dei libri ordinata in base al criterio specificato,
     * convertendo le entità in oggetti {@link LibroDTO}.
     * <p>
     * Risponde alle richieste HTTP GET su {@code /api/classifica/libri/{criterio}}.
     *
     * @param criterio l'identificativo testuale della strategia di ordinamento richiesta (es. "piu-catalogati")
     * @return la lista dei libri ordinata secondo il criterio selezionato
     * @throws CriterioNonValidoException se il criterio specificato non esiste o non è valido
     */
    @GetMapping("/libri/{criterio}")
    public List<LibroDTO> classifica(@PathVariable String criterio) {
        return classificaService.classifica(criterio)
                .stream()
                .map(LibroDTO::new)
                .toList();
    }

    /**
     * Restituisce l'elenco di tutti i nomi identificativi dei criteri di classifica disponibili nel sistema.
     * <p>
     * Risponde alle richieste HTTP GET su {@code /api/classifica/criteri}, consentendo al client
     * di popolare dinamicamente i menu di selezione.
     *
     * @return la lista di stringhe con i nomi di tutte le strategie di ordinamento configurate
     */
    @GetMapping("/criteri")
    public List<String> criteriDisponibili() {
        return classificaService.nomiCriteriDisponibili();
    }
}