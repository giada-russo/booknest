package it.polimi.booknest.service;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service che gestisce la logica applicativa relativa ai libri.
 * <p>
 * Questa classe funge da intermediario tra il controller e il repository,
 * occupandosi di recuperare i dati dei libri dal database.
 * </p>
 */
@Service
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {

        this.libroRepository = libroRepository;
    }

    public List<Libro> trovaTutti() {

        return libroRepository.findAll();
    }


}
