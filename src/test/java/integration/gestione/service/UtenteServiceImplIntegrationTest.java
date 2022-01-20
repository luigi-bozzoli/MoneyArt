package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.ContractProducer;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UtenteServiceImplIntegrationTest {

  private static DataSource dataSource;

  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private NotificaDao notificaDao;
  private PartecipazioneDao partecipazioneDao;

  @BeforeAll
  public static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx;
    Context envCtx = null;
    try {
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if (envCtx == null) {
      try {
        initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
        dataSource = (DataSource) envCtx.lookup("jdbc/storage");
      } catch (NamingException e) {
        e.printStackTrace();
      }
    }
  }

  @BeforeEach
  void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/" +
            "model/db/populator_moneyart.sql"));
    runner.runScript(reader);
    connection.close();

    notificaDao = new NotificaDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
  }

  @AfterEach
  void tearDown() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all.sql"));
    runner.runScript(reader);
    connection.close();
  }

  @Test
  void checkUser() {
  }

  @Test
  void getUserInformation() {
  }

  @Test
  void testGetUserInformation() {
  }

  @Test
  void signUpUser() {
  }

  @Test
  void updateUser() {
  }

  @Test
  void getAllUsers() {
  }

  @Test
  void getUsersSortedByFollowers() {
  }

  @Test
  void searchUsers() {
  }

  @Test
  void checkUsername() {
  }

  @Test
  void checkEmail() {
  }

  @Test
  void follow() {
  }

  @Test
  void unfollow() {
  }

  @Test
  void deposit() {
  }

  @Test
  void withdraw() {
  }

  @Test
  void getBalance() {
  }

  @Test
  void encryptPassword() {
  }
}