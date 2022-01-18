package integration.utils.timers;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDaoImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class TimedObjectDaoImplIntegrationTest {

  private static DataSource dataSource;

  private TimedObjectDao timedObjectDao;

  @BeforeAll
  static void generalSetUp(){
    Context initCtx = null;
    Context envCtx = null;
    try{
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/timer");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if(envCtx == null){
      try{
        initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
        dataSource = (DataSource) envCtx.lookup("jdbc/timer");
      } catch (NamingException e) {
        e.printStackTrace();
      }
    }

  }

  @BeforeEach
  void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    timedObjectDao = new TimedObjectDaoImpl(dataSource);
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all_timers.sql"));
    runner.runScript(reader);
    connection.close();
  }


  static class TimedObjectProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      TimedObject timedObject1 = new TimedObject(null,"avviaAsta",simpleDateFormat.parse("2014-02-11"));
      TimedObject timedObject2 = new TimedObject("aaaa","TerminaAsta",simpleDateFormat.parse("2014-02-11"));
      TimedObject timedObject3 = new TimedObject(300L,"Prova",simpleDateFormat.parse("2014-02-11"));

      return Stream.of(Arguments.of(timedObject1),Arguments.of(timedObject2),Arguments.of(timedObject3));
    }
  }

  static class ListTimedObjectProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      TimedObject timedObject1 = new TimedObject(100,"avviaAsta",simpleDateFormat.parse("2014-05-11"));
      TimedObject timedObject2 = new TimedObject("aaaa","TerminaAsta",simpleDateFormat.parse("2019-02-11"));
      TimedObject timedObject3 = new TimedObject(simpleDateFormat.parse("2019-01-11"),"Attiva Rivendita",simpleDateFormat.parse("2014-02-11"));
      TimedObject timedObject4 = new TimedObject(100f,"k",simpleDateFormat.parse("2013-02-11"));
      TimedObject timedObject5 = new TimedObject(300L,"Prova",simpleDateFormat.parse("2015-02-11"));

      List<TimedObject> list1 =
          Arrays.asList(timedObject1, timedObject2, timedObject3);
      List<TimedObject> list2 =
          Arrays.asList(timedObject4, timedObject5);
      List<TimedObject> list3 = new ArrayList<>();

      return Stream.of(Arguments.of(list1),Arguments.of(list2),Arguments.of(list3));


    }
  }

  @ParameterizedTest
  @DisplayName("Create")
  @ArgumentsSource(TimedObjectProvider.class)
  void doCreate(TimedObject timedObject) {
    timedObjectDao.doCreate(timedObject);
    TimedObject result = timedObjectDao.doRetrieveById(timedObject.getId());
    Assertions.assertEquals(timedObject,result);
  }

  @ParameterizedTest
  @DisplayName("Retrieve")
  @ArgumentsSource(TimedObjectProvider.class)
  void doRetrieveById(TimedObject timedObject) {
    timedObjectDao.doCreate(timedObject);
    TimedObject result = timedObjectDao.doRetrieveById(timedObject.getId());
    Assertions.assertEquals(result,timedObject);
  }

  @ParameterizedTest
  @DisplayName("Retrieve All")
  @ArgumentsSource(ListTimedObjectProvider.class)
  void doRetrieveAll(List<TimedObject> list) {
    for (TimedObject t : list){
      timedObjectDao.doCreate(t);
    }
    List<TimedObject> result = timedObjectDao.doRetrieveAll("id");
    Assertions.assertArrayEquals(list.toArray(),result.toArray());
  }

  @ParameterizedTest
  @DisplayName("Update")
  @ArgumentsSource(TimedObjectProvider.class)
  void doUpdate(TimedObject timedObject) throws ParseException {
    timedObjectDao.doCreate(timedObject);
    TimedObject toUpdate = timedObjectDao.doRetrieveById(timedObject.getId());
    toUpdate.setAttribute("cambiamento");
    toUpdate.setTaskType("modifica");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    toUpdate.setTaskDate(simpleDateFormat.parse("2020-11-10"));
    timedObjectDao.doUpdate(toUpdate);
    TimedObject result = timedObjectDao.doRetrieveById(toUpdate.getId());
    Assertions.assertEquals(toUpdate,result);

  }

  @ParameterizedTest
  @DisplayName("Delete")
  @ArgumentsSource(TimedObjectProvider.class)
  void doDelete(TimedObject timedObject) {
    timedObjectDao.doCreate(timedObject);
    Assertions.assertNotNull(timedObjectDao.doRetrieveById(timedObject.getId()));
    timedObjectDao.doDelete(timedObject);
    Assertions.assertNull(timedObjectDao.doRetrieveById(timedObject.getId()));
  }
}