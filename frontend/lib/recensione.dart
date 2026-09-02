/// Rappresenta una recensione scritta da un utente su un libro.
class Recensione {
  final int id;
  final String testo;
  final bool pubblica;
  final String usernameAutore;
  final int idAutore;
  final String titoloLibro;

  Recensione({
    required this.id,
    required this.testo,
    required this.pubblica,
    required this.usernameAutore,
    required this.idAutore,
    required this.titoloLibro,
  });

  /// Costruisce una [Recensione] dalla mappa JSON restituita dal backend.
  factory Recensione.fromJson(Map<String, dynamic> json) {
    return Recensione(
      id: json['id'],
      testo: json['testo'],
      pubblica: json['pubblica'],
      usernameAutore: json['usernameAutore'],
      idAutore: json['idAutore'],
      titoloLibro: json['titoloLibro'],
    );
  }
}