import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'libro.dart';
import 'sessione.dart';
import 'schermata_accesso.dart';
import 'schermata_showdown.dart';
import 'schermata_classifica.dart';
import 'schermata_libreria.dart';
import 'schermata_diario.dart';
import 'schermata_recensioni.dart';
import 'schermata_libro.dart';
import 'schermata_utenti.dart';

void main() {
  runApp(const BookNestApp());
}

class BookNestApp extends StatelessWidget {
  const BookNestApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'BookNest',
      home: SchermataPrincipale(),
    );
  }
}

/// Contenitore principale dell'applicazione.
///
/// Gestisce la navigazione tra le sezioni tramite una barra inferiore:
/// il contenuto mostrato dipende dall'indice della sezione selezionata.
class SchermataPrincipale extends StatefulWidget {
  const SchermataPrincipale({super.key});

  @override
  State<SchermataPrincipale> createState() => _StatoSchermataPrincipale();
}

class _StatoSchermataPrincipale extends State<SchermataPrincipale> {
  int indiceSezione = 0;

  /// Sezioni accessibili anche senza autenticazione.
  static const List<Widget> sezioniPubbliche = [
    SchermataLibri(),
    SchermataShowdown(),
    SchermataClassifica(),
  ];

  /// Voci di navigazione corrispondenti alle sezioni pubbliche.
  static const List<BottomNavigationBarItem> vociPubbliche = [
    BottomNavigationBarItem(icon: Icon(Icons.menu_book), label: 'Catalogo'),
    BottomNavigationBarItem(icon: Icon(Icons.how_to_vote), label: 'Showdown'),
    BottomNavigationBarItem(icon: Icon(Icons.leaderboard), label: 'Classifica'),
  ];

  /// Sezioni riservate agli utenti autenticati.
  static const List<Widget> sezioniRiservate = [
    SchermataLibreria(),
    SchermataDiario(),
    SchermataRecensioni(),
    SchermataUtenti(),
  ];

  /// Voci di navigazione corrispondenti alle sezioni riservate.
  static const List<BottomNavigationBarItem> vociRiservate = [
    BottomNavigationBarItem(icon: Icon(Icons.library_books), label: 'Libreria'),
    BottomNavigationBarItem(icon: Icon(Icons.history), label: 'Diario'),
    BottomNavigationBarItem(icon: Icon(Icons.rate_review), label: 'Recensioni'),
    BottomNavigationBarItem(icon: Icon(Icons.people), label: 'Utenti'),
  ];

  /// Apre la schermata di accesso e aggiorna l'interfaccia al rientro.
  void apriAccesso() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => SchermataAccesso(
          alAccessoRiuscito: () {
            Navigator.pop(context);
            setState(() {});
          },
        ),
      ),
    );
  }

  /// Termina la sessione e riporta l'interfaccia allo stato di visitatore.
  void esci() {
    setState(() {
      idUtenteCorrente = null;
      usernameCorrente = null;
      indiceSezione = 0;
    });
  }

  @override
  Widget build(BuildContext context) {
    final autenticato = idUtenteCorrente != null;
    final sezioni = autenticato
        ? [...sezioniPubbliche, ...sezioniRiservate]
        : sezioniPubbliche;
    final voci = autenticato
        ? [...vociPubbliche, ...vociRiservate]
        : vociPubbliche;

    return Scaffold(
      appBar: AppBar(
        title: Text(autenticato ? 'BookNest — $usernameCorrente' : 'BookNest'),
        actions: [
          IconButton(
            icon: Icon(autenticato ? Icons.logout : Icons.login),
            tooltip: autenticato ? 'Esci' : 'Accedi',
            onPressed: autenticato ? esci : apriAccesso,
          ),
        ],
      ),
      body: sezioni[indiceSezione],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: indiceSezione,
        onTap: (indice) => setState(() => indiceSezione = indice),
        type: BottomNavigationBarType.fixed,
        items: voci,
      ),
    );
  }
}

class SchermataLibri extends StatefulWidget {
  const SchermataLibri({super.key});

  @override
  State<SchermataLibri> createState() => _StatoSchermataLibri();
}

class _StatoSchermataLibri extends State<SchermataLibri> {
  List<Libro> libri = [];
  bool caricamento = true;
  bool errore = false;

  @override
  void initState() {
    super.initState();
    caricaLibri();
  }

  /// Carica il catalogo dei libri dal backend.
  ///
  /// In caso di errore di rete o di risposta non valida, interrompe il
  /// caricamento e mostra un messaggio all'utente invece di lasciare
  /// la schermata bianca.
  Future<void> caricaLibri() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/libri'),
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
    if (caricamento) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errore) {
      return const Center(child: Text('Impossibile caricare il catalogo'));
    }
    return ListView.builder(
      itemCount: libri.length,
      itemBuilder: (context, i) => ListTile(
        title: Text(libri[i].titolo),
        subtitle: Text(libri[i].autore),
        trailing: libri[i].votoMedio == null
            ? const Text('—')
            : Text('★ ${libri[i].votoMedio!.toStringAsFixed(1)}'),
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => SchermataLibro(libro: libri[i]),
          ),
        ),
      ),
    );
  }
}