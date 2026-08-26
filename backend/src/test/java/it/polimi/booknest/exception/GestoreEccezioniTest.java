package it.polimi.booknest.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GestoreEccezioniTest {

    private final GestoreEccezioni gestore = new GestoreEccezioni();

    @Test
    void risorsaNonTrovataProduce404() {
        ResponseEntity<String> risposta =
                gestore.gestisciNonTrovato(new LibroNonTrovatoException(5L));

        assertEquals(HttpStatus.NOT_FOUND, risposta.getStatusCode());
        assertNotNull(risposta.getBody());
    }

    @Test
    void conflittoProduce409() {
        ResponseEntity<String> risposta =
                gestore.gestisciConflitto(new VotoGiaEspressoException());

        assertEquals(HttpStatus.CONFLICT, risposta.getStatusCode());
        assertNotNull(risposta.getBody());
    }

    @Test
    void richiestaNonValidaProduce400() {
        ResponseEntity<String> risposta =
                gestore.gestisciRichiestaNonValida(new CriterioNonValidoException("inesistente"));

        assertEquals(HttpStatus.BAD_REQUEST, risposta.getStatusCode());
        assertNotNull(risposta.getBody());
    }

    @Test
    void credenzialiNonValideProduce401() {
        ResponseEntity<String> risposta =
                gestore.gestisciCredenzialiNonValide(new CredenzialiNonValideException());

        assertEquals(HttpStatus.UNAUTHORIZED, risposta.getStatusCode());
        assertNotNull(risposta.getBody());
    }
}