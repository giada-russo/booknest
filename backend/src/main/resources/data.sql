-- 1. LIBRI (21 righe, id da 1 a 21)
INSERT INTO libro (titolo, autore, isbn) VALUES ('Cambiare l''acqua ai fiori', 'Valerie Perrin', '9788833571000');
INSERT INTO libro (titolo, autore, isbn) VALUES ('La bugia dell''orchidea', 'Donato Carrisi', '9788830460011');
INSERT INTO libro (titolo, autore, isbn) VALUES ('L''educazione delle farfalle', 'Donato Carrisi', '9788830460028');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Tata', 'Valerie Perrin', '9788833571039');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il quaderno dell''amore perduto', 'Valerie Perrin', '9788868365042');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il suggeritore', 'Donato Carrisi', '9788850218318');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Io che ti ho voluto così bene', 'Roberta Recchia', '9788804780076');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Tutta la vita che resta', 'Roberta Recchia', '9788804780083');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Uno splendido disastro', 'Jamie McGuire', '9788854154438');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Un treno per Marrakesh', 'Dinah Jefferies', '9788822730107');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Circe', 'Madeline Miller', '9788823522271');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Storia di due anime', 'Alex Landragin', '9788811816126');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Matrimonio di Convivenza', 'Felicia Kingsley', '9788822760135');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Il tuo pericoloso sorriso', 'Arianna Mechelli', '9788831450142');
INSERT INTO libro (titolo, autore, isbn) VALUES ('L''impostore', 'Martin Griffin', '9788820075150');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Central Park', 'Guillaume Musso', '9788845277163');
INSERT INTO libro (titolo, autore, isbn) VALUES ('La donna dei fiori di carta', 'Donato Carrisi', '9788830441170');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Io sono l''abisso', 'Donato Carrisi', '9788830455189');
INSERT INTO libro (titolo, autore, isbn) VALUES ('L''uomo che portava a spasso i libri', 'Carsten Henn', '9788828210196');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Una di famiglia', 'Freida McFadden', '9788854199201');
INSERT INTO libro (titolo, autore, isbn) VALUES ('Chi ha peccato', 'Anna Bailey', '9788868367219');

-- 2. UTENTI (4, con hash BCrypt valido a 60 caratteri)
INSERT INTO utente (nome, cognome, username, email, password_hash) VALUES
                                                                       ('Romina', 'Battista', 'romibat27', 'romibat@gmail.com', '$2a$10$Y8Nikv2c9b6f1DnDjRMq7.nuJJ6VB.Pe5Cu7TbOltg9QNlZWxsnV2'),
                                                                       ('Marco', 'Rossi', 'marcoros', 'marco@gmail.com', '$2a$10$Y8Nikv2c9b6f1DnDjRMq7.nuJJ6VB.Pe5Cu7TbOltg9QNlZWxsnV2'),
                                                                       ('Anna', 'Verdi', 'annav', 'anna@gmail.com', '$2a$10$Y8Nikv2c9b6f1DnDjRMq7.nuJJ6VB.Pe5Cu7TbOltg9QNlZWxsnV2'),
                                                                       ('Luca', 'Bianchi', 'lucab', 'luca@gmail.com', '$2a$10$Y8Nikv2c9b6f1DnDjRMq7.nuJJ6VB.Pe5Cu7TbOltg9QNlZWxsnV2');

-- 3. CATALOGAZIONI (voto e data solo per lo stato LETTO)
INSERT INTO catalogazione (utente_id, libro_id, stato, voto, data_completamento) VALUES
                                                                                     -- Romina
                                                                                     (1, 1, 'LETTO', 5, '2026-07-15 10:00:00'),
                                                                                     (1, 2, 'LETTO', 4, '2026-08-01 10:00:00'),
                                                                                     (1, 3, 'IN_LETTURA', NULL, NULL),
                                                                                     (1, 4, 'LETTO', 5, '2026-08-05 15:30:00'),
                                                                                     (1, 5, 'DA_LEGGERE', NULL, NULL),
                                                                                     (1, 6, 'LETTO', 4, '2026-06-20 18:00:00'),
                                                                                     (1, 7, 'LETTO', 5, '2026-07-10 12:00:00'),
                                                                                     (1, 9, 'LETTO', 3, '2026-05-14 14:00:00'),
                                                                                     (1, 11, 'LETTO', 5, '2026-06-01 09:30:00'),
                                                                                     (1, 13, 'LETTO', 4, '2026-07-22 21:00:00'),
                                                                                     (1, 16, 'IN_LETTURA', NULL, NULL),
                                                                                     (1, 19, 'DA_LEGGERE', NULL, NULL),

                                                                                     -- Marco
                                                                                     (2, 1, 'LETTO', 4, '2026-07-18 11:00:00'),
                                                                                     (2, 2, 'LETTO', 5, '2026-08-03 16:00:00'),
                                                                                     (2, 6, 'LETTO', 5, '2026-06-25 20:15:00'),
                                                                                     (2, 8, 'LETTO', 4, '2026-07-12 11:30:00'),
                                                                                     (2, 10, 'IN_LETTURA', NULL, NULL),
                                                                                     (2, 12, 'LETTO', 3, '2026-05-20 17:00:00'),
                                                                                     (2, 15, 'LETTO', 4, '2026-06-15 14:20:00'),
                                                                                     (2, 17, 'LETTO', 4, '2026-07-30 19:45:00'),
                                                                                     (2, 18, 'LETTO', 5, '2026-08-02 22:10:00'),
                                                                                     (2, 20, 'DA_LEGGERE', NULL, NULL),

                                                                                     -- Anna
                                                                                     (3, 3, 'LETTO', 5, '2026-08-10 15:00:00'),
                                                                                     (3, 4, 'LETTO', 4, '2026-08-12 18:30:00'),
                                                                                     (3, 7, 'LETTO', 5, '2026-07-11 13:40:00'),
                                                                                     (3, 8, 'LETTO', 5, '2026-07-25 10:15:00'),
                                                                                     (3, 9, 'IN_LETTURA', NULL, NULL),
                                                                                     (3, 11, 'LETTO', 4, '2026-06-05 11:00:00'),
                                                                                     (3, 13, 'DA_LEGGERE', NULL, NULL),
                                                                                     (3, 14, 'LETTO', 4, '2026-07-19 16:20:00'),
                                                                                     (3, 16, 'LETTO', 3, '2026-06-28 21:00:00'),
                                                                                     (3, 19, 'LETTO', 5, '2026-08-04 12:00:00'),
                                                                                     (3, 21, 'LETTO', 4, '2026-08-15 17:00:00'),

                                                                                     -- Luca
                                                                                     (4, 1, 'LETTO', 5, '2026-07-20 09:00:00'),
                                                                                     (4, 5, 'LETTO', 4, '2026-07-28 14:10:00'),
                                                                                     (4, 6, 'LETTO', 4, '2026-06-22 19:30:00'),
                                                                                     (4, 10, 'LETTO', 3, '2026-06-10 16:00:00'),
                                                                                     (4, 11, 'LETTO', 5, '2026-06-08 10:45:00'),
                                                                                     (4, 14, 'IN_LETTURA', NULL, NULL),
                                                                                     (4, 15, 'LETTO', 3, '2026-06-18 11:15:00'),
                                                                                     (4, 17, 'LETTO', 4, '2026-08-01 20:00:00'),
                                                                                     (4, 18, 'LETTO', 4, '2026-08-05 13:20:00'),
                                                                                     (4, 20, 'LETTO', 5, '2026-08-14 15:30:00'),
                                                                                     (4, 21, 'IN_LETTURA', NULL, NULL);

-- 4. RECENSIONI (unico blocco valido, con pubblica e apostrofi corretti)
INSERT INTO recensione (utente_id, libro_id, testo, data_creazione, pubblica) VALUES
                                                                                  (1, 1, 'Un capolavoro assoluto della letteratura. Toccante e necessario.', '2026-07-15 10:30:00', true),
                                                                                  (1, 2, 'Carrisi non delude mai, ritmo serrato e colpi di scena continui.', '2026-08-01 10:30:00', true),
                                                                                  (2, 2, 'Tensione alta dall''inizio alla fine, consigliatissimo.', '2026-08-03 16:30:00', true),
                                                                                  (2, 6, 'Il romanzo che ha consacrato Carrisi. Geniale la figura del suggeritore.', '2026-06-25 21:00:00', false),
                                                                                  (3, 3, 'Una storia delicata che sa arrivare dritta al cuore.', '2026-08-10 15:30:00', true),
                                                                                  (3, 8, 'Un esordio pazzesco, emozionante e struggente.', '2026-07-25 11:00:00', true),
                                                                                  (4, 1, 'Letto tutto d''un fiato. Una scrittura che lascia il segno.', '2026-07-20 09:30:00', true),
                                                                                  (4, 20, 'Thriller domestico tesissimo, impossibile staccarsi dalle pagine.', '2026-08-14 16:00:00', true);

-- 5. LIKE AI LIBRI
INSERT INTO like_libro (utente_id, libro_id) VALUES
                                                 (1, 1),
                                                 (1, 2),
                                                 (2, 1),
                                                 (3, 3),
                                                 (4, 1);

-- 6. LIKE ALLE RECENSIONI
INSERT INTO like_recensione (utente_id, recensione_id) VALUES
                                                           (2, 1),
                                                           (3, 1),
                                                           (4, 1),
                                                           (1, 3),
                                                           (3, 3),
                                                           (1, 6);

-- 7. SHOWDOWN / SFIDA (colonne corrette)
INSERT INTO showdown (libroa_id, librob_id, data_creazione, attivo) VALUES
                                                                        (3, 4, CURRENT_TIMESTAMP, true),
                                                                        (6, 11, CURRENT_TIMESTAMP, true);