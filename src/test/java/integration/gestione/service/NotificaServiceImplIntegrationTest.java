package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaServiceImpl;
import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NotificaServiceImplIntegrationTest {

  private static DataSource dataSource;

  private NotificaDao notificaDao;

  private NotificaService notifcaService;

  @BeforeAll
  static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx = null;
    Context envCtx = null;
    try{
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if(envCtx == null){
      try{
        initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
        dataSource = (DataSource) envCtx.lookup("jdbc/storage");
      } catch (NamingException e) {
        e.printStackTrace();
      }
    }

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/model/db/ddl_moneyart.sql"));
    runner.runScript(reader);
    connection.close();
  }

  @BeforeEach
  void setUp() throws SQLException, FileNotFoundException {

    notificaDao = new NotificaDaoImpl(dataSource);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/" +
      "model/db/populator_moneyart.sql"));
    runner.runScript(reader);
    connection.close();

    notifcaService = new NotificaServiceImpl(notificaDao);
  }

  @AfterEach
  void tearDown() throws SQLException, FileNotFoundException {

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
    runner.runScript(reader);
    connection.close();
  }

  static class  NotificaProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Utente u1 = new Utente();
      u1.setId(1);
      Rivendita r1 = new Rivendita();
      r1.setId(1);
      Asta a1 = new Asta();
      a1.setId(1);
      Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, null, true);
      n1.setId(1);

      Utente u2 = new Utente();
      u2.setId(2);
      Rivendita r2 = new Rivendita();
      r2.setId(2);
      Asta a2 = new Asta();
      a2.setId(2);
      Notifica n2 = new Notifica(u2, a2, r2, Notifica.Tipo.VITTORIA, null, false);
      n2.setId(2);

      Utente u3 = new Utente();
      u3.setId(3);
      Rivendita r3 = new Rivendita();
      r3.setId(3);
      Asta a3 = new Asta();
      a3.setId(3);
      Notifica n3 = new Notifica(u3, a3, r3, Notifica.Tipo.TERMINATA, null, true);
      n3.setId(3);

      return Stream.of(
        Arguments.of(n1),
        Arguments.of(n2),
        Arguments.of(n3)
      );
    }
  }


  @Test
  void getNotificationsByUser() {
    List<Notifica> lista = new ArrayList<Notifica>();

    Utente u1 = new Utente();
    u1.setId(1);
    Rivendita r1 = new Rivendita();
    r1.setId(1);
    Asta a1 = new Asta();
    a1.setId(1);
    Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, null, true);

    Rivendita r2 = new Rivendita();
    r2.setId(2);
    Asta a2 = new Asta();
    a2.setId(2);
    Notifica n2 = new Notifica(u1, a2, r2, Notifica.Tipo.SUPERATO, null, false);

    for (Notifica n: lista) {
      notificaDao.doCreate(n);
    }

    assertArrayEquals(lista.toArray(), notifcaService.getNotificationsByUser(u1.getId()).toArray());
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  void getNotification(Notifica n) {
    Notifica notifica = notificaDao.doRetrieveById(n.getId());

    assertEquals(notifica, notifcaService.getNotification(n.getId()));
  }

  @ParameterizedTest
  @ArgumentsSource((NotificaProvider.class))
  void readNotification(Notifica n) {
    notificaDao.doCreate(n);
    notifcaService.readNotification(n);
    Notifica notifica = notificaDao.doRetrieveById(n.getId());
    assertTrue(notifica.isLetta());
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  void unreadNotification(Notifica n) {
    notificaDao.doCreate(n);
    notifcaService.readNotification(n);
    Notifica notifica = notificaDao.doRetrieveById(n.getId());
    assertTrue(notifica.isLetta());
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  void deleteNotification(Notifica n) {
    notificaDao.doCreate(n);
    notifcaService.deleteNotification(n);
    Notifica notifica = notificaDao.doRetrieveById(n.getId());

    assertNull(n);
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  void addNotification(Notifica n) {
    assertTrue(notifcaService.addNotification(n));
    Notifica notifica = notificaDao.doRetrieveById(n.getId());
    assertEquals(notifica, n);
  }
}