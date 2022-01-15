package it.unisa.c02.moneyart.gestione.avvisi.notifica.service;

import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.util.List;
import javax.inject.Inject;

/**
 * Questa classe implementa i metodi dell'interfaccia NotificaService.
 */
public class NotificaServiceImpl implements NotificaService {

  public NotificaServiceImpl() {

  }

  /**
   * Costruttore che istanzia tramite il Retriver i dao.
   */


  /**
   * Costruttore con parametri.
   *
   * @param notificaDao dao di notifica
   */
  public NotificaServiceImpl( NotificaDao notificaDao) {
    this.notificaDao = notificaDao;
  }

  /**
   * Restituisce tutte le notifiche di un utente.
   *
   * @param idUtente id dell'utente interessato alle notifiche
   * @return List di notifiche
   */
  @Override
  public List<Notifica> getNotificationsByUser(int idUtente) {

    List<Notifica> notifies = notificaDao.doRetrieveAllByUserId(idUtente);

    return notifies;
  }

  /**
   * Restituisce la notifica.
   *
   * @param id l'identificativo della notifica da restituire
   * @return la notifica trovata
   */
  @Override
  public Notifica getNotification(int id) {
    return notificaDao.doRetrieveById(id);
  }

  /**
   * Imposta il parametro "letta" a true.
   *
   * @param notifica notifica da impostare come letta
   */
  @Override
  public void readNotification(Notifica notifica) {
    notifica = notificaDao.doRetrieveById(notifica.getId());
    notifica.setLetta(true);

    notificaDao.doUpdate(notifica);
  }

  /**
   * Imposta il parametro "letta" a false.
   *
   * @param notifica notifica da impostare come non letta
   */
  @Override
  public void unreadNotification(Notifica notifica) {
    notifica = notificaDao.doRetrieveById(notifica.getId());
    notifica.setLetta(false);

    notificaDao.doUpdate(notifica);
  }

  /**
   * Elimina una notifca.
   *
   * @param notifica notifica da eliminare
   */
  @Override
  public void deleteNotification(Notifica notifica) {
    notifica = notificaDao.doRetrieveById(notifica.getId());
    notificaDao.doDelete(notifica);
  }

  /**
   * Aggiunge una nuova notifica.
   *
   * @param notifica notifica da aggiungere
   */
  @Override
  public void addNotification(Notifica notifica) {
    notificaDao.doCreate(notifica);
  }

  @Inject
  private UtenteDao utenteDao;

  @Inject
  private NotificaDao notificaDao;

}