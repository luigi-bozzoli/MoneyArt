package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import java.util.List;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia SegnalazioneDao.
 */
public class SegnalazioneDaoImpl implements SegnalazioneDao {
  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Segnalazione item) {

  }

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Segnalazione doRetrieveById(int id) {
    return null;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Segnalazione> doRetrieveAll(String filter) {
    return null;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Segnalazione item) {

  }

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Segnalazione item) {

  }

  /**
   * Ricerca nel database tutte le segnalazioni collegate
   * ad una particolare asta.
   *
   * @param id l'identificativo dell'asta segnalata
   * @return una collezione di segnalazioni di una specifica asta
   */
  @Override
  public List<Segnalazione> doRetrieveByAuctionId(int id) {
    return null;
  }
}
