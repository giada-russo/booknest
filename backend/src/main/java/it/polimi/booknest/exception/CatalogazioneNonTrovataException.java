package it.polimi.booknest.exception;

/**
 * Segnala l'assenza di una catalogazione all'interno della libreria.
 * Viene sollevata quando si cerca un'associazione tra un utente e un libro
 * che non esiste nel sistema.
 */
public class CatalogazioneNonTrovataException extends RuntimeException {

  /**
   * Costruisce l'eccezione componendo il messaggio descrittivo con i riferimenti forniti.
   *
   * @param utenteId L'identificativo dell'utente cercato.
   * @param libroId L'identificativo del libro cercato.
   */
  public CatalogazioneNonTrovataException(Long utenteId, Long libroId) {
    super("L'utente con id " + utenteId + " non ha catalogato il libro con id " + libroId);
  }
}