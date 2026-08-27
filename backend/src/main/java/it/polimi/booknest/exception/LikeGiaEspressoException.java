package it.polimi.booknest.exception;

/**
 * Eccezione sollevata quando un utente tenta di esprimere un like su una risorsa
 * (libro o recensione) a cui ha già messo like in precedenza.
 * <p>
 * Rappresenta una violazione delle regole di dominio e viene mappata
 * a livello HTTP sullo stato {@code 409 Conflict}.
 * </p>
 */
public class LikeGiaEspressoException extends RuntimeException {

  /**
   * Costruisce una nuova eccezione specificando l'utente e la risorsa coinvolti.
   *
   * @param utenteId  l'identificativo dell'utente che tenta di mettere il like
   * @param risorsaId l'identificativo della risorsa (libro o recensione) già apprezzata
   */
  public LikeGiaEspressoException(Long utenteId, Long risorsaId) {
    super("L'utente con id " + utenteId + " ha già espresso un like sulla risorsa con id " + risorsaId);
  }
}