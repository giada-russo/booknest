import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'catalogazione.dart';
import 'schermata_showdown.dart';
import 'sessione.dart';

/// Mostra la libreria personale dell'utente e consente di cambiare
/// lo stato di lettura di ciascun libro.
///
/// Le transizioni non consentite vengono rifiutate dal backend, che applica
/// le regole del pattern State: in quel caso viene mostrato un messaggio.
class SchermataLibreria extends StatefulWidget {
  const SchermataLibreria({super.key});

  @override
  State<SchermataLibreria> createState() => _StatoSchermataLibreria();
}

class _StatoSchermataLibreria extends State<SchermataLibreria> {
  List<Catalogazione> catalogazioni = [];
  bool caricamento = true;
  bool errore = false;

  /// Gli stati di lettura previsti dal dominio.
  static const List<String> statiPossibili = [
    'DA_LEGGERE',
    'IN_LETTURA',
    'LETTO',
    'ABBANDONATO',
  ];

  @override
  void initState() {
    super.initState();
    caricaLibreria();
  }

  Future<void> caricaLibreria() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/catalogazioni'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        catalogazioni = dati.map((json) => Catalogazione.fromJson(json)).toList();
        caricamento = false;
      });
    } catch (e) {
      setState(() {
        caricamento = false;
        errore = true;
      });
    }
  }

  /// Richiede al backend il cambio di stato di un libro catalogato.
  ///
  /// Se la transizione non è consentita il backend risponde 409 e viene
  /// mostrato il messaggio dell'eccezione di dominio.
  Future<void> cambiaStato(int idLibro, String nuovoStato) async {
    try {
      final risposta = await http.put(
        Uri.parse('http://localhost:8080/api/catalogazioni/$idLibro/stato'),
        headers: {
          'Content-Type': 'application/json',
          'X-Utente-Id': '$idUtenteCorrente',
        },
        body: jsonEncode({'nuovoStato': nuovoStato}),
      );

      if (risposta.statusCode == 200) {
        await caricaLibreria();
      } else if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(risposta.body)),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Errore di connessione')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (caricamento) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errore) {
      return const Center(child: Text('Impossibile caricare la libreria'));
    }
    if (catalogazioni.isEmpty) {
      return const Center(child: Text('Nessun libro in libreria'));
    }
    return ListView.builder(
      itemCount: catalogazioni.length,
      itemBuilder: (context, i) {
        final c = catalogazioni[i];
        return ListTile(
          title: Text(c.titoloLibro),
          subtitle: Text('${c.autoreLibro} · ${c.stato}'),
          trailing: DropdownButton<String>(
            value: c.stato,
            items: statiPossibili
                .map((s) => DropdownMenuItem(value: s, child: Text(s)))
                .toList(),
            onChanged: (nuovo) {
              if (nuovo != null) cambiaStato(c.idLibro, nuovo);            },
          ),
        );
      },
    );
  }
}