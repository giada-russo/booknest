package it.polimi.booknest.dto;

import it.polimi.booknest.model.Utente;

/**
 * DTO ridotto per l'esposizione pubblica di un utente.
 * <p>
 * Espone il solo identificativo e lo username, sufficienti a riconoscere
 * una persona e a costruire le richieste di follow. I dati anagrafici e
 * l'indirizzo email restano riservati all'utente stesso, che li riceve
 * in {@link UtenteDTO} al momento dell'autenticazione.
 */
public class UtenteRidottoDTO {

    private Long id;
    private String username;

    /**
     * Costruisce il DTO a partire dall'entità.
     *
     * @param utente l'entità da cui estrarre i dati
     */
    public UtenteRidottoDTO(Utente utente) {
        this.id = utente.getId();
        this.username = utente.getUsername();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
