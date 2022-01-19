package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
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
import java.util.stream.Stream;


class OperaServiceImplIntegrationTest {

  private static DataSource dataSource;

  private OperaService operaService;

  private OperaDao operaDao;

  @BeforeAll
  public static void generalSetUp() {
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
    }

    @DisplayName("Add Artwork with image null")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkImgNull(Opera opera) throws Exception {
      opera.setImmagine(null);

      Assertions.assertFalse(operaService.addArtwork(opera));
    }

    @DisplayName("Add Artwork with name exsiting")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkFalseCheckArtwork(Opera opera) throws Exception {
      operaService.addArtwork(opera);

      Assertions.assertFalse(operaService.addArtwork(opera));
    }

  }

  @Test
  void checkArtwork() {

  }

  @Test
  void getArtwork() {
  }

  @Test
  void searchOpera() {
  }

  @Test
  void getArtworkByUser() {
  }
}