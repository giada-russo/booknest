import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:frontend/schermata_libro.dart';
import 'package:http/http.dart' as http;
import 'libro.dart';

/// Mostra la classifica dei libri secondo il criterio scelto dall'utente.
///
/// I criteri disponibili sono richiesti al backend, che li ricava dalle
/// strategie registrate (pattern Strategy): aggiungere un criterio lato
/// server lo rende disponibile qui senza modificare questo codice.
class SchermataClassifica extends StatefulWidget {
  const SchermataClassifica({super.key});

  @override
  State<SchermataClassifica> createState() => _StatoSchermataClassifica();
}

class _StatoSchermataClassifica extends State<SchermataClassifica> {
  List<String> criteri = [];
  String? criterioScelto;
  List<Libro> libri = [];
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaCriteri();
  }

  /// Richiede al backend l'elenco dei criteri disponibili e carica
  /// la classifica secondo il primo di essi.
  Future<void> caricaCriteri() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/classifica/criteri'),
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        criteri = dati.cast<String>();
        criterioScelto = criteri.first;
      });
      await caricaClassifica();
    } catch (e) {
      setState(() {
        caricamento = false;
        errore = true;
      });
    }
  }

  /// Carica la classifica secondo il criterio attualmente selezionato.
  Future<void> caricaClassifica() async {
    setState(() => caricamento = true);
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/classifica/libri/$criterioScelto'),
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
    if (errore) {
      return const Center(child: Text('Impossibile caricare la classifica'));
    }
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: DropdownButton<String>(
            value: criterioScelto,
            isExpanded: true,
            items: criteri
                .map((c) => DropdownMenuItem(value: c, child: Text(c)))
                .toList(),
            onChanged: (nuovo) {
              setState(() => criterioScelto = nuovo);
              caricaClassifica();
            },
          ),
        ),
        Expanded(
          child: caricamento
              ? const Center(child: CircularProgressIndicator())
              : ListView.builder(
            itemCount: libri.length,
            itemBuilder: (context, i) => ListTile(
              onTap: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => SchermataLibro(libro: libri[i]),
                ),
              ),
              leading: Text('${i + 1}'),
              title: Text(libri[i].titolo),
              subtitle: Text(libri[i].autore),
            ),
          ),
        ),
      ],
    );
  }
}