INSERT INTO libro (titolo, autore, isbn) VALUES ('Se questo è un uomo', 'Primo Levi', '9788806219352');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il nome della rosa', 'Umberto Eco', '9788845292613');
INSERT INTO libro (titolo, autore, isbn) VALUES ('La coscienza di Zeno', 'Italo Svevo', '9788807900105');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il barone rampante', 'Italo Calvino', '9788804668237');

INSERT INTO utente (nome, cognome, username, email, password_hash) VALUES ('Romina', 'Battista', 'romibat27', 'romibat@gmail.com', '$2a$10$d./xarWE.2IL.cDdMqX6uOZzaspcL023xPnYKVk9pwRCtWyuF0kua');
INSERT INTO showdown (libroa_id, librob_id, data_creazione, attivo) VALUES (1, 2, CURRENT_TIMESTAMP, true);
INSERT INTO showdown (libroa_id, librob_id, data_creazione, attivo) VALUES (3, 4, CURRENT_TIMESTAMP, true);