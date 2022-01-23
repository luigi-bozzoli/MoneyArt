package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Rivendita;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una Rivendita.
 */
public interface RivenditaDao extends GenericDao<Rivendita> {

  /**
   * Ricerca tutte le partecipazioni relative ad un utente.
   *
   * @param s stato della rivendita
   * @return la collezione di partecipazioni trovata nel database
   */
  List<Rivendita> doRetrieveByStato(Rivendita.Stato s);
}
