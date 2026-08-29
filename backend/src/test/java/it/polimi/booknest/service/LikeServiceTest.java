package it.polimi.booknest.service;

import it.polimi.booknest.exception.LibroNonTrovatoException;
import it.polimi.booknest.exception.LikeGiaEspressoException;
import it.polimi.booknest.exception.RecensioneNonTrovataException;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LikeLibro;
import it.polimi.booknest.model.LikeRecensione;
import it.polimi.booknest.model.Recensione;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.LikeLibroRepository;
import it.polimi.booknest.repository.LikeRecensioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.RecensioneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeLibroRepository likeLibroRepository;

    @Mock
    private LikeRecensioneRepository likeRecensioneRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private RecensioneRepository recensioneRepository;

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private LikeService likeService;

    @Test
    void metteLikeLibroSalvaIlLike() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(likeLibroRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(false);

        // Act
        likeService.metteLikeLibro(1L, 5L);

        // Assert
        ArgumentCaptor<LikeLibro> captor = ArgumentCaptor.forClass(LikeLibro.class);
        verify(likeLibroRepository).save(captor.capture());
        LikeLibro likeSalvato = captor.getValue();
        assertEquals(utente, likeSalvato.getUtente());
        assertEquals(libro, likeSalvato.getLibro());
    }

    @Test
    void metteLikeLibroDuplicatoSollevaEccezione() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(likeLibroRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act & Assert
        assertThrows(LikeGiaEspressoException.class, () -> {
            likeService.metteLikeLibro(1L, 5L);
        });
    }

    @Test
    void togliLikeLibroRimuoveIlLike() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));

        // Act
        likeService.togliLikeLibro(1L, 5L);

        // Assert
        verify(likeLibroRepository).deleteByUtenteAndLibro(utente, libro);
    }

    @Test
    void contaLikeLibroRestituisceIlConteggio() {
        // Arrange
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(likeLibroRepository.countByLibro(libro)).thenReturn(3L);

        // Act
        long conteggio = likeService.contaLikeLibro(5L);

        // Assert
        assertEquals(3L, conteggio);
    }

    @Test
    void metteLikeRecensioneSalvaIlLike() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findById(10L)).thenReturn(Optional.of(recensione));
        when(likeRecensioneRepository.existsByUtenteAndRecensione(utente, recensione)).thenReturn(false);

        // Act
        likeService.metteLikeRecensione(1L, 10L);

        // Assert
        ArgumentCaptor<LikeRecensione> captor = ArgumentCaptor.forClass(LikeRecensione.class);
        verify(likeRecensioneRepository).save(captor.capture());
        LikeRecensione likeSalvato = captor.getValue();
        assertEquals(utente, likeSalvato.getUtente());
        assertEquals(recensione, likeSalvato.getRecensione());
    }

    @Test
    void contaLikeRecensioneRestituisceIlConteggio() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", "9788845292613");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);
        when(recensioneRepository.findById(10L)).thenReturn(Optional.of(recensione));
        when(likeRecensioneRepository.countByRecensione(recensione)).thenReturn(5L);

        // Act
        long conteggio = likeService.contaLikeRecensione(10L);

        // Assert
        assertEquals(5L, conteggio);
    }

    @Test
    void metteLikeSuLibroInesistenteSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LibroNonTrovatoException.class,
                () -> likeService.metteLikeLibro(1L, 99L));
    }

    @Test
    void metteLikeSuRecensioneInesistenteSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecensioneNonTrovataException.class,
                () -> likeService.metteLikeRecensione(1L, 99L));
    }

    @Test
    void metteLikeRecensioneDuplicatoSollevaEccezione() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findById(10L)).thenReturn(Optional.of(recensione));
        when(likeRecensioneRepository.existsByUtenteAndRecensione(utente, recensione)).thenReturn(true);

        assertThrows(LikeGiaEspressoException.class,
                () -> likeService.metteLikeRecensione(1L, 10L));
    }

    @Test
    void togliLikeRecensioneRimuoveIlLike() {
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findById(10L)).thenReturn(Optional.of(recensione));

        likeService.togliLikeRecensione(1L, 10L);

        verify(likeRecensioneRepository).deleteByUtenteAndRecensione(utente, recensione);
    }

    @Test
    void haMessoLikeLibroRestituisceVeroSeIlLikeEsiste() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(libroRepository.findById(5L)).thenReturn(Optional.of(libro));
        when(likeLibroRepository.existsByUtenteAndLibro(utente, libro)).thenReturn(true);

        // Act
        boolean risultato = likeService.haMessoLikeLibro(1L, 5L);

        // Assert
        assertTrue(risultato);
    }

    @Test
    void haMessoLikeRecensioneRestituisceFalsoSeIlLikeNonEsiste() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        Libro libro = new Libro("Circe", "Madeline Miller", "9788823522271");
        Recensione recensione = new Recensione(utente, libro, "Bellissimo", true);

        when(utenteService.trovaPerId(1L)).thenReturn(utente);
        when(recensioneRepository.findById(10L)).thenReturn(Optional.of(recensione));
        when(likeRecensioneRepository.existsByUtenteAndRecensione(utente, recensione)).thenReturn(false);

        // Act
        boolean risultato = likeService.haMessoLikeRecensione(1L, 10L);

        // Assert
        assertFalse(risultato);
    }
}