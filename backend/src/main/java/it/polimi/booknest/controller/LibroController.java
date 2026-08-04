package it.polimi.booknest.controller;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.service.LibroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Espone gli endpoint REST relativi ai libri del catalogo.
 * <p>
 * Riceve le richieste HTTP provenienti dal client, delega l'elaborazione
 * a {@link LibroService} e restituisce i risultati in formato JSON.
 */
@RestController
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * Restituisce tutti i libri presenti nel sistema.
     * <p>
     * Risponde alle richieste HTTP GET su {@code /api/libri}.
     *
     * @return la lista completa dei {@link Libro} disponibili
     */
    @GetMapping("/api/libri")
    public List<Libro> trovaTutti() {
        return libroService.trovaTutti();
    }
}
