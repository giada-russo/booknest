import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'showdown.dart';
import 'sessione.dart';

/// Schermata che mostra l'elenco dei sondaggi showdown attivi, consentendo
/// agli utenti autenticati di esprimere il proprio voto e a tutti di
/// visualizzare i risultati aggiornati.
class SchermataShowdown extends StatefulWidget {
  const SchermataShowdown({super.key});

  @override
  State<SchermataShowdown> createState() => _StatoSchermataShowdown();
}

class _StatoSchermataShowdown extends State<SchermataShowdown> {
  List<Showdown> sondaggi = [];
  bool caricamento = true;
  Map<int, Map<String, int>> risultati = {};
  String? errore;

  @override
  void initState() {
    super.initState();
    caricaSondaggi();
  }

  /// Carica i sondaggi attivi e, subito dopo, i rispettivi conteggi.
  ///
  /// Endpoint pubblico: la consultazione è consentita anche ai visitatori,
  /// mentre il voto richiede l'autenticazione.
  Future<void> caricaSondaggi() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/showdown/attivi'),
      );
      final List<dynamic> datiJSON = jsonDecode(risposta.body);

      setState(() {
        sondaggi = datiJSON.map((json) => Showdown.fromJson(json)).toList();
        caricamento = false;
      });

      await caricaRisultati();
    } catch (e) {
      setState(() {
        errore = 'Impossibile contattare il server';
        caricamento = false;
      });
    }
  }

  /// Carica i conteggi dei voti per ciascun sondaggio mostrato.
  ///
  /// Esegue una chiamata per sondaggio: su un numero contenuto di showdown
  /// attivi è accettabile, ma andrebbe sostituita da un endpoint aggregato.
  Future<void> caricaRisultati() async {
    try {
      for (var s in sondaggi) {
        final risposta = await http.get(
          Uri.parse('http://localhost:8080/api/showdown/${s.id}/risultati'),
        );
        final Map<String, dynamic> dati = jsonDecode(risposta.body);

        setState(() {
          risultati[s.id] = {
            'A': dati['conteggioA'],
            'B': dati['conteggioB'],
          };
        });
      }
    } catch (e) {
      // Un eventuale errore qui non impedisce la visualizzazione della lista
      // dei sondaggi: vengono omessi soltanto i conteggi mancanti.
    }
  }

  /// Registra il voto dell'utente e aggiorna i conteggi mostrati.
  ///
  /// Se l'utente ha già votato quel sondaggio il backend risponde 409: il voto
  /// è protetto da un lock lato server e da un vincolo di unicità sulla coppia
  /// utente-showdown.
  Future<void> vota(int idShowdown, String scelta) async {
    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/showdown/$idShowdown/voto'),
        headers: {
          'Content-Type': 'application/json',
          'X-Utente-Id': '$idUtenteCorrente',
        },
        body: jsonEncode({'libroScelto': scelta}),
      );

      if (!mounted) return;

      if (risposta.statusCode == 200) {
        final Map<String, dynamic> dati = jsonDecode(risposta.body);
        setState(() {
          risultati[idShowdown] = {
            'A': dati['conteggioA'],
            'B': dati['conteggioB'],
          };
        });
      } else {
        final messaggio = risposta.statusCode == 409
            ? 'Hai già votato in questa sfida, oppure la sfida è chiusa'
            : 'Non è stato possibile registrare il voto';
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(messaggio)),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Errore di connessione durante il voto')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (caricamento) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errore != null) {
      return Center(child: Text(errore!));
    }
    if (sondaggi.isEmpty) {
      return const Center(child: Text('Nessuno showdown attivo'));
    }

    final autenticato = idUtenteCorrente != null;

    return ListView.builder(
      itemCount: sondaggi.length,
      itemBuilder: (context, index) {
        final sondaggio = sondaggi[index];

        return Card(
          margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  '${sondaggio.titoloLibroA} di ${sondaggio.autoreLibroA}',
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 16.0),
                ),
                const SizedBox(height: 8.0),
                ElevatedButton(
                  onPressed:
                  autenticato ? () => vota(sondaggio.id, 'A') : null,
                  child: const Text('Vota A'),
                ),
                const SizedBox(height: 8.0),
                Text('${risultati[sondaggio.id]?['A'] ?? 0} voti'),
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 12.0),
                  child: Text(
                    'VS',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 18.0,
                    ),
                  ),
                ),
                Text(
                  '${sondaggio.titoloLibroB} di ${sondaggio.autoreLibroB}',
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 16.0),
                ),
                const SizedBox(height: 8.0),
                ElevatedButton(
                  onPressed:
                  autenticato ? () => vota(sondaggio.id, 'B') : null,
                  child: const Text('Vota B'),
                ),
                const SizedBox(height: 8.0),
                Text('${risultati[sondaggio.id]?['B'] ?? 0} voti'),
                if (!autenticato) ...[
                  const SizedBox(height: 8),
                  const Text(
                    'Accedi per votare',
                    style: TextStyle(fontSize: 12, color: Colors.grey),
                  ),
                ],
              ],
            ),
          ),
        );
      },
    );
  }
}