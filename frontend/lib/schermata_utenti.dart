import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'schermata_showdown.dart';
import 'sessione.dart';

/// Elenco degli altri utenti registrati, con la possibilità di seguirli
/// o smettere di seguirli.
///
/// L'utente stesso non compare nell'elenco: il backend lo esclude, perché
/// seguire se stessi non è consentito dalle regole di dominio.
class SchermataUtenti extends StatefulWidget {
  const SchermataUtenti({super.key});

  @override
  State<SchermataUtenti> createState() => _StatoSchermataUtenti();
}

class _StatoSchermataUtenti extends State<SchermataUtenti> {
  List<dynamic> utenti = [];
  Set<int> idSeguiti = {};
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaDati();
  }

  /// Carica l'elenco degli utenti e quello delle persone già seguite.
  Future<void> caricaDati() async {
    try {
      final intestazioni = {'X-Utente-Id': '$idUtenteCorrente'};

      final rispostaUtenti = await http.get(
        Uri.parse('http://localhost:8080/api/utenti'),
        headers: intestazioni,
      );
      final rispostaSeguiti = await http.get(
        Uri.parse('http://localhost:8080/api/utenti/seguiti'),
        headers: intestazioni,
      );

      setState(() {
        utenti = jsonDecode(rispostaUtenti.body);
        idSeguiti = (jsonDecode(rispostaSeguiti.body) as List)
            .map((u) => u['id'] as int)
            .toSet();
        caricamento = false;
      });
    } catch (e) {
      setState(() {
        caricamento = false;
        errore = true;
      });
    }
  }

  /// Inizia o interrompe il follow verso l'utente indicato.
  Future<void> cambiaFollow(int idUtente) async {
    final indirizzo =
    Uri.parse('http://localhost:8080/api/utenti/seguiti/$idUtente');
    final intestazioni = {'X-Utente-Id': '$idUtenteCorrente'};
    final giaSeguito = idSeguiti.contains(idUtente);

    try {
      final risposta = giaSeguito
          ? await http.delete(indirizzo, headers: intestazioni)
          : await http.post(indirizzo, headers: intestazioni);

      if (risposta.statusCode == 200) {
        await caricaDati();
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
      return const Center(child: Text('Impossibile caricare gli utenti'));
    }
    if (utenti.isEmpty) {
      return const Center(child: Text('Nessun altro utente registrato'));
    }
    return ListView.builder(
      itemCount: utenti.length,
      itemBuilder: (context, i) {
        final u = utenti[i];
        final id = u['id'] as int;
        final seguito = idSeguiti.contains(id);

        return ListTile(
          leading: const Icon(Icons.person),
          title: Text(u['username']),
          trailing: TextButton(
            onPressed: () => cambiaFollow(id),
            child: Text(seguito ? 'Smetti di seguire' : 'Segui'),
          ),
        );
      },
    );
  }
}