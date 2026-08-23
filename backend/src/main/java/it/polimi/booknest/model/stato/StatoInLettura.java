package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;

/**
 * Rappresenta lo stato di un libro attualmente in corso di lettura.
 * <p>
 * Da questo stato, il libro può transitare verso {@code LETTO} (al completamento
 * dell'opera) oppure verso {@code ABBANDONATO} (se l'utente decide di interromperne
 * la fruizione prima della fine).
 * </p>
 */
public class StatoInLettura implements StatoCatalogazione {

    @Override
    public boolean permetteVoto() {
        return false;
    }

    @Override
    public boolean permetteRecensione() {
        return false;
    }

    @Override
    public boolean puoPassareA(StatoLettura nuovoStato) {
        return nuovoStato == StatoLettura.LETTO || nuovoStato == StatoLettura.ABBANDONATO;
    }
}