package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di un'Opera.
 */
public interface OperaDao extends GenericDao<Opera> {

  /**
   * Restituisce tutte le opere che sono in possesso dell'utente.
   *
   * @param utente l'utente possessore delle opere
   * @return tutte le opere in possesso dell'utente
   */
  List<Opera> doRetrieveAllByPossessore(Utente utente);

  /**
   * Restituisce tutte le opere create dall'utente.
   *
   * @param artista l'utente creatore delle opere
   * @return tutte le opere create dall'utente
   */
  List<Opera> doRetrieveAllByArtista(Utente artista);

}
