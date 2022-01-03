package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Questa classe rappresenta il DAO di una partecipazione
 */

public interface PartecipazioneDao extends GenericDao<Partecipazione>{
  public List<Double> doRetrieveOffers(int id);
  public List<Asta> doRetrieveByUserId(int id);
  public List<Utente> doRetrieveByAuctionId(int id);
}
