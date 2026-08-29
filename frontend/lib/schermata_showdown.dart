import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'showdown.dart';
import 'sessione.dart';

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

  Future<void> caricaSondaggi() async {
    try {
      final risposta = await http.get(Uri.parse('http://localhost:8080/api/showdown/attivi'));
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

  Future<void> caricaRisultati() async {
    try {
      for (var s in sondaggi) {
        final risposta = await http.get(
          Uri.parse('http://localhost:8080/api/showdown/${s.id}/risultati'),
        );
        final Map<String, dynamic> dati = jsonDecode(risposta.body);

        risultati[s.id] = {
          'A': dati['conteggioA'],
          'B': dati['conteggioB']
        };
      }
      setState(() {});
    } catch (e) {
      // Un eventuale errore qui non impedisce la renderizzazione
      // della lista sondaggi, omettendo solo i relativi conteggi.
    }
  }

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

      if (risposta.statusCode == 200) {
        final Map<String, dynamic> dati = jsonDecode(risposta.body);
        setState(() {
          risultati[idShowdown] = {
            'A': dati['conteggioA'],
            'B': dati['conteggioB']
          };
        });
      } else {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Hai già votato per questo showdown')),
          );
        }
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
                  onPressed: () => vota(sondaggio.id, 'A'),
                  child: const Text('Vota A'),
                ),
                const SizedBox(height: 8.0),
                Text('${risultati[sondaggio.id]?['A'] ?? 0} voti'),
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 12.0),
                  child: Text(
                    'VS',
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18.0),
                  ),
                ),
                Text(
                  '${sondaggio.titoloLibroB} di ${sondaggio.autoreLibroB}',
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 16.0),
                ),
                const SizedBox(height: 8.0),
                ElevatedButton(
                  onPressed: () => vota(sondaggio.id, 'B'),
                  child: const Text('Vota B'),
                ),
                const SizedBox(height: 8.0),
                Text('${risultati[sondaggio.id]?['B'] ?? 0} voti'),
              ],
            ),
          ),
        );
      },
    );
  }
}