package it.unisa.c02.moneyart.utils;

import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.utils.production.Retriever;

public class EsempioService {



  //usato nella web app
  public EsempioService() {
    this.notificaDao = Retriever.getIstance(NotificaDao.class);
  }

  //usato solo nella fase di testing
  public EsempioService(NotificaDao notificaDao) {
    this.notificaDao = notificaDao;
  }


  private NotificaDao notificaDao;

}
