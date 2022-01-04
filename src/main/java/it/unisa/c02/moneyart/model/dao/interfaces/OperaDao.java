package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Opera;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di un'Opera.
 */
public interface OperaDao extends GenericDao<Opera> {

  /**
   * Restituisce tutte le opere che sono in possesso dell'utente.
   *
   * @param id l'id dell'utente possessore delle opere
   * @return tutte le opere in possesso dell'utente
   */
  List<Opera> doRetrieveAllByOwnerId(int id);

  /**
   * Restituisce tutte le opere create dall'utente.
   *
   * @param id l'id dell'utente creatore delle opere
   * @return tutte le opere create dall'utente
   */
  List<Opera> doRetrieveAllByArtistId(int id);

}
