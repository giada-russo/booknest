package it.polimi.booknest.exception;

public class ShowdownNonAttivoException extends RuntimeException {
    public ShowdownNonAttivoException(Long id) {

        super("Showdown non attivo con id: " + id);
    }
}
