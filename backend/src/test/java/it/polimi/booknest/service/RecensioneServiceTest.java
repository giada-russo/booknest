package it.polimi.booknest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.RecensioneGiaEsistenteException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.polimi.booknest.exception.RecensioneNonConsentitaException;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.RecensioneRepository;

@ExtendWith(MockitoExtension.class)
class RecensioneServiceTest {

    @Mock
    private RecensioneRepository recensioneRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CatalogazioneRepository catalogazioneRepository;

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private RecensioneService recensioneService;

    @Test
    void scriviLanciaEccezioneSeIlLibroNonEStatoLetto() {
        // Arrange
        Long utenteId = 1L;
        Long libroId = 10L;
        String testo = "Un bel libro!";
        boolean pubblica = true;

        Utente utente = new Utente();
        Libro libro = new Libro();

        // Catalogazione creata nello stato di default (DA_LEGGERE)
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(utenteService.trovaPerId(utenteId)).thenReturn(utente);
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(recensioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(false);
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(catalogazione));

        // Act & Assert
        assertThrows(RecensioneNonConsentitaException.class, () -> {
            recensioneService.scrivi(utenteId, libroId, testo, pubblica);
        });

        // Verify che non sia stato chiamato il salvataggio
        verify(recensioneRepository, never()).save(any());
    }

    @Test
    void scriviSalvaLaRecensioneSeIlLibroELetto() {
        // Arrange
        Long utenteId = 1L;
        Long libroId = 10L;
        String testo = "Un capolavoro assoluto!";
        boolean pubblica = true;

        Utente utente = new Utente();
        Libro libro = new Libro();

        Catalogazione catalogazione = new Catalogazione(utente, libro);
        // Portiamo la catalogazione allo stato LETTO per permettere la recensione
        catalogazione.cambiaStato(StatoLettura.LETTO);

        when(utenteService.trovaPerId(utenteId)).thenReturn(utente);
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(recensioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(false);
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.of(catalogazione));

        // ArgumentCaptor per catturare l'entità passata al salvataggio
        ArgumentCaptor<Recensione> captor = ArgumentCaptor.forClass(Recensione.class);

        // Act
        recensioneService.scrivi(utenteId, libroId, testo, pubblica);

        // Assert
        verify(recensioneRepository).save(captor.capture());
        Recensione recensioneSalvata = captor.getValue();

        assertEquals(testo, recensioneSalvata.getTesto());
        assertEquals(pubblica, recensioneSalvata.isPubblica());
        assertNotNull(recensioneSalvata.getDataCreazione());
        assertEquals(utente, recensioneSalvata.getUtente());
        assertEquals(libro, recensioneSalvata.getLibro());
    }
    @Test
    void scriviLanciaEccezioneSeGiaRecensito() {
        // Arrange
        Long utenteId = 1L;
        Long libroId = 10L;
        String testo = "Una seconda recensione";
        boolean pubblica = false;

        Utente utente = new Utente();
        Libro libro = new Libro();

        when(utenteService.trovaPerId(utenteId)).thenReturn(utente);
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        // La recensione esiste già, quindi il controllo deve fallire subito
        when(recensioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act & Assert
        assertThrows(RecensioneGiaEsistenteException.class, () -> {
            recensioneService.scrivi(utenteId, libroId, testo, pubblica);
        });

        // Verify che il salvataggio non venga mai invocato e che non si proceda oltre
        verify(recensioneRepository, never()).save(any());
    }

    @Test
    void scriviSuLibroNonLettoSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Catalogazione catalogazione = new Catalogazione(utente, libro);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(recensioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(false);
        when(catalogazioneRepository.findByUtenteAndLibro(utente, libro))
                .thenReturn(Optional.of(catalogazione));

        // Act + Assert
        assertThrows(RecensioneNonConsentitaException.class,
                () -> recensioneService.scrivi(1L, 5L, "Bellissimo", true));
    }

    @Test
    void scriviRecensioneDuplicataSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(recensioneRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act + Assert
        assertThrows(RecensioneGiaEsistenteException.class,
                () -> recensioneService.scrivi(1L, 5L, "Bellissimo", true));
    }

    @Test
    void scriviSuLibroInesistenteSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(LibroNonTrovatoException.class,
                () -> recensioneService.scrivi(1L, 99L, "Bellissimo", true));
    }

    @Test
    void modificaRecensioneInesistenteSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(recensioneRepository.findByUtenteAndLibro(utente, libro)).thenReturn(Optional.empty());

        assertThrows(RecensioneNonTrovataException.class,
                () -> recensioneService.modifica(1L, 5L, "Rivisto", true));
    }

    @Test
    void trovaPubblichePerLibroRestituisceLeRecensioni() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Recensione r = new Recensione(utente, libro, "Bellissimo", true);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(recensioneRepository.findByLibroAndPubblicaTrue(libro)).thenReturn(List.of(r));

        List<Recensione> risultato = recensioneService.trovaPubblichePerLibro(5L);

        assertEquals(1, risultato.size());
    }

    @Test
    void trovaMieRecensioniRestituisceLeRecensioniDellUtente() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Recensione r = new Recensione(utente, libro, "Bellissimo", true);
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findByUtente(utente)).thenReturn(List.of(r));

        List<Recensione> risultato = recensioneService.trovaMieRecensioni(1L);

        assertEquals(1, risultato.size());
    }
}