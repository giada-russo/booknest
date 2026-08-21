package it.polimi.booknest.controller;

import it.polimi.booknest.dto.RisultatoShowdownDTO;
import it.polimi.booknest.dto.ShowdownDTO;
import it.polimi.booknest.dto.VotoRequest;
import it.polimi.booknest.exception.ShowdownNonAttivoException;
import it.polimi.booknest.exception.ShowdownNonTrovatoException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.exception.VotoGiaEspressoException;
import it.polimi.booknest.service.ShowdownService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showdown")
public class ShowdownController {

    private final ShowdownService showdownService;

    public ShowdownController(ShowdownService showdownService) {
        this.showdownService = showdownService;
    }

    /**
     * Recupera la lista di tutti gli showdown attualmente attivi.
     *
     * Nota: questo è un endpoint pubblico.
     *
     * @return una lista di {@link ShowdownDTO} che rappresentano gli showdown attivi
     */
    @GetMapping("/attivi")
    public List<ShowdownDTO> getAttivi() {
        return showdownService.trovaAttivi().stream()
                .map(ShowdownDTO::new)
                .toList();
    }

    /**
     * Recupera i risultati attuali di uno specifico showdown.
     *
     * Nota: questo è un endpoint pubblico.
     *
     * @param id l'identificativo univoco dello showdown
     * @return un {@link RisultatoShowdownDTO} contenente le statistiche e i risultati dello showdown
     */
    @GetMapping("/{id}/risultati")
    public RisultatoShowdownDTO getRisultati(@PathVariable Long id) {
        return showdownService.getRisultati(id);
    }

    /**
     * Permette a un utente di esprimere il proprio voto per uno specifico showdown.
     *
     * @param utenteId l'identificativo dell'utente che sta votando (estratto dall'header)
     * @param id l'identificativo univoco dello showdown
     * @param request il corpo della richiesta contenente l'opzione votata
     * @return un {@link RisultatoShowdownDTO} con i risultati aggiornati dopo il voto
     * @throws UtenteNonTrovatoException se l'ID utente fornito nell'header non corrisponde a nessun utente registrato
     * @throws ShowdownNonTrovatoException se l'ID fornito non corrisponde a nessuno showdown esistente
     * @throws ShowdownNonAttivoException se lo showdown richiesto è chiuso
     * @throws VotoGiaEspressoException se l'utente ha già precedentemente votato per questo showdown
     */
    @PostMapping("/{id}/voto")
    public RisultatoShowdownDTO vota(@RequestHeader("X-Utente-Id") Long utenteId,
                                     @PathVariable Long id,
                                     @RequestBody VotoRequest request) {
        showdownService.vota(utenteId, id, request.getLibroScelto());
        return showdownService.getRisultati(id);
    }
}