import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'libro.dart';

void main() {
  runApp(const BookNestApp());
}

class BookNestApp extends StatelessWidget {
  const BookNestApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BookNest',
      home: SchermataLibri(),
    );
  }
}
class SchermataLibri extends StatefulWidget {
  const SchermataLibri({super.key});

  @override
  State<SchermataLibri> createState() => _StatoSchermataLibri();
}
class _StatoSchermataLibri extends State<SchermataLibri> {
  List<Libro> libri = [];
  bool caricamento = true;

  @override
  void initState() {
    super.initState();
    caricaLibri();
  }

  Future<void> caricaLibri() async {
    final risposta = await http.get(
      Uri.parse('http://localhost:8080/api/libri'),
    );
    final List<dynamic> dati = jsonDecode(risposta.body);
    setState(() {
      libri = dati.map((json) => Libro.fromJson(json)).toList();
      caricamento = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('BookNest')),
      body: caricamento
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: libri.length,
        itemBuilder: (context, i) => ListTile(
          title: Text(libri[i].titolo),
          subtitle: Text(libri[i].autore),
        ),
      ),
    );
  }
}