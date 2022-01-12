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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UtenteDaoImplTest {

  private UtenteDao utenteDao;

  private static DataSource dataSource;

  @BeforeClass
  public static void generalSetUp() {
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
  public void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    this.utenteDao = new UtenteDaoImpl(dataSource);
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/populate_all.sql"));
    runner.runScript(reader);
  }

 // @After
  public void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all.sql"));
    runner.runScript(reader);

  }

  @Test
  public void doCreate() throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    Utente admin = new Utente(
        "Franco",
        "Battiato",
        null,
        "prova@moneyart.it",
        "franco",
        new Utente(),
        md.digest("admin".getBytes()),
        0d
    );
    utenteDao.doCreate(admin);
    Utente admin2 = utenteDao.doRetrieveById(admin.getId());
    System.out.println(admin2);
  }

  @Test
  public void doRetrieveById() {
  }

  @Test
  public void doRetrieveAll() {
  }

  @Test
  public void doUpdate() {
  }

  @Test
  public void doDelete() {
  }

  @Test
  public void doRetrieveByUsername() {
  }

  @Test
  public void doRetrieveFollowersByUserId() {
  }

  @Test
  public void researchUser() {
  }

  @Test
  public void doRetrieveByEmail() {
  }
}