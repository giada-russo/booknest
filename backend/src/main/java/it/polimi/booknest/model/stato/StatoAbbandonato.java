package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;

/**
 * Rappresenta lo stato di un libro la cui lettura è stata interrotta.
 * <p>
 * A livello di dominio, l'abbandono di un libro non è considerato uno stato definitivo.
 * L'utente mantiene la possibilità di riprendere la lettura in un secondo momento
 * (transitando verso {@code IN_LETTURA}) oppure di rimettere il libro nella lista
 * di quelli da affrontare in futuro (transitando verso {@code DA_LEGGERE}).
 * </p>
 */
public class StatoAbbandonato implements StatoCatalogazione {

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
        return nuovoStato == StatoLettura.IN_LETTURA || nuovoStato == StatoLettura.DA_LEGGERE;
    }
}