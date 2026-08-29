package it.polimi.booknest.service;

import it.polimi.booknest.exception.*;
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
    @Test
    void catalogaLibroGiaPresenteSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act + Assert
        assertThrows(LibroGiaCatalogatoException.class,
                () -> catalogazioneService.cataloga(1L, 5L));
    }

    @Test
    void cambiaStatoNonValidoSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Catalogazione catalogazione = new Catalogazione(utente, libro); // Nasce DA_LEGGERE

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(catalogazione));

        // Act + Assert
        assertThrows(TransizioneNonValidaException.class,
                () -> catalogazioneService.cambiaStato(1L, 5L, StatoLettura.ABBANDONATO));
    }

    @Test
    void assegnaVotoSuLibroNonLettoSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Catalogazione catalogazione = new Catalogazione(utente, libro); // Nasce DA_LEGGERE (quindi non permette voto)

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(catalogazione));

        // Act + Assert
        assertThrows(VotoNonConsentitoException.class,
                () -> catalogazioneService.assegnaVoto(1L, 5L, 4));
    }

    @Test
    void cambiaStatoConLibroInesistenteSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LibroNonTrovatoException.class,
                () -> catalogazioneService.cambiaStato(1L, 99L, StatoLettura.LETTO));
    }

    @Test
    void cambiaStatoSenzaCatalogazioneSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.empty());

        assertThrows(CatalogazioneNonTrovataException.class,
                () -> catalogazioneService.cambiaStato(1L, 5L, StatoLettura.LETTO));
    }

    @Test
    void catalogaConLibroInesistenteSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LibroNonTrovatoException.class,
                () -> catalogazioneService.cataloga(1L, 99L));
    }

    @Test
    void assegnaVotoFuoriIntervalloSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Catalogazione c = new Catalogazione(utente, libro);
        c.cambiaStato(StatoLettura.LETTO);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(c));

        assertThrows(IllegalArgumentException.class,
                () -> catalogazioneService.assegnaVoto(1L, 5L, 7));
    }

    @Test
    void assegnaVotoSuLibroLettoFunziona() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Catalogazione c = new Catalogazione(utente, libro);
        c.cambiaStato(StatoLettura.LETTO);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(c));

        Catalogazione risultato = catalogazioneService.assegnaVoto(1L, 5L, 4);

        assertEquals(4, risultato.getVoto());
        verify(catalogazioneRepository).save(c);
    }
}