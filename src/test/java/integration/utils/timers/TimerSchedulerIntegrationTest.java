package integration.utils.timers;

import static org.junit.jupiter.api.Assertions.*;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDaoImpl;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import it.unisa.c02.moneyart.utils.timers.TimerService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicReference;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class TimerSchedulerIntegrationTest {

  private static DataSource dataSource;

  private TimerScheduler timerScheduler;

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
  void setUp()
      throws IllegalAccessException, InvocationTargetException, InstantiationException,
      SQLException, FileNotFoundException {
    //reinizializza il singleton

    Constructor constructor = TimerScheduler.class.getDeclaredConstructors()[1];

    constructor.setAccessible(true);

    timerScheduler = (TimerScheduler) constructor.newInstance();
    timedObjectDao = new TimedObjectDaoImpl(dataSource);
    timerScheduler.setTimedObjectDao(timedObjectDao);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all_timers.sql"));
    runner.runScript(reader);
    connection.close();
  }

  @Test
  void getInstance() {
    TimerScheduler timerScheduler1 = TimerScheduler.getInstance();
    TimerScheduler timerScheduler2 = TimerScheduler.getInstance();
    Assertions.assertSame(timerScheduler1,timerScheduler2);
  }


  @Nested
  class Schedule {

    private Serializable oracle;

    @BeforeEach
    public void setUp() {
      oracle = null;
      TimerService timerService = (timed) -> {
        oracle = timed.getAttribute();
      };
      timerScheduler.registerTimedService("prova", timerService);
    }

    @Test
    public void scheduleTimedService() throws InterruptedException {
      int seconds = 1;

      Date newDate = getDateAfterSeconds(seconds);

      TimedObject timedObject = new TimedObject("prova", "prova", newDate);
      timerScheduler.scheduleTimedService(timedObject);
      Thread.sleep(seconds * 1000 * 2);
      Assertions.assertEquals(timedObject.getAttribute(), oracle);
    }

    @Test
    public void scheduleTimedServiceFail() {
      int seconds = 1;

      Date newDate = getDateAfterSeconds(seconds);

      TimedObject timedObject = new TimedObject("prova", "prova2", newDate);
      IllegalArgumentException thrown = Assertions
          .assertThrows(IllegalArgumentException.class, () -> {
            timerScheduler.scheduleTimedService(timedObject);
          }, "Illegal Argument Exception Expected");

      Assertions.assertEquals(
          "non esiste un servizio registrato sotto il nome di " + timedObject.getTaskType(),
          thrown.getMessage());


    }

  }


  @Test
  void retrivePersistentTimers() throws InterruptedException {
    AtomicReference<Serializable> oracle1 = new AtomicReference<>();
    TimerService timerService1 = (timed) -> {
      oracle1.set(timed.getAttribute());
    };
    timerScheduler.registerTimedService("prova1", timerService1);
    AtomicReference<Serializable> oracle2 = new AtomicReference<>();
    TimerService timerService2 = (timed) -> {
      oracle2.set(timed.getAttribute());
    };
    timerScheduler.registerTimedService("prova2", timerService2);


    int seconds = 1;
    Date newDate = getDateAfterSeconds(seconds);
    TimedObject timedObject1 = new TimedObject("a", "prova1", newDate);
    TimedObject timedObject2 = new TimedObject("b", "prova2", newDate);
    timedObjectDao.doCreate(timedObject1);
    timedObjectDao.doCreate(timedObject2);
    timerScheduler.retrivePersistentTimers();
    Thread.sleep(seconds * 2 * 1000);
    Assertions.assertEquals(timedObject1.getAttribute(), oracle1.get());
    Assertions.assertEquals(timedObject2.getAttribute(), oracle2.get());
  }


  private Date getDateAfterSeconds(int seconds) {
    Date oldDate = new Date();
    Calendar gcal = new GregorianCalendar();
    gcal.setTime(oldDate);
    gcal.add(Calendar.SECOND, seconds);
    return gcal.getTime();
  }
}