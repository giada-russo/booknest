package it.polimi.booknest.dto;

import it.polimi.booknest.model.Utente;

/**
 * Data Transfer Object delegato al trasferimento in uscita dei dati anagrafici (Response DTO).
 * <p>
 * Ha il duplice scopo di filtrare i dati sensibili dell'entità di dominio (come il passwordHash)
 * e di fornire al client un set di informazioni ottimizzato.
 * L'inclusione dell'{@code id} non è casuale: viene esposto in chiaro affinché il frontend
 * possa immagazzinarlo e allegarlo agli header nelle successive richieste API,
 * permettendo al backend di identificare rapidamente la risorsa.
 * </p>
 */
public class UtenteDTO {
    private Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;

    /**
     * Mappa un'entità di dominio in un oggetto di trasferimento (Proiezione).
     * <p>
     * il passaggio dell'entità al costruttore accentra la logica di conversione
     * in un unico punto del codice. Soprattutto, protegge i layer superiori (Controller)
     * da bug logici silenziosi: evita che il chiamante debba passare una sequenza di
     * parametri omogenei (4 {@code String}), prevenendo errori a runtime dovuti all'inversione
     * accidentale dell'ordine dei campi (es. scambiare username con email).
     * </p>
     *
     * @param utente L'entità sorgente estratta dal database da cui copiare
     *               i soli attributi sicuri per la visualizzazione lato client.
     */
    public UtenteDTO(Utente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.username = utente.getUsername();
        this.email = utente.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
