package it.polimi.booknest.service;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.exception.LibroNonTrovatoException;
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

    /**
     * Restituisce fino a cinque libri simili a quello indicato, individuati in base
     * alle catalogazioni degli altri utenti (filtraggio collaborativo).
     * <p>
     * La lista è vuota se nessun altro utente ha catalogato il libro insieme ad altri.
     *
     * @param idLibro l'identificativo del libro di riferimento
     * @return la lista dei libri simili, al massimo cinque
     * @throws LibroNonTrovatoException se il libro non esiste
     */
    public List<LibroDTO> trovaLibriSimili(Long idLibro) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new LibroNonTrovatoException(idLibro));

        return catalogazioneRepository.trovaLibriSimili(libro)
                .stream()
                .limit(5)
                .map(LibroDTO::new)
                .toList();
    }
}