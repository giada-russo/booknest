import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'sessione.dart';

/// Elenco delle persone seguite dall'utente, con la possibilità di smettere
/// di seguirle.
class SchermataUtenti extends StatefulWidget {
  const SchermataUtenti({super.key});

  @override
  State<SchermataUtenti> createState() => _StatoSchermataUtenti();
}

class _StatoSchermataUtenti extends State<SchermataUtenti> {
  List<dynamic> utenti = [];
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaDati();
  }

  /// Carica l'elenco delle persone seguite dall'utente.
  Future<void> caricaDati() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/utenti/seguiti'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );

      setState(() {
        utenti = jsonDecode(risposta.body);
        caricamento = false;
      });
    } catch (e) {
      setState(() {
        caricamento = false;
        errore = true;
      });
    }
  }

  /// Interrompe il follow verso l'utente indicato.
  Future<void> smettiDiSeguire(int idUtente) async {

    final indirizzo =
    Uri.parse('http://localhost:8080/api/utenti/seguiti/$idUtente');
    final intestazioni = {'X-Utente-Id': '$idUtenteCorrente'};

    try {
      final risposta = await http.delete(indirizzo, headers: intestazioni);

      if (!mounted) return;

      if (risposta.statusCode == 200) {
        await caricaDati();
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Non è stato possibile completare l\'operazione')),
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
      return const Center(child: Text('Impossibile caricare gli utenti seguiti'));
    }
    if (utenti.isEmpty) {
      return const Center(child: Text('Nessun utente seguito'));
    }
    return ListView.builder(
      itemCount: utenti.length,
      itemBuilder: (context, i) {
        final u = utenti[i];
        final id = u['id'] as int;

        return ListTile(
          leading: const Icon(Icons.person),
          title: Text(u['username']),
          trailing: TextButton(
            onPressed: () => smettiDiSeguire(id),
            child: const Text('Smetti di seguire'),
          ),
        );
      },
    );
  }
}