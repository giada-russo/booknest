/// Rappresenta uno showdown, il sondaggio testa a testa tra due libri
/// generato periodicamente dal sistema.
///
/// I dati provengono dall'endpoint pubblico `/api/showdown/attivi`, che
/// restituisce i soli sondaggi ancora aperti al voto.
class Showdown {
  final int id;
  final String titoloLibroA;
  final String autoreLibroA;
  final String titoloLibroB;
  final String autoreLibroB;

  /// Crea uno showdown con i dati dei due libri in competizione.
  Showdown({
    required this.id,
    required this.titoloLibroA,
    required this.autoreLibroA,
    required this.titoloLibroB,
    required this.autoreLibroB,
  });

  /// Costruisce uno [Showdown] dalla mappa JSON restituita dal backend.
  ///
  /// I conteggi dei voti non sono inclusi: risiedono in memoria nel service
  /// e si ottengono dall'endpoint dei risultati.
  factory Showdown.fromJson(Map<String, dynamic> json) {
    return Showdown(
      id: json['id'],
      titoloLibroA: json['titoloLibroA'],
      autoreLibroA: json['autoreLibroA'],
      titoloLibroB: json['titoloLibroB'],
      autoreLibroB: json['autoreLibroB'],
    );
  }
}