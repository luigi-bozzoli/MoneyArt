package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;


@DisplayName("RivenditaDao")
class RivenditaDaoImplIntegrationTest {

  private static DataSource dataSource;

  private RivenditaDao rivenditaDao;
  private OperaDao operaDao;

  @BeforeAll
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

  @BeforeEach
  void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    rivenditaDao = new RivenditaDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/populate_all.sql"));
    runner.runScript(reader);
    connection.close();
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

  static class RivenditaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
      throws Exception {

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
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
      throws Exception {

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
      List<Rivendita> result = rivenditaDao.doRetrieveByStato("TERMINATA");
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

      rivendite.add(0, rivenditaDao.doRetrieveById(1));

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
    void doRetrieveNonExistingResell(Rivendita rivendita){

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
  @DisplayName("Update")
  @ArgumentsSource(RivenditaProvider.class)
  void doDelete(Rivendita rivendita) {

    rivenditaDao.doCreate(rivendita);

    rivenditaDao.doDelete(rivendita);

    Rivendita result = rivenditaDao.doRetrieveById(rivendita.getId());

    Assertions.assertNull(result);

  }
}
