package it.unisa.c02.moneyart.gestione.avvisi.notifica.service;

import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.List;

public interface NotificaService {

  List<Notifica> getNotificationsByUser(int idUtente);

  Notifica getNotification(int id);

  void readNotification (Notifica notifica);

  void unreadNotification (Notifica notifica);

  void deleteNotification  (Notifica notifica);

  void addNotification (Notifica notifica);

}
