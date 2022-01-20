package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
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

class SegnalazioneServiceImplIntegrationTest {

  private static DataSource dataSource;

  private SegnalazioneDao segnalazioneDao;

  private SegnalazioneService segnalazioneService;

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
    segnalazioneDao = new SegnalazioneDaoImpl(dataSource);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_segnalazione.sql"));
    runner.runScript(reader);
    connection.close();

    segnalazioneService = new SegnalazioneServiceImpl(segnalazioneDao);
  }

  @AfterEach
  void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
    runner.runScript(reader);
    connection.close();
  }

  static class SegnalazioneProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      Asta asta = new Asta();
      asta.setId(1);
      String commento = "MarioPeluso ha copiato l'opera di alfcan attualmente all'asta.";
      Segnalazione s = new Segnalazione(asta, commento, false);
      s.setId(1);

      Asta asta2 = new Asta();
      asta2.setId(2);
      String commento2 = "Asta illecita";
      Segnalazione s2 = new Segnalazione(asta2, commento2, false);
      s2.setId(2);

      Asta asta3 = new Asta();
      asta3.setId(3);
      String commento3 = "Altra Asta illecita";
      Segnalazione s3 = new Segnalazione(asta3, commento3, false);
      s3.setId(3);

      return Stream.of(
              Arguments.of(s),
              Arguments.of(s2),
              Arguments.of(s3)
      );
    }
  }

  static class ListSegnalazioneProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      Asta asta = new Asta();
      asta.setId(1);
      String commento = "MarioPeluso ha copiato l'opera di alfcan attualmente all'asta.";
      Segnalazione s = new Segnalazione(asta, commento, false);
      s.setId(1);

      Asta asta2 = new Asta();
      asta2.setId(2);
      String commento2 = "Asta illecita";
      Segnalazione s2 = new Segnalazione(asta2, commento2, false);
      s2.setId(2);

      Asta asta3 = new Asta();
      asta3.setId(3);
      String commento3 = "Altra Asta illecita";
      Segnalazione s3 = new Segnalazione(asta3, commento3, false);
      s3.setId(3);

      ArrayList<Segnalazione> segnalazioni = new ArrayList<Segnalazione>();
      segnalazioni.add(s);
      segnalazioni.add(s2);
      segnalazioni.add(s3);

      return Stream.of(
              Arguments.of(segnalazioni)
      );
    }
  }

  @DisplayName("Get Reports")
  @ParameterizedTest
  @ArgumentsSource(ListSegnalazioneProvider.class)
  void getReports(List<Segnalazione> segnalazioni) {
    for (Segnalazione s: segnalazioni) {
      segnalazioneDao.doCreate(s);
    }

    assertArrayEquals(segnalazioni.toArray(), segnalazioneService.getReports(null).toArray());
  }

  @DisplayName("Get Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void getReport(Segnalazione s) {
    Segnalazione result = segnalazioneDao.doRetrieveById(s.getId());
    assertEquals(result, segnalazioneService.getReport(s.getId()));
  }

  @DisplayName("Add Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void addReport(Segnalazione s) {
    assertTrue(segnalazioneService.addReport(s));
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());
    assertEquals(s1, s);
  }

  @DisplayName("Remove Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void removeReport(Segnalazione s) {
    segnalazioneDao.doCreate(s);
    segnalazioneService.removeReport(s);
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());
    assertNull(s1);
  }

  @DisplayName("Read Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void readReport(Segnalazione s) {
    segnalazioneDao.doCreate(s);
    segnalazioneService.readReport(s);
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());
    assertTrue(s1.isLetta());
  }

  @DisplayName("Unread Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void unreadReport(Segnalazione s) {
    segnalazioneDao.doCreate(s);
    segnalazioneService.unreadReport(s);
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());
    assertFalse(s1.isLetta());
  }
}