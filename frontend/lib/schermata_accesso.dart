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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('BookNest')),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: controlloUsername,
              decoration: const InputDecoration(labelText: 'Username'),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: controlloPassword,
              decoration: const InputDecoration(labelText: 'Password'),
              obscureText: true,
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: inCorso ? null : accedi,
              child: const Text('Accedi'),
            ),
          ],
        ),
      ),
    );
  }
}