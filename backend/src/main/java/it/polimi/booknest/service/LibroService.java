package it.polimi.booknest.service;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service che gestisce la logica applicativa relativa ai libri.
 * <p>
 * Questa classe funge da intermediario tra il controller e il repository,
 * occupandosi di recuperare i dati dei libri dal database e arricchendoli
 * con le metriche calcolate (come il voto medio).
 * </p>
 */
@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final CatalogazioneRepository catalogazioneRepository;

    public LibroService(LibroRepository libroRepository,
                        CatalogazioneRepository catalogazioneRepository) {
        this.libroRepository = libroRepository;
        this.catalogazioneRepository = catalogazioneRepository;
    }

    /**
     * Restituisce l'elenco di tutte le entità libro presenti nel catalogo.
     *
     * @return Lista di entità {@link Libro}.
     */
    public List<Libro> trovaTutti() {
        return libroRepository.findAll();
    }

    /**
     * Restituisce l'elenco di tutti i libri presenti nel catalogo, mappati in DTO
     * e arricchiti con il rispettivo voto medio calcolato dalle recensioni/catalogazioni.
     * <p>
     * Se un libro non ha ricevuto alcun voto, il campo {@code votoMedio} del DTO vale {@code null}.
     * </p>
     *
     * @return Lista di {@link LibroDTO} contenenti i dati del libro e il voto medio.
     */
    public List<LibroDTO> trovaTuttiConVotoMedio() {
        return libroRepository.findAll()
                .stream()
                .map(libro -> new LibroDTO(libro, catalogazioneRepository.calcolaVotoMedio(libro)))
                .toList();
    }
}