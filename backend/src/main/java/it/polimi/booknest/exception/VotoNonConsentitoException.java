package it.polimi.booknest.exception;

import it.polimi.booknest.model.StatoLettura;

/**
 * Segnala un'operazione di valutazione non valida.
 * Viene sollevata quando si tenta di assegnare un voto a una catalogazione
 * il cui stato corrente (es. DA_LEGGERE o IN_LETTURA) non prevede tale operazione.
 */
public class VotoNonConsentitoException extends RuntimeException {

  /**
   * Costruisce l'eccezione formulando automaticamente il messaggio descrittivo
   * basato sullo stato che ha bloccato l'operazione.
   *
   * @param statoAttuale Lo stato corrente del libro che impedisce l'inserimento del voto.
   */
  public VotoNonConsentitoException(StatoLettura statoAttuale) {
    super("Non è permesso assegnare un voto quando il libro si trova nello stato: " + statoAttuale);
  }
}