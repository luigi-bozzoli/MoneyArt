package it.unisa.c02.moneyart.utils.production;

import javax.enterprise.inject.Produces;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe che produce un dataSource, usato per CDI (Dependency Injection).
 *
 */
public class DataSourceProducer {


  /**
   * restituisce un datasource.
   *
   * @return di un data source
   * @throws NamingException lancia un'eccezione in caso la lookup del datasource dovesse fallire
   */
  @Produces
  public DataSource produce() throws NamingException {
    DataSource dataSource;
    Context initCtx = new InitialContext();
    Context envCtx = (Context) initCtx.lookup("java:comp/env");
    dataSource = (DataSource) envCtx.lookup("jdbc/storage");

    return dataSource;
  }

}
