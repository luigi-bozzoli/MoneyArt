package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di un'Asta.
 */
public interface AstaDao extends GenericDao<Asta> {

  // TODO: Aggiungere qualche operazione che recupera le aste
  // in base alle date di inizio e di fine

  /**
   * Restituisce tutte le aste che si trovano nello stato specificato.
   *
   * @param s lo stato in cui si devono trovare le aste cercate
   * @return tutte le aste che si trovano nello stato s
   */
  List<Asta> doRetrieveByStato(Asta.Stato s);

  /**
   * Restituisce le aste associate ad una specifica opera.
   *
   * @param id l'identificativo dell'opera di cui si vogliono recuperare le aste
   * @return le aste associate all'opera specificata
   */
  List<Asta> doRetrieveByOperaId(int id);
}
