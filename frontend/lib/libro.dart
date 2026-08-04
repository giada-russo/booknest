/// Rappresenta un libro ricevuto dal backend BookNest.
class Libro {
  final int id;
  final String titolo;
  final String autore;
  final String isbn;

  Libro({required this.id, required this.titolo, required this.autore, required this.isbn});

  factory Libro.fromJson(Map<String, dynamic> json) {
    return Libro(
      id: json['id'],
      titolo: json['titolo'],
      autore: json['autore'],
      isbn: json['isbn'],
    );
  }
}