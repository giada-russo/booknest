import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'recensione.dart';
import 'schermata_showdown.dart';

/// Mostra le recensioni scritte dall'utente e consente di scriverne di nuove.
///
/// La scrittura è consentita dal backend solo per i libri con stato LETTO:
/// il rifiuto viene mostrato con il messaggio dell'eccezione di dominio.
class SchermataRecensioni extends StatefulWidget {
  const SchermataRecensioni({super.key});

  @override
  State<SchermataRecensioni> createState() => _StatoSchermataRecensioni();
}

class _StatoSchermataRecensioni extends State<SchermataRecensioni> {
  List<Recensione> recensioni = [];
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaRecensioni();
  }

  Future<void> caricaRecensioni() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/recensioni/mie'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        recensioni = dati.map((json) => Recensione.fromJson(json)).toList();
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
    if (caricamento) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errore) {
      return const Center(child: Text('Impossibile caricare le recensioni'));
    }
    if (recensioni.isEmpty) {
      return const Center(child: Text('Nessuna recensione scritta'));
    }
    return ListView.builder(
      itemCount: recensioni.length,
      itemBuilder: (context, i) {
        final r = recensioni[i];
        return ListTile(
          title: Text(r.titoloLibro),
          subtitle: Text(r.testo),
          trailing: Icon(r.pubblica ? Icons.public : Icons.lock),
        );
      },
    );
  }
}