package integration.utils.production;

import static org.junit.jupiter.api.Assertions.*;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.utils.production.DataSourceProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataSourceProducerIntegrationTest {

  private DataSourceProducer dataSourceProducer;

  @BeforeAll
  public static void generalSetUp() {
    Context initCtx;
    Context envCtx;
    try{
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
  }

  @BeforeEach
  void setUp() {
    dataSourceProducer = new DataSourceProducer();
  }
  

  @Test
  void produce() throws NamingException {
    InitialContext initCtx = new InitialContext();
    Context envCtx = (Context) initCtx.lookup("java:comp/env");
    Assertions.assertSame(envCtx.lookup("jdbc/storage"),dataSourceProducer.produce());
  }
}