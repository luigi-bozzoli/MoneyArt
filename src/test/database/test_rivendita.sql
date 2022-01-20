/****************************** UTENTI ******************************/

INSERT INTO utente(id_seguito, email, pwd, username, nome, cognome, foto, saldo)
VALUES (NULL, "moneyart@gmail.com", 0x8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918, "admin", "Money", "Art", null, 0),
       (NULL, "alfonso.cannavale@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "alfcan", "Alfonso", "Cannavale", NULL, 1000),
       (NULL, "nicolò.delogu@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "XJustUnluckyX", "Nicolò", "Delogu", NULL, 1500),
       (NULL, "michael.desantis@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "shoyll", "Michael", "De Santis", NULL, 2000),
       (NULL, "daniele.galloppo@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "DG266", "Daniele", "Galloppo", NULL, 2500),
       (NULL, "dario.mazza@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "xDaryamo", "Dario", "Mazza", NULL, 3000),
       (NULL, "mario.peluso@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "MarioPeluso", "Mario", "Peluso", NULL, 956),
       (NULL, "aurelio.sepe@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "AurySepe", "Aurelio", "Sepe", NULL, 200),
       (NULL, "stefano.zarro@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "stepzar", "Stefano", "Zarro", NULL, 10),
       (NULL, "antonio.martucci@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "AntonioMartucci", "Antonio", "Martucci", NULL, 150),
       (NULL, "luigi.bozzoli@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "luigi-bozzoli", "Luigi", "Bozzoli", NULL, 10000),
       (NULL, "leonardo.davinci@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "DaVin", "Leonardo", "da Vinci", NULL, 1337),
       (NULL, "raffaello.sanzio@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "RafSan", "Raffaello", "Sanzio", NULL, 120),
       (NULL, "salvador.dali@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "SalDal", "Salvador", "Dalí", NULL, 5000),
       (NULL, "michelangelo.merisi@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "Caravaggio", "Michelangelo", "Merisi", NULL, 0);




/****************************** OPERE ******************************/

INSERT INTO opera(id_utente, id_artista, nome, descrizione, immagine, certificato, stato)
VALUES ((SELECT id FROM utente WHERE username = "XJustUnluckyX"), (SELECT id FROM utente WHERE username = "alfcan"), "The Shibosis", "Descrizione", 0xFFFF, NULL, "IN_POSSESSO"),
       ((SELECT id FROM utente WHERE username = "alfcan"), (SELECT id FROM utente WHERE username = "alfcan"), "Bears Deluxe #3742", "Descrizione", 0xFFFF, NULL, "ALL_ASTA"),
       ((SELECT id FROM utente WHERE username = "DG266"), (SELECT id FROM utente WHERE username = "DG266"), "CupCat", "Descrizione", 0xFFFF, NULL, "PREVENDITA"),
       ((SELECT id FROM utente WHERE username = "xDaryamo"), (SELECT id FROM utente WHERE username = "xDaryamo"), "TIGXR", "Descrizione", 0xFFFF, NULL, "PREVENDITA"),
       ((SELECT id FROM utente WHERE username = "MarioPeluso"), (SELECT id FROM utente WHERE username = "MarioPeluso"), "Bears Deluxe #3742", "Descrizione", 0xFFFF, NULL, "ALL_ASTA"),
       ((SELECT id FROM utente WHERE username = "shoyll"), (SELECT id FROM utente WHERE username = "xDaryamo"), "Capsule House", "Descrizione", 0xFFFF, NULL, "IN_POSSESSO"),
       ((SELECT id FROM utente WHERE username = "AurySepe"), (SELECT id FROM utente WHERE username = "AurySepe"), "Kumo Resident", "Descrizione", 0xFFFF, null, "ALL_ASTA");




/****************************** ASTE ******************************/

INSERT INTO asta(id_opera, data_inizio, data_fine, stato)
VALUES((SELECT id FROM opera WHERE nome = "The Shibosis" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan")), CURRENT_DATE() - 8, CURRENT_DATE() - 1, "TERMINATA"),
      ((SELECT id FROM opera WHERE nome = "Bears Deluxe #3742" AND id_artista = (SELECT id FROM utente WHERE username = "alfcan")), CURRENT_DATE() - 1, CURRENT_DATE + 6, "IN_CORSO"),
      ((SELECT id FROM opera WHERE nome = "Bears Deluxe #3742" AND id_artista = (SELECT id FROM utente WHERE username = "MarioPeluso")), CURRENT_DATE(), CURRENT_DATE() + 7, "IN_CORSO"),
      ((SELECT id FROM opera WHERE nome = "Kumo Resident" AND id_artista = (SELECT id FROM utente WHERE username = "AurySepe")), CURRENT_DATE(), CURRENT_DATE() + 7, "IN_CORSO");

