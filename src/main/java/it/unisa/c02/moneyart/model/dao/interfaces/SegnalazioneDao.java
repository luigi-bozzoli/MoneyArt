package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una Segnalazione.
 */
public interface SegnalazioneDao extends GenericDao<Segnalazione> {

  /**
   * Ricerca nel database tutte le segnalazioni collegate
   * ad una particolare asta.
   *
   * @param id l'identificativo dell'asta segnalata
   * @return una collezione di segnalazioni di una specifica asta
   */
  List<Segnalazione> doRetrieveByAuctionId(int id);
}
