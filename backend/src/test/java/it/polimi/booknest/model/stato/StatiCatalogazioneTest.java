package it.polimi.booknest.model.stato;

import it.polimi.booknest.model.StatoLettura;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatiCatalogazioneTest {

    @Test
    void statoDaLeggereNonPermetteVotoNeRecensione() {
        // Arrange
        StatoDaLeggere stato = new StatoDaLeggere();

        // Act + Assert
        assertFalse(stato.permetteVoto());
        assertFalse(stato.permetteRecensione());
        assertTrue(stato.puoPassareA(StatoLettura.IN_LETTURA));
        assertFalse(stato.puoPassareA(StatoLettura.ABBANDONATO));
    }

    @Test
    void statoInLetturaGestisceCorrettamenteTransizioniEPermessi() {
        // Arrange
        StatoInLettura stato = new StatoInLettura();

        // Act + Assert
        assertFalse(stato.permetteVoto());
        assertFalse(stato.permetteRecensione());
        assertTrue(stato.puoPassareA(StatoLettura.LETTO));
        assertFalse(stato.puoPassareA(StatoLettura.DA_LEGGERE));
    }

    @Test
    void statoLettoGestisceCorrettamenteTransizioniEPermessi() {
        // Arrange
        StatoLetto stato = new StatoLetto();

        // Act + Assert
        assertTrue(stato.permetteVoto());
        assertTrue(stato.permetteRecensione());
        assertFalse(stato.puoPassareA(StatoLettura.IN_LETTURA));
        assertFalse(stato.puoPassareA(StatoLettura.ABBANDONATO));
    }

    @Test
    void statoAbbandonatoGestisceCorrettamenteTransizioniEPermessi() {
        // Arrange
        StatoAbbandonato stato = new StatoAbbandonato();

        // Act + Assert
        assertFalse(stato.permetteVoto());
        assertFalse(stato.permetteRecensione());
        assertTrue(stato.puoPassareA(StatoLettura.IN_LETTURA));
        assertTrue(stato.puoPassareA(StatoLettura.DA_LEGGERE));
    }
}