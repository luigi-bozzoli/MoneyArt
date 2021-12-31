package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una Notifica.
 */
public interface NotificaDao extends GenericDao<Notifica> {

  /**
   * Restituisce tutte le notifiche destinate ad uno specifico utente.
   *
   * @param utente il destinatario delle notifiche
   * @return tutte le notifiche destinate a utente
   */
  List<Notifica> doRetriveAllByUtente(Utente utente);

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica asta.
   *
   * @param asta l'asta a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento ad asta
   */
  List<Notifica> doRetriveAllByAsta(Asta asta);

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica rivendita.
   *
   * @param rivendita la rivendita a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento a rivendita
   */
  List<Notifica> doRetiveAllByRivendita(Rivendita rivendita);
}
