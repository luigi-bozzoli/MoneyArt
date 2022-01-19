package unit.utils.timers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import it.unisa.c02.moneyart.utils.timers.TimerService;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TimerSchedulerUnitTest {


  @Mock
  private TimedObjectDao timedObjectDao;

  private TimerScheduler timerScheduler;


  @BeforeEach
  void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    //reinizializza il singleton

    Constructor constructor = TimerScheduler.class.getDeclaredConstructors()[1];

    constructor.setAccessible(true);

    timerScheduler = (TimerScheduler) constructor.newInstance();
    timerScheduler.setTimedObjectDao(timedObjectDao);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getInstance() {
    TimerScheduler timerScheduler1 = TimerScheduler.getInstance();
    TimerScheduler timerScheduler2 = TimerScheduler.getInstance();
    assertSame(timerScheduler1, timerScheduler2);
  }


  @Test
  public void registerTimedService() {
    String serviceName = "prova";
    TimerService timerService = (timed) -> {
    };
    timerScheduler.registerTimedService(serviceName, timerService);
    Assertions.assertTrue(timerScheduler.getServices().contains(serviceName));
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
  void shutdownTimers() throws InterruptedException {
    AtomicReference<Serializable> oracle = new AtomicReference<>();
    TimerService timerService = (timed) -> {
      oracle.set(timed.getAttribute());
    };
    timerScheduler.registerTimedService("prova", timerService);
    int seconds = 1;

    Date newDate = getDateAfterSeconds(seconds);

    TimedObject timedObject = new TimedObject("prova", "prova", newDate);
    timerScheduler.scheduleTimedService(timedObject);
    timerScheduler.shutdownTimers();
    Thread.sleep(seconds * 1000 * 2);
    Assertions.assertNull(oracle.get());
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

    when(timedObjectDao.doRetrieveAll("id")).thenReturn(Arrays.asList(timedObject1, timedObject2));
    timerScheduler.retrivePersistentTimers();
    Thread.sleep(seconds * 1000 * 2);
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