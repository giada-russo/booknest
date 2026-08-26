package it.polimi.booknest.controller;

import it.polimi.booknest.dto.CambioStatoRequest;
import it.polimi.booknest.dto.CatalogazioneDTO;
import it.polimi.booknest.dto.VotoLibroRequest;
import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.StatoLettura;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.service.CatalogazioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogazioneControllerTest {

    @Mock
    private CatalogazioneService catalogazioneService;

    @InjectMocks
    private CatalogazioneController catalogazioneController;

    @Test
    void trovaLibreriaConverteLeEntitaInDTO() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Utente utente = new Utente();
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(catalogazioneService.trovaLibreria(1L)).thenReturn(List.of(catalogazione));

        // Act
        List<CatalogazioneDTO> risultato = catalogazioneController.trovaLibreria(1L);

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitoloLibro());
        assertEquals("Umberto Eco", risultato.get(0).getAutoreLibro());
    }

    @Test
    void catalogaRestituisceIlDTODellaNuovaCatalogazione() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Utente utente = new Utente();
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(catalogazioneService.cataloga(1L, 5L)).thenReturn(catalogazione);

        // Act
        CatalogazioneDTO risultato = catalogazioneController.cataloga(1L, 5L);

        // Assert
        assertEquals("Il nome della rosa", risultato.getTitoloLibro());
        assertEquals("Umberto Eco", risultato.getAutoreLibro());
        assertEquals("DA_LEGGERE", risultato.getStato());
    }

    @Test
    void cambiaStatoPassaAlServiceLoStatoRicevutoNellaRichiesta() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Utente utente = new Utente();
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        CambioStatoRequest richiesta = new CambioStatoRequest();
        richiesta.setNuovoStato(StatoLettura.IN_LETTURA);

        when(catalogazioneService.cambiaStato(1L, 5L, StatoLettura.IN_LETTURA)).thenReturn(catalogazione);

        // Act
        CatalogazioneDTO risultato = catalogazioneController.cambiaStato(1L, 5L, richiesta);

        // Assert
        assertEquals("Il nome della rosa", risultato.getTitoloLibro());
        assertEquals("Umberto Eco", risultato.getAutoreLibro());
    }

    @Test
    void assegnaVotoPassaAlServiceIlVotoRicevutoNellaRichiesta() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Utente utente = new Utente();
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        VotoLibroRequest richiesta = new VotoLibroRequest();
        richiesta.setVoto(4);

        when(catalogazioneService.assegnaVoto(1L, 5L, 4)).thenReturn(catalogazione);

        // Act
        CatalogazioneDTO risultato = catalogazioneController.assegnaVoto(1L, 5L, richiesta);

        // Assert
        assertEquals("Il nome della rosa", risultato.getTitoloLibro());
        assertEquals("Umberto Eco", risultato.getAutoreLibro());
    }

    @Test
    void trovaDiarioConverteLeEntitaInDTO() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Utente utente = new Utente();
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(catalogazioneService.trovaDiario(1L)).thenReturn(List.of(catalogazione));

        // Act
        List<CatalogazioneDTO> risultato = catalogazioneController.trovaDiario(1L);

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("Il nome della rosa", risultato.get(0).getTitoloLibro());
        assertEquals("Umberto Eco", risultato.get(0).getAutoreLibro());
    }
}