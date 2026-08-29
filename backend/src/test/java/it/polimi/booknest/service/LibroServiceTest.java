package it.polimi.booknest.service;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CatalogazioneRepository catalogazioneRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    void trovaTuttiRestituisceLibriDalRepository() {
        // Arrange
        Libro libro1 = new Libro("Cambiare l'acqua ai fiori", "Valerie Perrin", "9782226429537");
        Libro libro2 = new Libro("Tata", "Valerie Perrin", "9782226496430");
        when(libroRepository.findAll()).thenReturn(List.of(libro1, libro2));

        // Act
        List<Libro> risultato = libroService.trovaTutti();

        // Assert
        assertEquals(2, risultato.size());
        assertEquals("Cambiare l'acqua ai fiori", risultato.get(0).getTitolo());
    }

    @Test
    void trovaLibriSimiliDiLibroInesistenteSollevaEccezione() {
        // Arrange
        when(libroRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(LibroNonTrovatoException.class,
                () -> libroService.trovaLibriSimili(999L));
    }

    @Test
    void trovaTuttiConVotoMedioRestituisceILibriConLaMedia() {
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        when(libroRepository.findAll()).thenReturn(List.of(libro));
        when(catalogazioneRepository.calcolaVotoMedio(libro)).thenReturn(4.5);

        List<LibroDTO> risultato = libroService.trovaTuttiConVotoMedio();

        assertEquals(1, risultato.size());
        assertEquals(4.5, risultato.get(0).getVotoMedio());
    }

    @Test
    void trovaLibriSimiliRestituisceISuggerimenti() {
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Libro simile = new Libro("Tata", "Valerie Perrin", "9788833571039");
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.trovaLibriSimili(libro)).thenReturn(List.of(simile));

        List<LibroDTO> risultato = libroService.trovaLibriSimili(1L);

        assertEquals(1, risultato.size());
        assertEquals("Tata", risultato.get(0).getTitolo());
    }
}