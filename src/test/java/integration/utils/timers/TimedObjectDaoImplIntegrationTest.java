package integration.utils.timers;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDaoImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
      TimedObject timedObject1 = new TimedObject(100,"avviaAsta",simpleDateFormat.parse("2014-02-11"));
      TimedObject timedObject2 = new TimedObject("aaaa","TerminaAsta",simpleDateFormat.parse("2014-02-11"));
      TimedObject timedObject3 = new TimedObject(300L,"Prova",simpleDateFormat.parse("2014-02-11"));

      return Stream.of(Arguments.of(timedObject1),Arguments.of(timedObject2),Arguments.of(timedObject3));
    }
  }

  @ParameterizedTest
  @DisplayName("create test")
  @ArgumentsSource(TimedObjectProvider.class)
  void doCreate(TimedObject timedObject) {
    timedObjectDao.doCreate(timedObject);
    TimedObject result = timedObjectDao.doRetrieveById(timedObject.getId());
    Assertions.assertEquals(timedObject,result);
  }

  @Test
  void doRetrieveById() {
  }

  @Test
  void doRetrieveAll() {
  }

  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }
}