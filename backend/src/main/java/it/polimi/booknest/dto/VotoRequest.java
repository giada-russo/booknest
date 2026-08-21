package it.polimi.booknest.dto;

import it.polimi.booknest.model.LibroScelto;

/**
 * Data Transfer Object che rappresenta la richiesta di voto (direzione client -> server).
 * Incapsula la scelta effettuata dall'utente in merito a un determinato Showdown.
 */
public class VotoRequest {

    private LibroScelto libroScelto;

    /**
     * Costruttore vuoto.
     * Utilizzato esclusivamente dalla libreria Jackson per instanziare l'oggetto in fase
     * di deserializzazione del JSON proveniente dal client, prima di invocare i setter.
     */
    public VotoRequest() {
    }

    public LibroScelto getLibroScelto() {
        return libroScelto;
    }

    public void setLibroScelto(LibroScelto libroScelto) {
        this.libroScelto = libroScelto;
    }
}