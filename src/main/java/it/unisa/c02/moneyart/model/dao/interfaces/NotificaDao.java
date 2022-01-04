package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Notifica;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una Notifica.
 */
public interface NotificaDao extends GenericDao<Notifica> {

  /**
   * Restituisce tutte le notifiche destinate ad uno specifico utente.
   *
   * @param id l'id del destinatario delle notifiche
   * @return tutte le notifiche destinate a utente
   */
  List<Notifica> doRetrieveAllByUserId(int id);

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica asta.
   *
   * @param id l'id dell'asta a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento ad asta
   */
  List<Notifica> doRetrieveAllByAuctionId(int id);

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica rivendita.
   *
   * @param id l'id della rivendita a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento a rivendita
   */
  List<Notifica> doRetrieveAllByRivenditaId(int id);
}
