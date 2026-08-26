package it.polimi.booknest.service;

import it.polimi.booknest.classifica.CriterioClassifica;
import it.polimi.booknest.exception.CriterioNonValidoException;
import it.polimi.booknest.model.Libro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificaServiceTest {

    @Mock
    private CriterioClassifica criterioFinto;

    @Mock
    private CriterioClassifica secondoCriterio;

    @Test
    void classificaDelegaAllaStrategiaCorrispondenteAlNome() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        when(criterioFinto.getNome()).thenReturn("piu-catalogati");
        when(criterioFinto.ordina()).thenReturn(List.of(libro));

        ClassificaService service = new ClassificaService(List.of(criterioFinto));

        // Act
        List<Libro> risultato = service.classifica("piu-catalogati");

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitolo());
        verify(criterioFinto).ordina();
    }

    @Test
    void classificaConCriterioInesistenteSollevaEccezione() {
        // Arrange
        when(criterioFinto.getNome()).thenReturn("piu-catalogati");
        ClassificaService service = new ClassificaService(List.of(criterioFinto));

        // Act + Assert
        assertThrows(CriterioNonValidoException.class, () -> service.classifica("inesistente"));
    }

    @Test
    void nomiCriteriDisponibiliRestituisceTuttiICriteriIniettati() {
        // Arrange
        when(criterioFinto.getNome()).thenReturn("piu-catalogati");
        when(secondoCriterio.getNome()).thenReturn("piu-recensiti");

        ClassificaService service = new ClassificaService(List.of(criterioFinto, secondoCriterio));

        // Act
        List<String> nomi = service.nomiCriteriDisponibili();

        // Assert
        assertEquals(2, nomi.size());
    }
}