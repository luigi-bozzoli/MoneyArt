package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
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

@DisplayName("SegnalazioneDao")
public class SegnalazioneDaoImplIntegrationTest {

  private static DataSource dataSource;

  private SegnalazioneDao segnalazioneDao;
  private AstaDao astaDao;

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
    // Creazione database
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

  @ParameterizedTest
  @DisplayName("Test Do Create")
  @ArgumentsSource(SegnalazioneProvider.class)
  void doCreate(Segnalazione s) {
    Boolean bool = segnalazioneDao.doCreate(s);
    System.out.println(s.getId());
    Segnalazione s2 = segnalazioneDao.doRetrieveById(s.getId());

    Assertions.assertEquals(s, s2);
    Assertions.assertTrue(bool);
  }

  @ParameterizedTest
  @DisplayName("Test Do Retrieve By Id")
  @ArgumentsSource(SegnalazioneProvider.class)
  void doRetrieveById(Segnalazione s){
    segnalazioneDao.doCreate(s);
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());

    Assertions.assertEquals(s1, s);
  }

  @ParameterizedTest
  @DisplayName("Test Do Retrieve All")
  @ArgumentsSource(ListSegnalazioneProvider.class)
  void doRetrieveAll(List<Segnalazione> segnalazioni){
    for (Segnalazione s: segnalazioni) {
      segnalazioneDao.doCreate(s);
    }

    List<Segnalazione> segnalazioni2 = segnalazioneDao.doRetrieveAll(null);

    Assertions.assertArrayEquals(segnalazioni.toArray(), segnalazioni2.toArray());
  }

  @ParameterizedTest
  @DisplayName("Test Do Update")
  @ArgumentsSource(SegnalazioneProvider.class)
  void doUpdate(Segnalazione s){
    segnalazioneDao.doCreate(s);

    s.setLetta(true);
    segnalazioneDao.doUpdate(s);

    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());

    Assertions.assertEquals(s, s1);
  }

  @ParameterizedTest
  @DisplayName("Test Do Delete")
  @ArgumentsSource(SegnalazioneProvider.class)
  void doDelete(Segnalazione s){
    segnalazioneDao.doCreate(s);
    Segnalazione s1 = segnalazioneDao.doRetrieveById(s.getId());
    segnalazioneDao.doDelete(s1);

    Segnalazione s2 = segnalazioneDao.doRetrieveById(s1.getId());
    Assertions.assertNull(s2);
  }

  @ParameterizedTest
  @DisplayName("Test Do Retrieve By Auction")
  @ArgumentsSource(ListSegnalazioneProvider.class)
  void doRetrieveByAuction(List<Segnalazione> segnalazioni) {
    Asta a = new Asta();
    a.setId(1);

    List<Segnalazione> segnalazioni2 = new ArrayList<Segnalazione>();
    for (Segnalazione s: segnalazioni) {
      segnalazioneDao.doCreate(s);
      if(s.getAsta().getId() == a.getId()){
        segnalazioni2.add(s);
      }
    }

    List<Segnalazione> oracle = segnalazioneDao.doRetrieveByAuctionId(a.getId());
    Assertions.assertArrayEquals(oracle.toArray(), segnalazioni2.toArray());
  }
}