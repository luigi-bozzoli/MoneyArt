package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Segnalazione;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di una Segnalazione.
 */
public interface SegnalazioneDao extends GenericDao<Segnalazione> {

  List<Segnalazione> doRetrieveByAuctionId(int id);
}
