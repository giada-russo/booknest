package it.polimi.booknest.controller;

import it.polimi.booknest.dto.LoginRequest;
import it.polimi.booknest.dto.RegistrazioneRequest;
import it.polimi.booknest.dto.UtenteDTO;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.service.UtenteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {
    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/registrazione")
    public UtenteDTO registra(@RequestBody RegistrazioneRequest richiesta){

        Utente utente = utenteService.registra(
                richiesta.getNome(),
                richiesta.getCognome(),
                richiesta.getUsername(),
                richiesta.getEmail(),
                richiesta.getPassword()
        );
        return new UtenteDTO(utente);
    }

    @PostMapping("/login")
    public UtenteDTO login(@RequestBody LoginRequest richiesta){

        Utente utente = utenteService.login(
                richiesta.getUsername(),
                richiesta.getPassword()
        );
        return new UtenteDTO(utente);
    }

}
