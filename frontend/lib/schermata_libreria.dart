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

  /// Etichette leggibili per gli stati di lettura.
  ///
  /// Gli identificativi restano quelli attesi dal backend nelle chiamate.
  static const Map<String, String> etichetteStati = {
    'DA_LEGGERE': 'Da leggere',
    'IN_LETTURA': 'In lettura',
    'LETTO': 'Letto',
    'ABBANDONATO': 'Abbandonato',
  };

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

      if (!mounted) return;

      if (risposta.statusCode == 200) {
        await caricaLibreria();
      } else {
        final messaggio = risposta.statusCode == 409
            ? 'Passaggio di stato non consentito da quello attuale'
            : 'Non è stato possibile aggiornare lo stato';
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(messaggio)),
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

  /// Assegna un voto da 1 a 5 a un libro catalogato.
  ///
  /// Il backend accetta il voto solo se lo stato di lettura lo consente
  /// (pattern State): in caso contrario risponde con un errore.
  Future<void> assegnaVoto(int idLibro, int voto) async {
    try {
      final risposta = await http.put(
        Uri.parse('http://localhost:8080/api/catalogazioni/$idLibro/voto'),
        headers: {
          'Content-Type': 'application/json',
          'X-Utente-Id': '$idUtenteCorrente',
        },
        body: jsonEncode({'voto': voto}),
      );
      if (!mounted) return;

      if (risposta.statusCode == 200) {
        await caricaLibreria();
      } else {
        final messaggio = risposta.statusCode == 409
            ? 'Puoi votare solo i libri che hai già letto'
            : 'Non è stato possibile assegnare il voto';
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(messaggio)),
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
          subtitle: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('${c.autoreLibro} · ${etichetteStati[c.stato] ?? c.stato}'),
              if (c.stato == 'LETTO')
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: List.generate(5, (i) {
                    final valore = i + 1;
                    return IconButton(
                      padding: EdgeInsets.zero,
                      constraints: const BoxConstraints(),
                      icon: Icon(
                        (c.voto != null && valore <= c.voto!)
                            ? Icons.star
                            : Icons.star_border,
                        size: 20,
                        color: const Color(0xFF9B8AA6), // Aggiunto un tocco di colore alle stelle per coerenza
                      ),
                      onPressed: () => assegnaVoto(c.idLibro, valore),
                    );
                  }),
                ),
            ],
          ),
          trailing: Container(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: const Color(0xFF8A9A7B), width: 1.5),
            ),
            child: DropdownButton<String>(
              value: c.stato,
              underline: const SizedBox(),
              icon: const Icon(Icons.expand_more,
                  color: Color(0xFF8A9A7B), size: 20),
              style: const TextStyle(
                color: Color(0xFF2E2E2E),
                fontSize: 14,
              ),
              items: statiPossibili
                  .map((s) => DropdownMenuItem(
                value: s,
                child: Text(etichetteStati[s] ?? s),
              ))
                  .toList(),
              onChanged: (nuovo) {
                if (nuovo != null) cambiaStato(c.idLibro, nuovo);
              },
            ),
          ),
        );
      },
    );
  }
}