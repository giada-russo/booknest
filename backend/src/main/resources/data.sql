INSERT INTO libro (titolo, autore, isbn) VALUES ('Se questo è un uomo', 'Primo Levi', '9788806219352');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il nome della rosa', 'Umberto Eco', '9788845292613');
INSERT INTO libro (titolo, autore, isbn) VALUES ('La coscienza di Zeno', 'Italo Svevo', '9788807900105');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il barone rampante', 'Italo Calvino', '9788804668237');

INSERT INTO utente (id, nome, cognome, username, email, password_hash) VALUES (1, 'Romina', 'Battista', 'romibat27', 'romibat@gmail.com', '1234');

INSERT INTO showdown (id, libroa_id, librob_id, data_creazione, attivo) VALUES (1, 1, 2, CURRENT_TIMESTAMP, true);
INSERT INTO showdown (id, libroa_id, librob_id, data_creazione, attivo) VALUES (2, 3, 4, CURRENT_TIMESTAMP, true);