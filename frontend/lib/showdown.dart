/// Rappresenta uno showdown ricevuto dal backend BookNest.
class Showdown {
  final int id;
  final String titoloLibroA;
  final String autoreLibroA;
  final String titoloLibroB;
  final String autoreLibroB;

  Showdown({
    required this.id,
    required this.titoloLibroA,
    required this.autoreLibroA,
    required this.titoloLibroB,
    required this.autoreLibroB,
  });

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