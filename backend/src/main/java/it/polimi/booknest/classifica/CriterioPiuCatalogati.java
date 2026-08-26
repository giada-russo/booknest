package it.polimi.booknest.classifica;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.CatalogazioneRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementazione concreta del pattern Strategy per il criterio di classifica basato sui libri più catalogati.
 *
 * <p>Questa strategia si occupa di ordinare i libri in base al numero di catalogazioni in ordine decrescente,
 * sfruttando il repository dedicato per l'interrogazione dei dati.
 */
@Component
public class CriterioPiuCatalogati implements CriterioClassifica {

    private final CatalogazioneRepository catalogazioneRepository;

    public CriterioPiuCatalogati(CatalogazioneRepository catalogazioneRepository) {
        this.catalogazioneRepository = catalogazioneRepository;
    }

    @Override
    public List<Libro> ordina() {
        return catalogazioneRepository.trovaLibriPiuCatalogati();
    }

    @Override
    public String getNome() {
        return "piu-catalogati";
    }
}