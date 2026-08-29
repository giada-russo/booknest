import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'catalogazione.dart';
import 'schermata_showdown.dart';
import 'sessione.dart';

/// Mostra il diario di lettura dell'utente: le letture completate,
/// ordinate dalla più recente.
///
/// Il diario riusa le catalogazioni con stato LETTO: la data di completamento
/// viene valorizzata automaticamente al momento della transizione.
class SchermataDiario extends StatefulWidget {
  const SchermataDiario({super.key});

  @override
  State<SchermataDiario> createState() => _StatoSchermataDiario();
}

class _StatoSchermataDiario extends State<SchermataDiario> {
  List<Catalogazione> letture = [];
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaDiario();
  }

  Future<void> caricaDiario() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/catalogazioni/diario'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        letture = dati.map((json) => Catalogazione.fromJson(json)).toList();
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
      return const Center(child: Text('Impossibile caricare il diario'));
    }
    if (letture.isEmpty) {
      return const Center(child: Text('Nessuna lettura completata'));
    }
    return ListView.builder(
      itemCount: letture.length,
      itemBuilder: (context, i) {
        final l = letture[i];
        return ListTile(
          title: Text(l.titoloLibro),
          subtitle: Text(l.autoreLibro),
          trailing: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              if (l.dataCompletamento != null)
                Text(l.dataCompletamento!.substring(0, 10)),
              if (l.voto != null) Text('★ ${l.voto}'),
            ],
          ),
        );
      },
    );
  }
}