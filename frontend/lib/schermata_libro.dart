import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'libro.dart';
import 'recensione.dart';
import 'schermata_showdown.dart';
import 'sessione.dart';

/// Scheda di dettaglio di un libro.
///
/// Mostra i dati bibliografici, la media dei voti, gli apprezzamenti ricevuti,
/// le recensioni pubbliche e i libri suggeriti sulla base delle catalogazioni
/// degli altri utenti.
class SchermataLibro extends StatefulWidget {
  final Libro libro;
  const SchermataLibro({super.key, required this.libro});

  @override
  State<SchermataLibro> createState() => _StatoSchermataLibro();
}

class _StatoSchermataLibro extends State<SchermataLibro> {
  int conteggioLike = 0;
  bool likeMesso = false;
  bool inCatalogazione = false;
  List<Recensione> recensioni = [];
  List<Libro> simili = [];
  Map<int, int> conteggiLikeRecensioni = {};
  Map<int, bool> likeMessiRecensioni = {};

  @override
  void initState() {
    super.initState();
    caricaConteggioLike();
    caricaStatoLike();
    caricaRecensioni();
    caricaSimili();
  }

  /// Chiede al backend quanti apprezzamenti ha ricevuto il libro.
  Future<void> caricaConteggioLike() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/like/libri/${widget.libro.id}/conteggio'),
      );
      setState(() => conteggioLike = int.parse(risposta.body));
    } catch (e) {
      // Il conteggio resta a zero: la scheda si mostra comunque.
    }
  }

  /// Chiede al backend se l'utente ha già espresso un apprezzamento su questo libro.
  Future<void> caricaStatoLike() async {
    if (idUtenteCorrente == null) return;
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/like/libri/${widget.libro.id}/mio'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      setState(() => likeMesso = risposta.body == 'true');
    } catch (e) {
      // Lo stato resta "non messo": il pulsante è comunque utilizzabile.
    }
  }

  /// Carica le recensioni rese pubbliche dagli utenti su questo libro.
  ///
  /// Endpoint pubblico: non richiede l'identificativo dell'utente.
  Future<void> caricaRecensioni() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/recensioni/libro/${widget.libro.id}'),
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        recensioni = dati.map((json) => Recensione.fromJson(json)).toList();
      });
      await caricaLikeRecensioni();
    } catch (e) {
      // La lista resta vuota: la scheda si mostra comunque.
    }
  }

  /// Carica conteggio e stato del like per ciascuna recensione mostrata.
  Future<void> caricaLikeRecensioni() async {
    if (idUtenteCorrente == null) return;
    for (final r in recensioni) {
      try {
        final conteggio = await http.get(
          Uri.parse('http://localhost:8080/api/like/recensioni/${r.id}/conteggio'),
        );
        final mio = await http.get(
          Uri.parse('http://localhost:8080/api/like/recensioni/${r.id}/mio'),
          headers: {'X-Utente-Id': '$idUtenteCorrente'},
        );
        setState(() {
          conteggiLikeRecensioni[r.id] = int.parse(conteggio.body);
          likeMessiRecensioni[r.id] = mio.body == 'true';
        });
      } catch (e) {
        // I dati della singola recensione restano assenti: le altre si mostrano comunque.
      }
    }
  }

  /// Carica i libri suggeriti sulla base delle catalogazioni degli altri utenti.
  ///
  /// Endpoint pubblico: la lista è vuota se nessun altro utente ha catalogato
  /// questo libro insieme ad altri.
  Future<void> caricaSimili() async {
    try {
      final risposta = await http.get(
        Uri.parse('http://localhost:8080/api/libri/${widget.libro.id}/simili'),
      );
      final List<dynamic> dati = jsonDecode(risposta.body);
      setState(() {
        simili = dati.map((json) => Libro.fromJson(json)).toList();
      });
    } catch (e) {
      // La lista resta vuota: la scheda si mostra comunque.
    }
  }

  /// Aggiunge o rimuove l'apprezzamento dell'utente sul libro.
  Future<void> cambiaLike() async {
    final indirizzo =
    Uri.parse('http://localhost:8080/api/like/libri/${widget.libro.id}');
    final intestazioni = {'X-Utente-Id': '$idUtenteCorrente'};
    try {
      final risposta = likeMesso
          ? await http.delete(indirizzo, headers: intestazioni)
          : await http.post(indirizzo, headers: intestazioni);
      if (risposta.statusCode == 200) {
        setState(() => likeMesso = !likeMesso);
        await caricaConteggioLike();
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

  /// Aggiunge il libro alla libreria personale dell'utente.
  ///
  /// Il backend crea la catalogazione nello stato iniziale DA_LEGGERE.
  Future<void> cataloga() async {
    setState(() => inCatalogazione = true);
    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/catalogazioni/${widget.libro.id}'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );
      if (!mounted) return;

      String messaggio;
      if (risposta.statusCode == 200) {
        messaggio = 'Aggiunto alla tua libreria';
      } else if (risposta.statusCode == 409) {
        messaggio = 'Questo libro è già nella tua libreria';
      } else {
        messaggio = 'Non è stato possibile aggiungere il libro';
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(messaggio)),
      );
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Errore di connessione')),
        );
      }
    }
    if (mounted) setState(() => inCatalogazione = false);
  }

  /// Aggiunge o rimuove l'apprezzamento dell'utente su una recensione.
  Future<void> cambiaLikeRecensione(int idRecensione) async {
    final indirizzo =
    Uri.parse('http://localhost:8080/api/like/recensioni/$idRecensione');
    final intestazioni = {'X-Utente-Id': '$idUtenteCorrente'};
    final giaMesso = likeMessiRecensioni[idRecensione] ?? false;
    try {
      final risposta = giaMesso
          ? await http.delete(indirizzo, headers: intestazioni)
          : await http.post(indirizzo, headers: intestazioni);
      if (risposta.statusCode == 200) {
        await caricaLikeRecensioni();
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

  /// Chiede conferma e avvia il follow verso l'autore della recensione.
  void chiediDiSeguire(int idAutore, String username) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Seguire $username?'),
        content: const Text(
          'Vedrai questa persona tra gli utenti che segui.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Annulla'),
          ),
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              segui(idAutore, username);
            },
            child: const Text('Segui'),
          ),
        ],
      ),
    );
  }

  /// Registra il follow verso l'autore indicato.
  Future<void> segui(int idAutore, String username) async {
    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/utenti/seguiti/$idAutore'),
        headers: {'X-Utente-Id': '$idUtenteCorrente'},
      );

      if (!mounted) return;

      String messaggio;
      if (risposta.statusCode == 200) {
        messaggio = 'Ora segui $username';
      } else if (risposta.statusCode == 409) {
        messaggio = 'Segui già questa persona';
      } else {
        messaggio = 'Non è stato possibile seguire questa persona';
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(messaggio)),
      );
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
    final libro = widget.libro;
    return Scaffold(
      appBar: AppBar(title: Text(libro.titolo)),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text(libro.titolo, style: Theme.of(context).textTheme.headlineSmall),
          const SizedBox(height: 8),
          Text('di ${libro.autore}'),
          const SizedBox(height: 4),
          Text('ISBN ${libro.isbn}'),
          const SizedBox(height: 4),
          Text(libro.votoMedio == null
              ? 'Nessun voto'
              : 'Voto medio ★ ${libro.votoMedio!.toStringAsFixed(1)}'),
          const SizedBox(height: 16),
          Row(
            children: [
              IconButton(
                icon: Icon(likeMesso ? Icons.favorite : Icons.favorite_border),
                color: const Color(0xFF9B8AA6),
                onPressed: idUtenteCorrente == null ? null : cambiaLike,
                tooltip: idUtenteCorrente == null
                    ? 'Accedi per mettere like'
                    : null,
              ),
              Text('$conteggioLike'),
            ],
          ),
          if (idUtenteCorrente != null) ...[
            const SizedBox(height: 8),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF9B8AA6),
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                ),
                icon: const Icon(Icons.bookmark_add),
                label: const Text('Aggiungi alla mia libreria'),
                onPressed: inCatalogazione ? null : cataloga,
              ),
            ),
          ],
          const Divider(height: 32),
          Text('Recensioni', style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 8),
          if (recensioni.isEmpty)
            const Text('Nessuna recensione pubblica per questo libro')
          else
            ...recensioni.map((r) {
              final idRecensione = r.id;
              final giaMesso = likeMessiRecensioni[idRecensione] ?? false;
              final conteggio = conteggiLikeRecensioni[idRecensione] ?? 0;

              return ListTile(
                title: Text(r.testo),
                subtitle: Text('di ${r.usernameAutore}'),
                onTap: idUtenteCorrente == null
                    ? null
                    : () => chiediDiSeguire(r.idAutore, r.usernameAutore),
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    IconButton(
                      icon: Icon(giaMesso ? Icons.favorite : Icons.favorite_border),
                      color: const Color(0xFF9B8AA6),
                      onPressed: idUtenteCorrente == null
                          ? null
                          : () => cambiaLikeRecensione(idRecensione),
                    ),
                    Text('$conteggio'),
                  ],
                ),
              );
            }),
          const Divider(height: 32),
          Text('Chi ha letto questo libro ha letto anche',
              style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 8),
          if (simili.isEmpty)
            const Text('Nessun suggerimento disponibile')
          else
            ...simili.map((l) => ListTile(
              title: Text(l.titolo),
              subtitle: Text(l.autore),
              onTap: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => SchermataLibro(libro: l),
                ),
              ),
            )),
        ],
      ),
    );
  }
}