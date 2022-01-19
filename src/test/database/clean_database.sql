# Elimina i follow (per evitare che la DELETE successiva possa fallire)
UPDATE moneyart.utente SET id_seguito = NULL WHERE id > 0;

DELETE FROM moneyart.segnalazione WHERE id > 0;
DELETE FROM moneyart.notifica WHERE id > 0;
DELETE FROM moneyart.partecipazione WHERE id > 0;
DELETE FROM moneyart.rivendita WHERE id > 0;
DELETE FROM moneyart.asta WHERE id > 0;
DELETE FROM moneyart.opera WHERE id > 0;
DELETE FROM moneyart.utente WHERE id > 0;

ALTER TABLE moneyart.segnalazione AUTO_INCREMENT = 1;
ALTER TABLE moneyart.notifica AUTO_INCREMENT = 1;
ALTER TABLE moneyart.partecipazione AUTO_INCREMENT = 1;
ALTER TABLE moneyart.rivendita AUTO_INCREMENT = 1;
ALTER TABLE moneyart.asta AUTO_INCREMENT = 1;
ALTER TABLE moneyart.opera AUTO_INCREMENT = 1;
ALTER TABLE moneyart.utente AUTO_INCREMENT = 1;