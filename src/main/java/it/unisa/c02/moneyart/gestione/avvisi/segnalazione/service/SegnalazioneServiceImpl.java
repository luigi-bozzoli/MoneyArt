package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;

import java.util.List;

public class SegnalazioneServiceImpl implements SegnalazioneService {

  public SegnalazioneServiceImpl() {
    this.segnalazioneDao = Retriever.getIstance(SegnalazioneDao.class);
  }

  public SegnalazioneServiceImpl(UtenteDao utenteDao, SegnalazioneDao segnalazioneDao) {
    this.segnalazioneDao = segnalazioneDao;
  }

  @Override
  public List<Segnalazione> getReports(String filter) {
    return segnalazioneDao.doRetrieveAll(filter);
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
