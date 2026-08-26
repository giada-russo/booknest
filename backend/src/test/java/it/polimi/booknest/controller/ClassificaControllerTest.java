package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LibroDTO;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.service.ClassificaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificaControllerTest {

    @Mock
    private ClassificaService classificaService;

    @InjectMocks
    private ClassificaController classificaController;

    @Test
    void classificaConverteLeEntitaInDTOMantenendoLOrdine() {
        // Arrange
        Libro libro1 = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Libro libro2 = new Libro("1984", "George Orwell", "9788804668237");

        when(classificaService.classifica("piu-catalogati")).thenReturn(List.of(libro1, libro2));

        // Act
        List<LibroDTO> risultato = classificaController.classifica("piu-catalogati");

        // Assert
        assertEquals(2, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitolo());
        assertEquals("1984", risultato.get(1).getTitolo());
    }

    @Test
    void criteriDisponibiliRestituisceINomiDelService() {
        // Arrange
        when(classificaService.nomiCriteriDisponibili())
                .thenReturn(List.of("piu-catalogati", "piu-recensiti", "migliore-voto"));

        // Act
        List<String> risultato = classificaController.criteriDisponibili();

        // Assert
        assertEquals(3, risultato.size());
        assertEquals("piu-catalogati", risultato.get(0));
    }
}