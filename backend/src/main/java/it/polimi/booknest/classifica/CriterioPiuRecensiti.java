package it.polimi.booknest.classifica;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.RecensioneRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementazione concreta del pattern Strategy per il criterio di classifica basato sui libri più recensiti.
 *
 * <p>Questa strategia si occupa di ordinare i libri in base al numero di recensioni ricevute in ordine decrescente,
 * sfruttando il repository dedicato per l'interrogazione dei dati.
 */
@Component
public class CriterioPiuRecensiti implements CriterioClassifica {

    private final RecensioneRepository recensioneRepository;

    public CriterioPiuRecensiti(RecensioneRepository recensioneRepository) {
        this.recensioneRepository = recensioneRepository;
    }

    /**
     * {@inheritDoc}
     * <p>Esegue la query sul repository per recuperare la lista dei libri ordinata per numero di recensioni decrescente.
     */
    @Override
    public List<Libro> ordina() {
        return recensioneRepository.trovaLibriPiuRecensiti();
    }

    /**
     * {@inheritDoc}
     *
     * @return la stringa "piu-recensiti"
     */
    @Override
    public String getNome() {
        return "piu-recensiti";
    }
}