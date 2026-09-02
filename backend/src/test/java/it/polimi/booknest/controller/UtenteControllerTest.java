package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LoginRequest;
import it.polimi.booknest.dto.RegistrazioneRequest;
import it.polimi.booknest.dto.UtenteDTO;
import it.polimi.booknest.dto.UtenteRidottoDTO;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.service.UtenteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtenteControllerTest {

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private UtenteController utenteController;

    @Test
    void registraConverteLEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        RegistrazioneRequest richiesta = new RegistrazioneRequest();
        richiesta.setNome("Romina");
        richiesta.setCognome("Battista");
        richiesta.setUsername("romibat27");
        richiesta.setEmail("romibat@gmail.com");
        richiesta.setPassword("password");

        when(utenteService.registra("Romina", "Battista", "romibat27", "romibat@gmail.com", "password"))
                .thenReturn(utente);

        // Act
        UtenteDTO risultato = utenteController.registra(richiesta);

        // Assert
        assertEquals("romibat27", risultato.getUsername());
        assertEquals("romibat@gmail.com", risultato.getEmail());
        assertEquals("Romina", risultato.getNome());
    }

    @Test
    void loginConverteLEntitaInDTO() {
        // Arrange
        Utente utente = new Utente("Romina", "Battista", "romibat27", "romibat@gmail.com", "hash");
        LoginRequest richiesta = new LoginRequest();
        richiesta.setUsername("romibat27");
        richiesta.setPassword("password");

        when(utenteService.login("romibat27", "password")).thenReturn(utente);

        // Act
        UtenteDTO risultato = utenteController.login(richiesta);

        // Assert
        assertEquals("romibat27", risultato.getUsername());
        assertEquals("romibat@gmail.com", risultato.getEmail());
    }

    @Test
    void seguiDelegaAlService() {
        // Act
        utenteController.segui(1L, 2L);

        // Assert
        verify(utenteService).segui(1L, 2L);
    }

    @Test
    void smettiDiSeguireDelegaAlService() {
        // Act
        utenteController.smettiDiSeguire(1L, 2L);

        // Assert
        verify(utenteService).smettiDiSeguire(1L, 2L);
    }

    @Test
    void trovaSeguitiConverteGliUtentiInDTO() {
        // Arrange
        Utente seguito = new Utente("Marco", "Rossi", "marcoros", "marco@gmail.com", "hash");
        when(utenteService.trovaSeguiti(1L)).thenReturn(Set.of(seguito));

        // Act
        List<UtenteRidottoDTO> risultato = utenteController.trovaSeguiti(1L);

        // Assert
        assertEquals(1, risultato.size());
        assertEquals("marcoros", risultato.get(0).getUsername());
    }
}