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
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'BookNest',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF8A9A7B),
          primary: const Color(0xFF8A9A7B),
          secondary: const Color(0xFF9B8AA6),
          surface: const Color(0xFFF2EDE4),
        ),
        scaffoldBackgroundColor: const Color(0xFFF2EDE4),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF8A9A7B),
          foregroundColor: Colors.white,
        ),
        bottomNavigationBarTheme: const BottomNavigationBarThemeData(
          backgroundColor: Colors.white,
          selectedItemColor: Color(0xFF9B8AA6),
          unselectedItemColor: Color(0xFF9E9E9E),
        ),
        dropdownMenuTheme: const DropdownMenuThemeData(
          textStyle: TextStyle(color: Color(0xFF2E2E2E)),
        ),
        inputDecorationTheme: const InputDecorationTheme(
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(color: Color(0xFF8A9A7B), width: 2),
          ),
        ),
        useMaterial3: true,
      ),
      home: const SchermataPrincipale(),
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
  /// Implementato come getter per permettere la ricostruzione dinamica di
  /// SchermataShowdown al cambio di utente (tramite ValueKey).
  List<Widget> get sezioniPubbliche => [
    const SchermataLibri(),
    SchermataShowdown(key: ValueKey(idUtenteCorrente)),
    const SchermataClassifica(),
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
    BottomNavigationBarItem(icon: Icon(Icons.people), label: 'Seguiti'),
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

    if (indiceSezione >= sezioni.length) {
      indiceSezione = 0;
    }

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
  String testoRicerca = '';

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

    final query = testoRicerca.toLowerCase();
    final libriFiltrati = query.isEmpty
        ? libri
        : libri
        .where((l) =>
    l.titolo.toLowerCase().contains(query) ||
        l.autore.toLowerCase().contains(query))
        .toList();

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(16),
          child: TextField(
            decoration: InputDecoration(
              hintText: 'Cerca per titolo o autore',
              prefixIcon: const Icon(Icons.search, color: Color(0xFF8A9A7B)),
              filled: true,
              fillColor: Colors.white,
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF8A9A7B)),
              ),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF8A9A7B), width: 1.5),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF8A9A7B), width: 2),
              ),
            ),
            onChanged: (valore) => setState(() => testoRicerca = valore),
          ),
        ),
        Expanded(
          child: libriFiltrati.isEmpty
              ? const Center(child: Text('Nessun libro corrisponde alla ricerca'))
              : ListView.builder(
            itemCount: libriFiltrati.length,
            itemBuilder: (context, i) => ListTile(
              title: Text(libriFiltrati[i].titolo),
              subtitle: Text(libriFiltrati[i].autore),
              trailing: libriFiltrati[i].votoMedio == null
                  ? const Text('—')
                  : Text(
                  '★ ${libriFiltrati[i].votoMedio!.toStringAsFixed(1)}'),
              onTap: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) =>
                      SchermataLibro(libro: libriFiltrati[i]),
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }
}