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
    void trovaTuttiConverteLeEntitaInDTO() {
        // Arrange
        Libro libro1 = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Libro libro2 = new Libro("1984", "George Orwell", "9788804668237");

        when(libroService.trovaTutti()).thenReturn(List.of(libro1, libro2));

        // Act
        List<LibroDTO> risultato = libroController.trovaTutti();

        // Assert
        assertEquals(2, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitolo());
        assertEquals("Umberto Eco", risultato.get(0).getAutore());
    }
}
