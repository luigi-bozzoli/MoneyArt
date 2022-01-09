package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;

/**
 * Questa classe implementa i metodi dell'interfaccia SegnalazioneService.
 */
public class SegnalazioneServiceImpl implements SegnalazioneService {

  /**
   * Costruttore che istanzia tramite il Retriver i dao.
   */
  public SegnalazioneServiceImpl() {
    this.segnalazioneDao = Retriever.getIstance(SegnalazioneDao.class);
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

  @Override
  public Segnalazione getReport(int id) {
    return segnalazioneDao.doRetrieveById(id);
  }

  @Override
  public void addReport(Segnalazione segnalazione) {
    segnalazioneDao.doCreate(segnalazione);
  }

  @Override
  public void removeReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazioneDao.doDelete(segnalazione);
  }

  @Override
  public void readReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazione.setLetta(true);

    segnalazioneDao.doUpdate(segnalazione);
  }

  @Override
  public void unreadReport(Segnalazione segnalazione) {
    segnalazione = segnalazioneDao.doRetrieveById(segnalazione.getId());
    segnalazione.setLetta(false);

    segnalazioneDao.doUpdate(segnalazione);
  }

  private SegnalazioneDao segnalazioneDao;

}
