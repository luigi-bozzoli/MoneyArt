UPDATE moneyart.utente SET id_seguito = NULL WHERE id > 0;

DELETE FROM segnalazione WHERE id > 0;
DELETE FROM notifica WHERE id > 0;
DELETE FROM partecipazione WHERE id > 0;
DELETE FROM rivendita WHERE id > 0;
DELETE FROM asta WHERE id > 0;
DELETE FROM opera WHERE id > 0;
DELETE FROM utente WHERE id > 0;

ALTER TABLE segnalazione AUTO_INCREMENT = 1;
ALTER TABLE notifica AUTO_INCREMENT = 1;
ALTER TABLE partecipazione AUTO_INCREMENT = 1;
ALTER TABLE rivendita AUTO_INCREMENT = 1;
ALTER TABLE asta AUTO_INCREMENT = 1;
ALTER TABLE opera AUTO_INCREMENT = 1;
ALTER TABLE utente AUTO_INCREMENT = 1;


INSERT INTO utente(id_seguito, email, pwd, username, nome, cognome, foto, saldo)
VALUES (NULL, "alfonso.cannavale@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "alfcan", "Alfonso", "Cannavale", NULL, 1000),
       (NULL, "daniele.galloppo@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "DG266", "Daniele", "Galloppo", NULL, 2500),
       (NULL, "michael.desantis@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "shoyll", "Michael", "De Santis", NULL, 2000);

INSERT INTO opera(id_utente, id_artista, nome, descrizione, immagine, certificato, stato)
VALUES ((SELECT id FROM utente WHERE username = "DG266"), (SELECT id FROM utente WHERE username = "alfcan"), "The Shibosis", "Descrizione", 0x717171, NULL, "IN_POSSESSO");

INSERT INTO asta(id_opera, data_inizio, data_fine, stato)
VALUES((SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan")), CURRENT_DATE() - 8, CURRENT_DATE() - 1, "TERMINATA");

INSERT INTO partecipazione(id_utente, id_asta, offerta)
VALUES((SELECT id FROM utente WHERE username = "shoyll"), (SELECT id FROM asta WHERE id_opera = (SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan"))), 1000),
      ((SELECT id FROM utente WHERE username = "DG266"), (SELECT id FROM asta WHERE id_opera = (SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan"))), 1500);

INSERT INTO notifica(id_utente, id_rivendita, id_asta, letta, tipo, contenuto)
VALUES((SELECT id FROM utente WHERE username = "shoyll"), NULL, (SELECT id FROM asta WHERE id_opera = (SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan"))), FALSE, "SUPERATO", "Contenuto della notifica."),
      ((SELECT id FROM utente WHERE username = "DG266"), NULL, (SELECT id FROM asta WHERE id_opera = (SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan"))), TRUE, "VITTORIA", "Contenuto della notifica.");
