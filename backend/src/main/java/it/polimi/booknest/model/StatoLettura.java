package it.polimi.booknest.model;

/**
 * Rappresenta le fasi del ciclo di vita della catalogazione di un libro all'interno del sistema.
 * <p>
 * <b>Nota sulle scelte progettuali (Rif. STATO.md):</b><br>
 * L'introduzione dello stato {@code ABBANDONATO} costituisce un'aggiunta rispetto alla
 * proposta iniziale del progetto, che prevedeva solamente tre stati.
 * La motivazione alla base di questa estensione è prettamente logica e di dominio:
 * un libro iniziato ma non portato a termine non può essere classificato come "letto",
 * ma non è nemmeno più "in lettura". In assenza di questo quarto stato, il libro
 * rimarrebbe bloccato a tempo indeterminato in {@code IN_LETTURA}, introducendo
 * un'incoerenza logica e falsando i dati del diario di lettura dell'utente.
 * </p>
 */
public enum StatoLettura {
    DA_LEGGERE,
    IN_LETTURA,
    LETTO,
    ABBANDONATO
}
