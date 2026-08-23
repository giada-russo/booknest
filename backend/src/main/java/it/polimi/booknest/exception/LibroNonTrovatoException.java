package it.polimi.booknest.exception;

/**
 * Segnala il mancato reperimento di un libro nel database.
 * Viene sollevata quando si tenta di eseguire un'operazione su un libro
 * identificato da un ID inesistente.
 */
public class LibroNonTrovatoException extends RuntimeException {

  /**
   * Costruisce l'eccezione componendo il messaggio descrittivo con l'ID non valido.
   *
   * @param id L'identificativo del libro che non è stato possibile trovare.
   */
  public LibroNonTrovatoException(Long id) {
    super("Nessun libro trovato con id: " + id);
  }
}