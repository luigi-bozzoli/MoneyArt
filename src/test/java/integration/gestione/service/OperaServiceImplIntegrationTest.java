package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.utils.production.ContractProducer;
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
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;


class OperaServiceImplIntegrationTest {

  private static DataSource dataSource;

  private OperaService operaService;

  private OperaDao operaDao;

  @BeforeAll
  public static void generalSetUp() throws SQLException, FileNotFoundException {
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
  public void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/" +
            "model/db/populator_moneyart.sql"));
    runner.runScript(reader);
    connection.close();

    ContractProducer cp = new ContractProducer();
    operaDao = new OperaDaoImpl(dataSource);
    MoneyArtNft moneyArtNft = cp.contractInizializer();

    operaService = new OperaServiceImpl(operaDao, moneyArtNft);
  }

  @AfterEach
  void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all.sql"));
    runner.runScript(reader);
    connection.close();
  }

  static class OperaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      String s1 = "image blob 1";
      Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
      Utente u1 = new Utente();
      u1.setId(1);
      Opera o1 = new Opera("The GOD", "Descrizione", Opera.Stato.PREVENDITA,
              b1, u1, u1, "xxxxx");

      String s2 = "image blob 2";
      Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
      Utente u2 = new Utente();
      u2.setId(2);
      Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
              b2, u2, u2, "yyyyyyyyy");

      String s3 = "image blob 3";
      Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
      Utente u3 = new Utente();
      u3.setId(3);
      Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
              b3, u3, u3, "zzzzzz");

      return Stream.of(
              Arguments.of(o1),
              Arguments.of(o2),
              Arguments.of(o3)
      );

    }
  }

  static class InputCheckArtworkProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      return Stream.of(
              Arguments.of(2,"The TestCaseBast"),
              Arguments.of(3, "BASTA TESTING"),
              Arguments.of(4, "ZizzoDonnarumma")
              );
    }
  }

  static class InputCheckArtworkExistingProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      return Stream.of(
              Arguments.of(2,"The Shibosis"),
              Arguments.of(2, "Bears Deluxe #3742"),
              Arguments.of(5, "CupCat")
      );
    }
  }

  @Nested
  @DisplayName("Test Suite addArtwork")
  class TestAddArtwork{

    @DisplayName("Add Artwork")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtwork(Opera opera) throws Exception {
      Assertions.assertTrue(operaService.addArtwork(opera));
      Opera result = operaDao.doRetrieveById(opera.getId());
      Assertions.assertEquals(opera,result);
    }

    @DisplayName("Add Artwork with name null")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkNameNull(Opera opera) throws Exception {
      opera.setNome(null);

      Assertions.assertFalse(operaService.addArtwork(opera));
      Assertions.assertNull(operaDao.doRetrieveAllByName(opera.getNome()));
    }

    @DisplayName("Add Artwork with image null")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkImgNull(Opera opera) throws Exception {
      opera.setImmagine(null);

      Assertions.assertFalse(operaService.addArtwork(opera));
      Assertions.assertEquals(0,operaDao.doRetrieveAllByName(opera.getNome()).size());
    }

    @Test
    @DisplayName("Add Artwork with name exsiting")
    void addArtworkFalseCheckArtwork() throws Exception {
      //OPERA GIA' PRESENTE NEL DB (PRESA DAL POPULATOR)
      String s = "image blob 1";
      Blob b = new SerialBlob(s.getBytes(StandardCharsets.UTF_8));
      Utente u1 = new Utente();
      u1.setId(3);
      Utente u2 = new Utente();
      u2.setId(2);
      Opera o = new Opera("The Shibosis", "Descrizione", Opera.Stato.IN_VENDITA,
              b, u1, u2, "xxxxx");

      Assertions.assertFalse(operaService.addArtwork(o));
    }

  }

  @Nested
  @DisplayName("Test Suite checkArtwork")
  class TestCheckArtwork {

    @DisplayName("Check Artwork false")
    @ParameterizedTest
    @ArgumentsSource(InputCheckArtworkProvider.class)
    void checkArtwork(int id, String name){
      Assertions.assertFalse(operaService.checkArtwork(id, name));
    }

    @DisplayName("Check Artwork with name null")
    @ParameterizedTest
    @ArgumentsSource(InputCheckArtworkProvider.class)
    void checkArtworkNameNull(int id){
      Assertions.assertThrows(Exception.class, () -> operaService.checkArtwork(id,null));
    }

    @DisplayName("Check Artwork name existing")
    @ParameterizedTest
    @ArgumentsSource(InputCheckArtworkExistingProvider.class)
    void checkArtworkNameNull(int id, String name){
      Assertions.assertTrue(operaService.checkArtwork(id, name));
    }

  }

  @DisplayName("Get Artwork")
  @ParameterizedTest
  @ArgumentsSource(InputCheckArtworkProvider.class)
  void getArtwork(int id) {
    Opera result = operaDao.doRetrieveById(id);

    Assertions.assertEquals(result, operaService.getArtwork(id));
  }

  @Test
  @DisplayName("Search name")
  void searchOpera() {
    List<Opera> result = operaDao.doRetrieveAllByName("Bears");

    Assertions.assertEquals(result, operaService.searchOpera("Bears"));
  }

  @Test
  @DisplayName("Search name null")
  void searchOperaNameNull() {
    List<Opera> result = operaDao.doRetrieveAllByName(null);

    Assertions.assertEquals(result, operaService.searchOpera(null));
  }

  @DisplayName("Get Artwork By User")
  @ParameterizedTest
  @ArgumentsSource(InputCheckArtworkProvider.class)
  void getArtworkByUser(int id) {
    List<Opera> result = operaDao.doRetrieveAllByArtistId(id);

    Assertions.assertEquals(result, operaService.getArtworkByUser(id));
  }
}