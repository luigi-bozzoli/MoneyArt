package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
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


@DisplayName("RivenditaDao")
class RivenditaDaoImplIntegrationTest {

  private static DataSource dataSource;

  private RivenditaDao rivenditaDao;

  @BeforeAll
  static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx;
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
    rivenditaDao = new RivenditaDaoImpl(dataSource);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_rivendita.sql"));
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

  static class RivenditaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

      Opera opera = new Opera();
      opera.setId(1);

      return Stream.of(
        Arguments.of(new Rivendita(
          opera,
          Rivendita.Stato.IN_CORSO,
          999.99d
        )));
    }
  }

  static class ListRivenditaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

      Opera opera1 = new Opera();
      opera1.setId(3);

      Opera opera2 = new Opera();
      opera2.setId(2);

      Rivendita rivendita1 = new Rivendita(
        opera1,
        Rivendita.Stato.TERMINATA,
        999.99d
      );

      Rivendita rivendita2 = new Rivendita(
        opera2,
        Rivendita.Stato.TERMINATA,
        999.99d
      );

      List<Rivendita> rivendite = new ArrayList<>();
      rivendite.add(rivendita1);
      rivendite.add(rivendita2);

      return Stream.of(
        Arguments.of(rivendite));
    }
  }

  @Nested
  @DisplayName("Create")
  class CreateTest {

    @ParameterizedTest
    @DisplayName("Create New Resell")
    @ArgumentsSource(RivenditaProvider.class)
    void doCreateNewResell(Rivendita rivendita) {

      rivenditaDao.doCreate(rivendita);
      Rivendita result = rivenditaDao.doRetrieveById(rivendita.getId());

      Assertions.assertEquals(rivendita, result);

    }

  }

  @Nested
  @DisplayName("Retrieve All By Status")
  class RetrieveAllByStatusTest {
    @ParameterizedTest
    @DisplayName("Retrieve All Resells By Status")
    @ArgumentsSource(ListRivenditaProvider.class)
    void doRetrieveAllResellsByStatus(List<Rivendita> rivendite){
      for(Rivendita r: rivendite) {
        rivenditaDao.doCreate(r);
      }
      List<Rivendita> result = rivenditaDao.doRetrieveByStato(Rivendita.Stato.TERMINATA);
      Assertions.assertNotNull(result);

      Assertions.assertArrayEquals(rivendite.toArray(), result.toArray());
    }
  }

  @Nested
  @DisplayName("Retrieve All")
  class RetrieveAllTest {

    @ParameterizedTest
    @DisplayName("Retrieve All Resells")
    @ArgumentsSource(ListRivenditaProvider.class)
    void doRetrieveAllResells(List<Rivendita> rivendite){

      for(Rivendita r: rivendite) {
        rivenditaDao.doCreate(r);
      }

      List<Rivendita> result = rivenditaDao.doRetrieveAll("id");

      Assertions.assertNotNull(result);

      Assertions.assertArrayEquals(rivendite.toArray(), result.toArray());
    }
  }


  @Nested
  @DisplayName("Retrieve")
  class RetrieveTest {

    @ParameterizedTest
    @DisplayName("Retrieve Existing Resell")
    @ArgumentsSource(RivenditaProvider.class)
    void doRetrieveExistingResell(Rivendita rivendita){

      rivenditaDao.doCreate(rivendita);
      Rivendita result = rivenditaDao.doRetrieveById(rivendita.getId());
      Assertions.assertEquals(rivendita, result);
    }

    @ParameterizedTest
    @DisplayName("Retrieve NonExisting Resell")
    @ArgumentsSource(RivenditaProvider.class)
    void doRetrieveNonExistingResell(){

      Rivendita result = rivenditaDao.doRetrieveById(-1);
      Assertions.assertNull(result);
    }

  }

  @ParameterizedTest
  @DisplayName("Update")
  @ArgumentsSource(RivenditaProvider.class)
  void doUpdate(Rivendita rivendita) {

    //create
    rivenditaDao.doCreate(rivendita);

    //update
    rivendita.setPrezzo(1d);
    rivenditaDao.doUpdate(rivendita);

    Rivendita result = rivenditaDao.doRetrieveById(rivendita.getId());

    Assertions.assertEquals(rivendita, result);
  }

  @ParameterizedTest
  @DisplayName("Delete")
  @ArgumentsSource(RivenditaProvider.class)
  void doDelete(Rivendita rivendita) {

    rivenditaDao.doCreate(rivendita);

    rivenditaDao.doDelete(rivendita);

    Rivendita result = rivenditaDao.doRetrieveById(rivendita.getId());

    Assertions.assertNull(result);
    
  }
}
