/// Rappresenta la catalogazione di un libro nella libreria personale.
///
/// I dati provengono dall'endpoint `/api/catalogazioni`, che richiede
/// l'identificativo dell'utente nell'header `X-Utente-Id`.
class Catalogazione {
  final int id;
  final String titoloLibro;
  final String autoreLibro;
  final String stato;
  final int? voto;
  final int idLibro;
  final String? dataCompletamento;

  Catalogazione({
    required this.id,
    required this.titoloLibro,
    required this.autoreLibro,
    required this.stato,
    this.voto,
    required this.idLibro,
    this.dataCompletamento,
  });

  /// Costruisce una [Catalogazione] dalla mappa JSON restituita dal backend.
  ///
  /// Il campo `voto` è nullo finché il libro non è stato letto e valutato.
  factory Catalogazione.fromJson(Map<String, dynamic> json) {
    return Catalogazione(
      id: json['id'],
      titoloLibro: json['titoloLibro'],
      autoreLibro: json['autoreLibro'],
      stato: json['stato'],
      voto: json['voto'],
      idLibro: json['idLibro'],
      dataCompletamento: json['dataCompletamento'],
    );
  }
}