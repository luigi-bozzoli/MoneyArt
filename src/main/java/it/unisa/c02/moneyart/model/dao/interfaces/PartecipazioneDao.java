package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una partecipazione.
 */

public interface PartecipazioneDao extends GenericDao<Partecipazione> {

  /**
   * Ricerca tutte le partecipazioni relative ad un utente.
   *
   * @param id identificativo di un utente
   * @return la collezione di partecipazioni trovata nel database
   */
  List<Partecipazione> doRetrieveAllByUserId(int id);

  /**
   * Ricerca nel database tutte le partecipazioni relative ad un'asta.
   *
   * @param id identificativo di un'asta
   * @return la collezione di partecipazioni trovata nel database
   */
  List<Partecipazione> doRetrieveAllByAuctionId(int id);
}
