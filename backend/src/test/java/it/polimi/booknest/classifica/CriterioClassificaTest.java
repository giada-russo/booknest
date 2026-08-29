package it.polimi.booknest.classifica;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.RecensioneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriteriClassificaTest {

    @Mock
    private CatalogazioneRepository catalogazioneRepository;

    @Mock
    private RecensioneRepository recensioneRepository;

    @Test
    void criterioPiuCatalogatiOrdinaPerNumeroDiCatalogazioni() {
        // Arrange
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        when(catalogazioneRepository.trovaLibriPiuCatalogati()).thenReturn(List.of(libro));
        CriterioPiuCatalogati criterio = new CriterioPiuCatalogati(catalogazioneRepository);

        // Act
        List<Libro> risultato = criterio.ordina();

        // Assert
        assertEquals("piu-catalogati", criterio.getNome());
        assertEquals(1, risultato.size());
        assertEquals("Circe", risultato.get(0).getTitolo());
        verify(catalogazioneRepository).trovaLibriPiuCatalogati();
    }

    @Test
    void criterioMiglioreVotoOrdinaPerMediaDeiVoti() {
        // Arrange
        Libro libro = new Libro("Tata", "Valerie Perrin", "9788833571039");
        when(catalogazioneRepository.trovaLibriMiglioreVoto()).thenReturn(List.of(libro));
        CriterioMiglioreVoto criterio = new CriterioMiglioreVoto(catalogazioneRepository);

        // Act
        List<Libro> risultato = criterio.ordina();

        // Assert
        assertEquals("migliore-voto", criterio.getNome());
        assertEquals(1, risultato.size());
        verify(catalogazioneRepository).trovaLibriMiglioreVoto();
    }

    @Test
    void criterioPiuRecensitiOrdinaPerNumeroDiRecensioni() {
        // Arrange
        Libro libro = new Libro("Il suggeritore", "Donato Carrisi", "9788850218318");
        when(recensioneRepository.trovaLibriPiuRecensiti()).thenReturn(List.of(libro));
        CriterioPiuRecensiti criterio = new CriterioPiuRecensiti(recensioneRepository);

        // Act
        List<Libro> risultato = criterio.ordina();

        // Assert
        assertEquals("piu-recensiti", criterio.getNome());
        assertEquals(1, risultato.size());
        verify(recensioneRepository).trovaLibriPiuRecensiti();
    }
}
