package it.polimi.booknest.service;

import it.polimi.booknest.exception.CredenzialiNonValideException;
import it.polimi.booknest.exception.EmailGiaEsistenteException;
import it.polimi.booknest.exception.UsernameGiaEsistenteException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock
    UtenteRepository utenteRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UtenteService utenteService;

    @Test
    void registraLanciaEccezioneSeUsernameGiaEsistente() {
        // Arrange
        String nome = "Mario";
        String cognome = "Rossi";
        String usernameTest = "mario88";
        String email = "mario@email.it";
        String password = "Password123!";

        when(utenteRepository.existsByUsername(usernameTest)).thenReturn(true);

        // Act & Assert (Verifica dello stato/eccezione)
        assertThrows(UsernameGiaEsistenteException.class, () -> {
            utenteService.registra(nome, cognome, usernameTest, email, password);
        });

        // Assert (Verifica dell'interazione)
        verify(utenteRepository, never()).save(any());
    }

    @Test
    void registraSalvaUtenteConPasswordCifrata() {
        // Arrange
        String nome = "Mario";
        String cognome = "Rossi";
        String usernameTest = "mario88";
        String email = "mario@email.it";
        String password = "Password123!";
        String hashFinto = "hash_finto";

        when(utenteRepository.existsByUsername(usernameTest)).thenReturn(false);
        when(utenteRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(hashFinto);

        //Act
        utenteService.registra(nome, cognome, usernameTest, email, password);

        // Assert
        ArgumentCaptor<Utente> captor = ArgumentCaptor.forClass(Utente.class);
        verify(utenteRepository).save(captor.capture());
        Utente utenteSalvato = captor.getValue();

        assertEquals(hashFinto, utenteSalvato.getPasswordHash());
        assertEquals(usernameTest, utenteSalvato.getUsername());
        assertEquals(email,utenteSalvato.getEmail());

    }

    @Test
    void registraLanciaEccezioneSeEmailGiaEsistente() {
        // Arrange
        String nome = "Mario";
        String cognome = "Rossi";
        String usernameTest = "mario88";
        String email = "mario@email.it";
        String password = "Password123!";

        when(utenteRepository.existsByUsername(usernameTest)).thenReturn(false);
        when(utenteRepository.existsByEmail(email)).thenReturn(true);

        //Act+Assert
        assertThrows(EmailGiaEsistenteException.class, ()-> {
            utenteService.registra(nome, cognome, usernameTest, email, password);
        });

        //Assert
        verify(utenteRepository, never()).save(any());
    }

    @Test
    void loginRiuscito(){

        //Arrange
        String nome = "Mario";
        String cognome = "Rossi";
        String usernameTest = "mario88";
        String email = "mario@email.it";
        String password = "Password123!";
        String hashFinto = "hash_finto";

        Utente utenteFittizio = new Utente(nome, cognome, usernameTest, email, hashFinto);

        when(utenteRepository.findByUsername(usernameTest)).thenReturn(Optional.of(utenteFittizio));

        when(passwordEncoder.matches(password,hashFinto)).thenReturn(true);

        Utente risultato = utenteService.login(usernameTest,password);

        assertSame(utenteFittizio, risultato);
    }

    @Test
    void loginLanciaEccezioneSePasswordErrata() {
        // Arrange
        String nome = "Mario";
        String cognome = "Rossi";
        String usernameTest = "mario88";
        String email = "mario@email.it";
        String hashFinto = "hash_finto";

        String passwordSbagliata = "PasswordErrata!";


        Utente utenteFinto = new Utente(nome, cognome, usernameTest, email, hashFinto);


        when(utenteRepository.findByUsername(usernameTest)).thenReturn(Optional.of(utenteFinto));
        when(passwordEncoder.matches(passwordSbagliata, hashFinto)).thenReturn(false);

        // Act & Assert
        assertThrows(CredenzialiNonValideException.class, () -> {
            utenteService.login(usernameTest, passwordSbagliata);
        });
    }

    @Test
    void loginLanciaEccezioneSeUtenteNonEsiste() {
        // Arrange
        String usernameTest = "utenteFantasma";
        String password = "UnaPasswordQualsiasi1!";

        when(utenteRepository.findByUsername(usernameTest)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UtenteNonTrovatoException.class, () -> {
            utenteService.login(usernameTest, password);
        });
    }

    @Test
    void trovaPerIdLanciaEccezioneSeUtenteNonEsiste() {
        // Arrange
        Long idInesistente = 99L;

        when(utenteRepository.findById(idInesistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UtenteNonTrovatoException.class, () -> {
            utenteService.trovaPerId(idInesistente);
        });
    }
}