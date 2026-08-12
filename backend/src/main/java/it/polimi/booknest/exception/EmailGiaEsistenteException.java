package it.polimi.booknest.exception;

public class EmailGiaEsistenteException extends RuntimeException {
    public EmailGiaEsistenteException(String email) {

        super("Email già esistente: " + email);
    }
}
