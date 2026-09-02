import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'sessione.dart';

/// Schermata di accesso e registrazione.
///
/// È la prima schermata mostrata all'avvio: finché non viene effettuato
/// l'accesso, le sezioni dell'applicazione non sono raggiungibili.
class SchermataAccesso extends StatefulWidget {
  /// Invocata ad accesso riuscito, per mostrare l'applicazione.
  final VoidCallback alAccessoRiuscito;

  const SchermataAccesso({super.key, required this.alAccessoRiuscito});

  @override
  State<SchermataAccesso> createState() => _StatoSchermataAccesso();
}

class _StatoSchermataAccesso extends State<SchermataAccesso> {
  final TextEditingController controlloUsername = TextEditingController();
  final TextEditingController controlloPassword = TextEditingController();
  final TextEditingController controlloNome = TextEditingController();
  final TextEditingController controlloCognome = TextEditingController();
  final TextEditingController controlloEmail = TextEditingController();
  bool modalitaRegistrazione = false;
  bool inCorso = false;

  /// Autentica l'utente e ne memorizza l'identificativo nella sessione.
  Future<void> accedi() async {
    setState(() => inCorso = true);

    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/utenti/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'username': controlloUsername.text,
          'password': controlloPassword.text,
        }),
      );

      if (risposta.statusCode == 200) {
        final dati = jsonDecode(risposta.body);
        idUtenteCorrente = dati['id'];
        usernameCorrente = dati['username'];
        widget.alAccessoRiuscito();
      } else if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(risposta.body)),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Impossibile contattare il server')),
        );
      }
    }

    if (mounted) setState(() => inCorso = false);
  }

  /// Registra un nuovo utente e ne apre immediatamente la sessione.
  ///
  /// A registrazione riuscita il backend restituisce l'utente creato con il
  /// suo identificativo: l'accesso è quindi automatico.
  Future<void> registrati() async {
    setState(() => inCorso = true);

    try {
      final risposta = await http.post(
        Uri.parse('http://localhost:8080/api/utenti/registrazione'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'nome': controlloNome.text,
          'cognome': controlloCognome.text,
          'username': controlloUsername.text,
          'email': controlloEmail.text,
          'password': controlloPassword.text,
        }),
      );

      if (risposta.statusCode == 200) {
        final dati = jsonDecode(risposta.body);
        idUtenteCorrente = dati['id'];
        usernameCorrente = dati['username'];
        widget.alAccessoRiuscito();
      } else if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(risposta.body)),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Impossibile contattare il server')),
        );
      }
    }

    if (mounted) setState(() => inCorso = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('BookNest')),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 420),
            child: Card(
              elevation: 2,
              color: Colors.white,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: const EdgeInsets.all(32),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(
                      Icons.menu_book,
                      size: 56,
                      color: Color(0xFF8A9A7B),
                    ),
                    const SizedBox(height: 16),
                    Text(
                      modalitaRegistrazione
                          ? 'Crea un account'
                          : 'Accedi a BookNest',
                      style: Theme.of(context).textTheme.headlineSmall,
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 24),
                    if (modalitaRegistrazione) ...[
                      TextField(
                        controller: controlloNome,
                        decoration: const InputDecoration(
                          labelText: 'Nome',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 12),
                      TextField(
                        controller: controlloCognome,
                        decoration: const InputDecoration(
                          labelText: 'Cognome',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 12),
                      TextField(
                        controller: controlloEmail,
                        decoration: const InputDecoration(
                          labelText: 'Email',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 12),
                    ],
                    TextField(
                      controller: controlloUsername,
                      decoration: const InputDecoration(
                        labelText: 'Username',
                        border: OutlineInputBorder(),
                      ),
                    ),
                    const SizedBox(height: 12),
                    TextField(
                      controller: controlloPassword,
                      decoration: const InputDecoration(
                        labelText: 'Password',
                        border: OutlineInputBorder(),
                      ),
                      obscureText: true,
                    ),
                    const SizedBox(height: 24),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF9B8AA6),
                          foregroundColor: Colors.white,
                          padding: const EdgeInsets.symmetric(vertical: 14),
                        ),
                        onPressed: inCorso
                            ? null
                            : (modalitaRegistrazione ? registrati : accedi),
                        child: Text(
                          modalitaRegistrazione ? 'Registrati' : 'Accedi',
                        ),
                      ),
                    ),
                    TextButton(
                      onPressed: () => setState(
                            () => modalitaRegistrazione = !modalitaRegistrazione,
                      ),
                      child: Text(modalitaRegistrazione
                          ? 'Hai già un account? Accedi'
                          : 'Non hai un account? Registrati'),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}