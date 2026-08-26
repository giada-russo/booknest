package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.service.LibroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {

    @Mock
    private LibroService libroService;

    @InjectMocks
    private LibroController libroController;

    @Test
    void trovaTuttiRestituisceILibriConVotoMedio() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        LibroDTO dto = new LibroDTO(libro, 4.5);

        when(libroService.trovaTuttiConVotoMedio()).thenReturn(List.of(dto));

        // Act
        List<LibroDTO> risultato = libroController.trovaTutti();

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitolo());
        assertEquals(4.5, risultato.get(0).getVotoMedio());
    }
}