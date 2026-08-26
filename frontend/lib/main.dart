import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'libro.dart';
import 'schermata_showdown.dart';

void main() {
  runApp(const BookNestApp());
}

class BookNestApp extends StatelessWidget {
  const BookNestApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BookNest',
      home: const SchermataLibri(),
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
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaLibri();
  }

  /// Carica il catalogo dei libri dal backend.
  ///
  /// In caso di errore di rete o di risposta non valida, interrompe il
  /// caricamento e mostra un messaggio all'utente invece di lasciare
  /// la schermata bianca.
  Future<void> caricaLibri() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/libri'),
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        libri = dati.map((json) => Libro.fromJson(json)).toList();
        caricamento = false;
      });
    } catch (e) {
      setState(() {
        caricamento = false;
        errore = true;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('BookNest'),
        actions: [
          IconButton(
            icon: const Icon(Icons.how_to_vote),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const SchermataShowdown()),
              );
            },
          ),
        ],
      ),
      body: caricamento
          ? const Center(child: CircularProgressIndicator())
          : errore
          ? const Center(child: Text('Impossibile caricare il catalogo'))
          : ListView.builder(
        itemCount: libri.length,
        itemBuilder: (context, i) => ListTile(
          title: Text(libri[i].titolo),
          subtitle: Text(libri[i].autore),
          trailing: libri[i].votoMedio == null
              ? const Text('—')
              : Text('★ ${libri[i].votoMedio!.toStringAsFixed(1)}'),
        ),
      ),
    );
  }
}