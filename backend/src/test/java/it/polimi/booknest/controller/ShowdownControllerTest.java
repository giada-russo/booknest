package it.polimi.booknest.controller;

import it.polimi.booknest.dto.RisultatoShowdownDTO;
import it.polimi.booknest.dto.ShowdownDTO;
import it.polimi.booknest.dto.VotoRequest;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LibroScelto;
import it.polimi.booknest.model.Showdown;
import it.polimi.booknest.service.ShowdownService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowdownControllerTest {

    @Mock
    private ShowdownService showdownService;

    @InjectMocks
    private ShowdownController showdownController;

    @Test
    void getAttiviConverteLeEntitaInDTO() {
        // Arrange
        Libro libroA = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Libro libroB = new Libro("1984", "George Orwell", "9788804668237");
        Showdown showdown = new Showdown(libroA, libroB);

        when(showdownService.trovaAttivi()).thenReturn(List.of(showdown));

        // Act
        List<ShowdownDTO> risultato = showdownController.getAttivi();

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitoloLibroA());
        assertEquals("1984", risultato.get(0).getTitoloLibroB());
    }

    @Test
    void getRisultatiRestituisceIlRisultatoDelService() {
        // Arrange
        RisultatoShowdownDTO risultatoAtteso = new RisultatoShowdownDTO(1L, 7, 3);
        when(showdownService.getRisultati(1L)).thenReturn(risultatoAtteso);

        // Act
        RisultatoShowdownDTO risultato = showdownController.getRisultati(1L);

        // Assert
        assertEquals(7, risultato.getConteggioA());
        assertEquals(3, risultato.getConteggioB());
    }

    @Test
    void votaRegistraIlVotoERestituisceIRisultatiAggiornati() {
        // Arrange
        VotoRequest richiesta = new VotoRequest();
        richiesta.setLibroScelto(LibroScelto.A);

        RisultatoShowdownDTO risultatoAtteso = new RisultatoShowdownDTO(1L, 8, 3);
        when(showdownService.getRisultati(1L)).thenReturn(risultatoAtteso);

        // Act
        RisultatoShowdownDTO risultato = showdownController.vota(1L, 1L, richiesta);

        // Assert
        verify(showdownService).vota(1L, 1L, LibroScelto.A);
        assertEquals(8, risultato.getConteggioA());
        assertEquals(3, risultato.getConteggioB());
    }
}