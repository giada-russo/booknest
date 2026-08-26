/// Rappresenta un libro ricevuto dal backend BookNest.
///
/// I dati provengono dall'endpoint pubblico `/api/libri`, che restituisce
/// i libri del catalogo con la media dei voti assegnati dagli utenti.
class Libro {
  final int id;
  final String titolo;
  final String autore;
  final double? votoMedio;

  Libro({
    required this.id,
    required this.titolo,
    required this.autore,
    this.votoMedio,
  });

  /// Costruisce un [Libro] a partire dalla mappa JSON restituita dal backend.
  ///
  /// Il campo `votoMedio` è nullo quando nessun utente ha ancora votato il libro.
  factory Libro.fromJson(Map<String, dynamic> json) {
    return Libro(
      id: json['id'],
      titolo: json['titolo'],
      autore: json['autore'],
      votoMedio: (json['votoMedio'] as num?)?.toDouble(),
    );
  }
}