package it.polimi.booknest.service;

import it.polimi.booknest.exception.LibroGiaCatalogatoException;
import it.polimi.booknest.exception.TransizioneNonValidaException;
import it.polimi.booknest.exception.VotoNonConsentitoException;
import it.polimi.booknest.model.Catalogazione;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.StatoLettura;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogazioneServiceTest {

    @Mock
    private CatalogazioneRepository catalogazioneRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private CatalogazioneService catalogazioneService;

    @Test
    void cambiaStatoAggiornaLoStatoSeLaTransizioneEValida() {
        // Arrange
        Long idUtente = 1L;
        Long idLibro = 1L;

        Utente utente = new Utente();
        Libro libro = new Libro();

        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(utenteService.trovaPerId(idUtente)).thenReturn(utente);
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro))
                .thenReturn(Optional.of(catalogazione));

        // Act
        catalogazioneService.cambiaStato(idUtente, idLibro, StatoLettura.IN_LETTURA);

        // Assert
        assertEquals(StatoLettura.IN_LETTURA, catalogazione.getStato());
    }

    @Test
    void cambiaStatoLanciaEccezioneSeLaTransizioneNonEValida() {
        // Arrange
        Long idUtente = 1L;
        Long idLibro = 1L;

        Utente utente = new Utente();
        Libro libro = new Libro();

        Catalogazione catalogazione = new Catalogazione(utente, libro);
        catalogazione.cambiaStato(StatoLettura.LETTO);

        when(utenteService.trovaPerId(idUtente)).thenReturn(utente);
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro))
                .thenReturn(Optional.of(catalogazione));

        // Act & Assert
        assertThrows(TransizioneNonValidaException.class, () ->
                catalogazioneService.cambiaStato(idUtente, idLibro, StatoLettura.IN_LETTURA)
        );

        // Assert
        verify(catalogazioneRepository, never()).save(any());
    }

    @Test
    void cambiaStatoAStatoLettoValorizzaLaDataDiCompletamento() {
        // Arrange
        Long idUtente = 1L;
        Long idLibro = 1L;

        Utente utente = new Utente();
        Libro libro = new Libro();

        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(utenteService.trovaPerId(idUtente)).thenReturn(utente);
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro))
                .thenReturn(Optional.of(catalogazione));

        // Precondizione
        assertNull(catalogazione.getDataCompletamento());

        // Act
        catalogazioneService.cambiaStato(idUtente, idLibro, StatoLettura.LETTO);

        // Assert
        assertNotNull(catalogazione.getDataCompletamento());
    }

    @Test
    void assegnaVotoLanciaEccezioneSeIlLibroNonEStatoLetto() {
        // Arrange
        Long idUtente = 1L;
        Long idLibro = 1L;

        Utente utente = new Utente();
        Libro libro = new Libro();

        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(utenteService.trovaPerId(idUtente)).thenReturn(utente);
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro))
                .thenReturn(Optional.of(catalogazione));

        // Act & Assert
        assertThrows(VotoNonConsentitoException.class, () ->
                catalogazioneService.assegnaVoto(idUtente, idLibro, 5)
        );

        // Assert
        verify(catalogazioneRepository, never()).save(any());
    }

    @Test
    void catalogaLanciaEccezioneSeIlLibroEGiaCatalogato() {
        // Arrange
        Long idUtente = 1L;
        Long idLibro = 1L;

        Utente utente = new Utente();
        Libro libro = new Libro();

        when(utenteService.trovaPerId(idUtente)).thenReturn(utente);
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act & Assert
        assertThrows(LibroGiaCatalogatoException.class, () ->
                catalogazioneService.cataloga(idUtente, idLibro)
        );

        // Assert
        verify(catalogazioneRepository, never()).save(any());
    }
}