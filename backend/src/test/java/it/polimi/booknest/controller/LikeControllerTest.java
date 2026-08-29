package it.polimi.booknest.controller;

import it.polimi.booknest.service.LikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    @Test
    void metteLikeLibroDelegaAlService() {
        // Act
        likeController.metteLikeLibro(1L, 5L);

        // Assert
        verify(likeService).metteLikeLibro(1L, 5L);
    }

    @Test
    void togliLikeLibroDelegaAlService() {
        // Act
        likeController.togliLikeLibro(1L, 5L);

        // Assert
        verify(likeService).togliLikeLibro(1L, 5L);
    }

    @Test
    void contaLikeLibroDelegaAlService() {
        // Arrange
        when(likeService.contaLikeLibro(5L)).thenReturn(3L);

        // Act
        long risultato = likeController.contaLikeLibro(5L);

        // Assert
        assertEquals(3L, risultato);
        verify(likeService).contaLikeLibro(5L);
    }

    @Test
    void metteLikeRecensioneDelegaAlService() {
        // Act
        likeController.metteLikeRecensione(1L, 10L);

        // Assert
        verify(likeService).metteLikeRecensione(1L, 10L);
    }

    @Test
    void togliLikeRecensioneDelegaAlService() {
        // Act
        likeController.togliLikeRecensione(1L, 10L);

        // Assert
        verify(likeService).togliLikeRecensione(1L, 10L);
    }

    @Test
    void contaLikeRecensioneDelegaAlService() {
        // Arrange
        when(likeService.contaLikeRecensione(10L)).thenReturn(5L);

        // Act
        long risultato = likeController.contaLikeRecensione(10L);

        // Assert
        assertEquals(5L, risultato);
        verify(likeService).contaLikeRecensione(10L);
    }

    @Test
    void haMessoLikeLibroDelegaAlService() {
        // Arrange
        when(likeService.haMessoLikeLibro(1L, 5L)).thenReturn(true);

        // Act
        boolean risultato = likeController.haMessoLikeLibro(1L, 5L);

        // Assert
        assertTrue(risultato);
        verify(likeService).haMessoLikeLibro(1L, 5L);
    }

    @Test
    void haMessoLikeRecensioneDelegaAlService() {
        // Arrange
        when(likeService.haMessoLikeRecensione(1L, 10L)).thenReturn(false);

        // Act
        boolean risultato = likeController.haMessoLikeRecensione(1L, 10L);

        // Assert
        assertFalse(risultato);
        verify(likeService).haMessoLikeRecensione(1L, 10L);
    }
}