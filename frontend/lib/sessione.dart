/// Identificativo dell'utente attualmente autenticato.
///
/// Vale `null` finché non viene effettuato l'accesso. Le schermate lo leggono
/// per popolare l'header `X-Utente-Id` delle richieste al backend.
///
/// La sessione è mantenuta in memoria e si perde al ricaricamento della pagina:
/// una soluzione di produzione conserverebbe un token nel browser.
int? idUtenteCorrente;

/// Username dell'utente autenticato, mostrato nell'interfaccia.
String? usernameCorrente;