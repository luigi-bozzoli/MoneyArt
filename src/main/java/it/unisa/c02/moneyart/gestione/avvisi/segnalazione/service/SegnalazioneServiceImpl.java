package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import java.util.List;
import javax.inject.Inject;

/**
 * Questa classe implementa i metodi dell'interfaccia SegnalazioneService.
 *
 */
public class SegnalazioneServiceImpl implements SegnalazioneService {

  /**
   * Costruttore che istanzia tramite il Retriver i dao.
   */
  public SegnalazioneServiceImpl() {

  }

  /**
   * Costruttore con parametri.
   *
   * @param segnalazioneDao dao di segnalazione
   */
  public SegnalazioneServiceImpl(SegnalazioneDao segnalazioneDao) {
    this.segnalazioneDao = segnalazioneDao;
  }

  /**
   * Restituisce tutte le segnalazioni ordinandole secondo un filtro.
   *
   * @param filter per ordinare la lista
   * @return lista di tutte le segnalazioni
   */
  @Override
  public List<Segnalazione> getReports(String filter) {
    return segnalazioneDao.doRetrieveAll(filter);
  }

  /**
   * Restituisce una segnalazione attraverso il suo id
   *
   * @param id della segnalazione
   * @return segnalazione
   */
  @Override
  public Segnalazione getReport(int id) {
    return segnalazioneDao.doRetrieveById(id);
  }

  /**
   * Aggiunge una nuova segnalazione.
   *
   * @param segnalazione
   * @pre Segnalazione.allIstances() -> not exists(s:Segnalazione | s = segnalazione)
   * @post Segnalazione.allIstances() -> exists(s:Segnalazione | s = segnalazione)
   */
  @Override
  public void addReport(Segnalazione segnalazione) {
    segnalazioneDao.doCreate(segnalazione);
  }

  /**
   * Rimuove una segnalazione.
   *
   * @param segnalazione
   * @pre Segnalazione.allIstances() -> exists(s:Segnalazione | s = segnalazione)
   * @post Segnalazione.allIstances() -> not exists(s:Segnalazione | s = segnalazione)
   */
  @Override
  public void removeReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazioneDao.doDelete(segnalazione);
  }

  /**
   * Imposta il parametro "letta" a true.
   *
   * @param segnalazione
   * @pre Segnalazione.allIstances() -> exists(s:Segnalazione | s = segnalazione)
   * @post segnalazione.setLetta(true)
   */
  @Override
  public void readReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazione.setLetta(true);

    segnalazioneDao.doUpdate(segnalazione);
  }

  /**
   * Imposta il parametro "letta" a false.
   *
   * @param segnalazione
   * @pre Segnalazione.allIstances() -> exists(s:Segnalazione | s = segnalazione)
   * @post segnalazione.setLetta(false)
   */
  @Override
  public void unreadReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazione.setLetta(false);

    segnalazioneDao.doUpdate(segnalazione);
  }

  @Inject
  private SegnalazioneDao segnalazioneDao;

}
