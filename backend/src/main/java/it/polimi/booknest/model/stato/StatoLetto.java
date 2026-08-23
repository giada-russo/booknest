package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;

/**
 * Rappresenta lo stato "Letto" nel ciclo di vita della catalogazione di un libro.
 * <p>
 * Questo stato è progettato come <b>finale</b> (o terminale) all'interno della macchina a stati.
 * A livello di dominio, un libro completato resta completato: non sono ammesse transizioni
 * verso stati precedenti o alternativi. Questa rigidità garantisce la coerenza logica e
 * l'integrità del diario di lettura dell'utente, rendendo al contempo definitivi e
 * affidabili il voto e la recensione associati all'opera.
 * </p>
 */
public class StatoLetto implements StatoCatalogazione {

    @Override
    public boolean permetteVoto() {
        return true;
    }

    @Override
    public boolean permetteRecensione() {
        return true;
    }

    @Override
    public boolean puoPassareA(StatoLettura nuovoStato) {
        return false;
    }
}