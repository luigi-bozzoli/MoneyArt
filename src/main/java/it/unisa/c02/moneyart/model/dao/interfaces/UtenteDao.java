package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Utente.
 */
public interface UtenteDao extends GenericDao<Utente> {
//signature metodi aggiuntivi.....

  /**
   * Restituisce l'utente in base all'username.
   *
   * @param username l'username dell'utente
   * @return l'utente con quell'username
   */
  public Utente doRetrieveByUsername(String username);

  /**
   * Restituisce tutti gli utenti che seguono uno specifico utente.
   *
   * @param id l'identificativo dell'utente di cui vogliamo conoscere i follower
   * @return i follower di quell'utente
   */
  public List<Utente> doRetrieveFollowersByUserId(int id);

}
