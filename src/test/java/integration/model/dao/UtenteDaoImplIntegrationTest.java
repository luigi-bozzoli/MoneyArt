package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;


class UtenteDaoImplIntegrationTest {

  private UtenteDao utenteDao;

  private static DataSource dataSource;

  @BeforeClass
  static void generalSetUp(){
    TomcatJNDI tomcatJNDI = new TomcatJNDI();
    tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
    tomcatJNDI.start();
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      dataSource = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  @Before
  void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    utenteDao = new UtenteDaoImpl(dataSource);
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/populate_all.sql"));
    runner.runScript(reader);
  }

  @After
  void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all.sql"));
    runner.runScript(reader);

  }

  @Test
  void doCreate() {

    Utente admin = new Utente(
        "Franco",
        "Battiato",
        null,
        "admin@moneyart.it",
        "franco",
        new Utente(),
        new byte[10],
        0d
    );
    utenteDao.doCreate(admin);
  }

  @Test
  void doRetrieveById() {
  }

  @Test
  void doRetrieveAll() {
  }

  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }
}