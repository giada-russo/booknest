package it.polimi.booknest.classifica;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.CatalogazioneRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementazione concreta del pattern Strategy per il criterio di classifica basato sulla media dei voti.
 *
 * <p>Questa strategia si occupa di ordinare i libri in base alla media dei voti ricevuti nelle catalogazioni
 * in ordine decrescente, sfruttando il repository dedicato per l'interrogazione dei dati.
 */
@Component
public class CriterioMiglioreVoto implements CriterioClassifica {

    private final CatalogazioneRepository catalogazioneRepository;

    public CriterioMiglioreVoto(CatalogazioneRepository catalogazioneRepository) {
        this.catalogazioneRepository = catalogazioneRepository;
    }

    /**
     * {@inheritDoc}
     * <p>Esegue la query sul repository per recuperare la lista dei libri ordinata per media voti decrescente.
     */
    @Override
    public List<Libro> ordina() {
        return catalogazioneRepository.trovaLibriMiglioreVoto();
    }

    /**
     * {@inheritDoc}
     *
     * @return la stringa "migliore-voto"
     */
    @Override
    public String getNome() {
        return "migliore-voto";
    }
}