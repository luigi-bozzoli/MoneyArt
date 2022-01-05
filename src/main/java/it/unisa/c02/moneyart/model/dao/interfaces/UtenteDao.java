package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Utente.
 */
public interface UtenteDao extends GenericDao<Utente> {

  /**
   * Restituisce l'utente in base all'username.
   *
   * @param username l'username dell'utente
   * @return l'utente con quell'username
   */
  Utente doRetrieveByUsername(String username);

  /**
   * Restituisce tutti gli utenti che seguono uno specifico utente.
   *
   * @param id l'identificativo dell'utente di cui vogliamo conoscere i follower
   * @return i follower di quell'utente
   */
  List<Utente> doRetrieveFollowersByUserId(int id);

  /**
   * Restituisce tutti gli utenti nel database che hanno un riscontro con la ricerca.
   *
   * @param text stringa da ricercare
   * @return una lista di utenti che hanno un riscontro positivo con la ricerca
   */
  List<Utente> researchUser(String text);

  /**
   * Restituisce l'utente in base all'email.
   *
   * @param email l'email dell'utente
   * @return l'utente con quell'email
   */
  Utente doRetrieveByEmail(String email);

}
