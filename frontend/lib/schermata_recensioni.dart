import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'recensione.dart';
import 'catalogazione.dart';
import 'schermata_showdown.dart';
import 'sessione.dart';

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

  List<Catalogazione> libreria = [];
  int? idLibroScelto;
  final TextEditingController controlloTesto = TextEditingController();
  bool pubblica = true;

  @override
  void initState() {
    super.initState();
    caricaRecensioni();
    caricaLibreria();
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

  /// Carica la libreria personale, da cui scegliere il libro da recensire.
  Future<void> caricaLibreria() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/catalogazioni'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        libreria = dati.map((json) => Catalogazione.fromJson(json)).toList();
      });
    } catch (e) {
      // La lista resta vuota: il modulo non mostrerà libri selezionabili.
    }
  }

  /// Invia al backend una nuova recensione per il libro selezionato.
  ///
  /// Se lo stato di lettura non consente la recensione, il backend risponde
  /// 409 e viene mostrato il messaggio dell'eccezione di dominio.
  Future<void> inviaRecensione() async {
    if (idLibroScelto == null || controlloTesto.text.isEmpty) return;
    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/recensioni/$idLibroScelto'),
        headers: {
          'Content-Type': 'application/json',
          'X-Utente-Id': '$idUtenteCorrente',
        },
        body: jsonEncode({
          'testo': controlloTesto.text,
          'pubblica': pubblica,
        }),
      );
      if (risposta.statusCode == 200) {
        controlloTesto.clear();
        idLibroScelto = null;
        await caricaRecensioni();
      } else if (mounted) {
        final messaggio = risposta.statusCode == 409
            ? 'Puoi recensire solo i libri che hai già letto, e una sola volta per libro'
            : 'Non è stato possibile salvare la recensione';
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

  /// Apre il modulo per scrivere una nuova recensione.
  void apriModuloRecensione() {
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, aggiornaDialogo) => AlertDialog(
          title: const Text('Nuova recensione'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              DropdownButton<int>(
                value: idLibroScelto,
                hint: const Text('Scegli un libro'),
                isExpanded: true,
                items: libreria
                    .where((c) => c.stato == 'LETTO')
                    .map((c) => DropdownMenuItem(
                  value: c.idLibro,
                  child: Text(c.titoloLibro),
                ))
                    .toList(),
                onChanged: (id) => aggiornaDialogo(() => idLibroScelto = id),
              ),
              TextField(
                controller: controlloTesto,
                decoration: const InputDecoration(labelText: 'La tua recensione'),
                maxLines: 3,
              ),
              SwitchListTile(
                title: const Text('Pubblica'),
                value: pubblica,
                onChanged: (v) => aggiornaDialogo(() => pubblica = v),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Annulla'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                inviaRecensione();
              },
              child: const Text('Salva'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (caricamento) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errore) {
      return const Center(child: Text('Impossibile caricare le recensioni'));
    }
    return Stack(
      children: [
        if (recensioni.isEmpty)
          const Center(child: Text('Nessuna recensione scritta'))
        else
          ListView.builder(
            itemCount: recensioni.length,
            itemBuilder: (context, i) {
              final r = recensioni[i];
              return ListTile(
                title: Text(r.titoloLibro),
                subtitle: Text(r.testo),
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      r.pubblica ? Icons.public : Icons.lock_outline,
                      size: 16,
                      color: Colors.grey.shade600,
                    ),
                    const SizedBox(width: 4),
                    Text(
                      r.pubblica ? 'Pubblica' : 'Privata',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              );
            },
          ),
        Positioned(
          right: 16,
          bottom: 16,
          child: FloatingActionButton(
            backgroundColor: const Color(0xFF9B8AA6),
            foregroundColor: Colors.white,
            onPressed: apriModuloRecensione,
            child: const Icon(Icons.add),
          ),
        ),
      ],
    );
  }
}