package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;

/**
 * Rappresenta lo stato iniziale di un libro inserito nel catalogo e non ancora iniziato.
 * <p>
 * Da questo stato, l'entità può passare a {@code IN_LETTURA} (quando l'utente inizia
 * effettivamente l'opera) oppure direttamente a {@code LETTO} (qualora l'utente lo
 * completi senza tracciare la fase intermedia nel diario).
 * </p>
 */
public class StatoDaLeggere implements StatoCatalogazione {

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
        return nuovoStato == StatoLettura.IN_LETTURA || nuovoStato == StatoLettura.LETTO;
    }
}