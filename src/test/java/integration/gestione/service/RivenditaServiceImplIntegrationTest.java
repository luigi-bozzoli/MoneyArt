package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;


class RivenditaServiceImplIntegrationTest {

  private static DataSource dataSource;

  private RivenditaService service;

  private RivenditaDao rivenditaDao;
  private NotificaDao notificaDao;
  private UtenteDao utenteDao;
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

    rivenditaDao = new RivenditaDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_rivendita.sql"));
    runner.runScript(reader);
    connection.close();

    service = new RivenditaServiceImpl(utenteDao, operaDao, rivenditaDao, notificaDao);
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

  static class OperaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Utente artist = new Utente();
      artist.setId(2);

      Utente owner = new Utente();
      owner.setId(3);

      Opera opera = new Opera(
        "The Shibosis",
        "Descrizione",
        Opera.Stato.IN_POSSESSO,
        new SerialBlob("0xFFFF".getBytes()),
        owner,
        artist,
        null
      );

      opera.setId(1);

      return Stream.of(Arguments.of(opera));
    }
  }

  static class RivenditaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Utente artist = new Utente();
      artist.setId(2);

      Utente owner = new Utente();
      owner.setId(3);

      Opera opera = new Opera(
        "The Shibosis",
        "Descrizione",
        Opera.Stato.IN_POSSESSO,
        new SerialBlob("0xFFFF".getBytes()),
        owner,
        artist,
        null
      );
      opera.setId(1);

      Rivendita resell = new Rivendita(
        opera,
        Rivendita.Stato.IN_CORSO,
        0d
      );

      resell.setId(1);

      return Stream.of(Arguments.of(resell));
    }
  }

  static class ListRivenditeProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      List<Rivendita> rivendite = new ArrayList<>();

      Utente artist1 = new Utente();
      artist1.setId(2);

      Utente owner1 = new Utente();
      owner1.setId(3);

      Opera opera1 = new Opera(
        "The Shibosis",
        "Descrizione",
        Opera.Stato.IN_POSSESSO,
        new SerialBlob("0xFFFF".getBytes()),
        owner1,
        artist1,
        null
      );
      opera1.setId(1);

      Rivendita resell1 = new Rivendita(
        opera1,
        Rivendita.Stato.IN_CORSO,
        0d
      );

      resell1.setId(1);

      Utente artist2 = new Utente();
      artist2.setId(6);

      Utente owner2 = new Utente();
      owner2.setId(4);

      Opera opera2 = new Opera(
        "Capsule House",
        "Descrizione",
        Opera.Stato.IN_POSSESSO,
        new SerialBlob("0xFFFF".getBytes()),
        owner2,
        artist2,
        null
      );
      opera2.setId(6);

      Rivendita resell2 = new Rivendita(
        opera2,
        Rivendita.Stato.IN_CORSO,
        2d
      );

      resell2.setId(1);

      rivendite.add(resell1);
      rivendite.add(resell2);

      return Stream.of(Arguments.of(rivendite));
    }
  }

  @ParameterizedTest
  @DisplayName("Get Resell Price Test")
  @ArgumentsSource(OperaProvider.class)
  void getResellPrice(Opera opera) {

    Opera artwork = operaDao.doRetrieveById(opera.getId());
    assertNotNull(artwork);
    assertEquals(service.getResellPrice(opera), service.getResellPrice(artwork));
  }

  @ParameterizedTest
  @DisplayName("Make New Resell Test")
  @ArgumentsSource(OperaProvider.class)
  void resell(Opera opera) {

    Opera artwork = operaDao.doRetrieveById(opera.getId());
    assertNotNull(artwork);
    assertTrue(service.resell(artwork.getId()));
  }

  @ParameterizedTest
  @DisplayName("Get Resell Test")
  @ArgumentsSource(RivenditaProvider.class)
  void getResell(Rivendita rivendita) {

    //Creo la rivendita
    assertTrue(service.resell(rivendita.getOpera().getId()));
    rivendita.getOpera().setStato(Opera.Stato.IN_VENDITA);

    Rivendita retrieve = service.getResell(rivendita.getId());
    assertEquals(rivendita.getId(), retrieve.getId());
    assertEquals(rivendita.getOpera().getId(), retrieve.getOpera().getId());
    assertEquals(rivendita.getStato(), retrieve.getStato());
    assertEquals(rivendita.getPrezzo(), retrieve.getPrezzo());
  }

  @ParameterizedTest
  @DisplayName("Buy Test")
  @ArgumentsSource(RivenditaProvider.class)
  void buy(Rivendita rivendita) {

    //Creo la rivendita
    assertTrue(service.resell(rivendita.getOpera().getId()));

    Rivendita resell = rivenditaDao.doRetrieveById(rivendita.getId());
    assertNotNull(resell);

    Utente buyer = utenteDao.doRetrieveById(1);
    assertNotNull(buyer);

    boolean result = service.buy(resell.getId(), buyer.getId());
    assertTrue(result);
  }

  @ParameterizedTest
  @DisplayName("Get Resells Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResells(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResells();

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());
  }

  @ParameterizedTest
  @DisplayName("Get Resells By State Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsByState(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResellsByState(Rivendita.Stato.IN_CORSO);

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());

  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPrice(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResellsSortedByPrice("", Rivendita.Stato.IN_CORSO);

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Desc Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPriceDesc(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResellsSortedByPrice("DESC", Rivendita.Stato.IN_CORSO);

    Collections.reverse(rivendite);

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Most Popular Artists Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByMostPopularArtists(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResellsSortedByArtistFollowers("", Rivendita.Stato.IN_CORSO);

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Most Popular Artists Desc Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByMostPopularArtistsDesc(List<Rivendita> rivendite) {

    rivenditaDao.doCreate(rivendite.get(0));
    rivenditaDao.doCreate(rivendite.get(1));

    List<Rivendita> result = service.getResellsSortedByArtistFollowers("DESC", Rivendita.Stato.IN_CORSO);

    Collections.reverse(rivendite);

    assertEquals(rivendite.get(0).getId(), result.get(0).getId());
    assertEquals(rivendite.get(0).getOpera().getId(), result.get(0).getOpera().getId());
    assertEquals(rivendite.get(0).getStato(), result.get(0).getStato());
    assertEquals(rivendite.get(0).getPrezzo(), result.get(0).getPrezzo());

    assertEquals(rivendite.get(1).getId(), result.get(1).getId());
    assertEquals(rivendite.get(1).getOpera().getId(), result.get(1).getOpera().getId());
    assertEquals(rivendite.get(1).getStato(), result.get(1).getStato());
    assertEquals(rivendite.get(1).getPrezzo(), result.get(1).getPrezzo());
  }
}
