package it.polimi.booknest.controller;

import it.polimi.booknest.dto.RecensioneDTO;
import it.polimi.booknest.dto.RecensioneRequest;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.service.RecensioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecensioneControllerTest {

    @Mock
    private RecensioneService recensioneService;

    @InjectMocks
    private RecensioneController recensioneController;

    @Test
    void scriviConverteLEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        RecensioneRequest richiesta = new RecensioneRequest();
        richiesta.setTesto("Bellissimo");
        richiesta.setPubblica(true);

        when(recensioneService.scrivi(1L, 5L, "Bellissimo", true)).thenReturn(recensione);

        // Act
        RecensioneDTO risultato = recensioneController.scrivi(1L, 5L, richiesta);

        // Assert
        assertEquals("Bellissimo", risultato.getTesto());
        assertTrue(risultato.isPubblica());
        assertEquals("romibat27", risultato.getUsernameAutore());
        assertEquals("Il nome della rosa", risultato.getTitoloLibro());
    }

    @Test
    void modificaConverteLEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Rivisto", true);

        RecensioneRequest richiesta = new RecensioneRequest();
        richiesta.setTesto("Rivisto");
        richiesta.setPubblica(true);

        when(recensioneService.modifica(1L, 5L, "Rivisto", true)).thenReturn(recensione);

        // Act
        RecensioneDTO risultato = recensioneController.modifica(1L, 5L, richiesta);

        // Assert
        assertEquals("Rivisto", risultato.getTesto());
        assertTrue(risultato.isPubblica());
        assertEquals("romibat27", risultato.getUsernameAutore());
        assertEquals("Il nome della rosa", risultato.getTitoloLibro());
    }

    @Test
    void trovaPubblichePerLibroConverteLeEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        when(recensioneService.trovaPubblichePerLibro(5L)).thenReturn(List.of(recensione));

        // Act
        List<RecensioneDTO> risultato = recensioneController.trovaPubblichePerLibro(5L);

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitoloLibro());
        assertEquals("romibat27", risultato.get(0).getUsernameAutore());
    }

    @Test
    void trovaMieRecensioniConverteLeEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        when(recensioneService.trovaMieRecensioni(1L)).thenReturn(List.of(recensione));

        // Act
        List<RecensioneDTO> risultato = recensioneController.trovaMieRecensioni(1L);

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Bellissimo", risultato.get(0).getTesto());
        assertEquals("romibat27", risultato.get(0).getUsernameAutore());
    }
}