package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;

public interface StatoCatalogazione {
    boolean permetteVoto();
    boolean permetteRecensione();
    boolean puoPassareA(StatoLettura nuovostato);
}
