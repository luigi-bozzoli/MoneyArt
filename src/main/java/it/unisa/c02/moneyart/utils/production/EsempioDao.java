package it.unisa.c02.moneyart.utils.production;

import javax.sql.DataSource;

public class EsempioDao {


  private DataSource ds;

  public EsempioDao() {
    ds =  Retriever.getIstance(DataSource.class);
  }

  public EsempioDao(DataSource ds) {
    this.ds = ds;
  }

}
