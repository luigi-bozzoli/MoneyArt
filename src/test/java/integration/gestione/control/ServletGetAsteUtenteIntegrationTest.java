package integration.gestione.control;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletGetAsteUtente;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.*;
import it.unisa.c02.moneyart.model.dao.interfaces.*;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletGetAsteUtenteIntegrationTest {

  private static DataSource dataSource;
  private ServletGetAsteUtente servletGetAsteUtente;
  private Utente utente;
  private AstaService service;

  private NotificaDao notificaDao;
  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private AstaDao astaDao;
  private PartecipazioneDao partecipazioneDao;
  private AstaLockingSingleton astaLockingSingleton;
  private TimerScheduler timerScheduler;

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  HttpSession session;

  @BeforeAll
  public static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx = null;
    Context envCtx = null;
    try {
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if (envCtx == null) {
      try {
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
  public void setUp() throws SQLException, FileNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_partecipazione.sql"));
    runner.runScript(reader);
    connection.close();

    operaDao = new OperaDaoImpl(dataSource);
    astaDao = new AstaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);

    Method astaLockingS = AstaLockingSingleton.class.
      getDeclaredMethod("retrieveIstance");
    astaLockingS.setAccessible(true);
    astaLockingSingleton = (AstaLockingSingleton) astaLockingS.invoke(astaLockingSingleton, null);

    Method timerS = TimerScheduler.class.
      getDeclaredMethod("getInstance");
    timerS.setAccessible(true);
    timerScheduler = (TimerScheduler) timerS.invoke(timerScheduler, null);

    service = new AstaServiceImpl(astaDao, operaDao, utenteDao, partecipazioneDao,
      timerScheduler, astaLockingSingleton, notificaDao);

    servletGetAsteUtente = new ServletGetAsteUtente();

    Field injectedObject = servletGetAsteUtente.getClass().getDeclaredField("astaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletGetAsteUtente, service);
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

  @Test
  @DisplayName("doGet Test AsteUtenteVinte")
  void doGetTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {

    utente = new Utente();
    utente.setId(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(anyString())).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("won");

    Method privateStringMethod = ServletGetAsteUtente.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAsteUtente, request, response);

    verify(request, times(1)).getSession();
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());
  }

  @Test
  @DisplayName("doGet Test Exception")
  void doGetTestException() throws NoSuchMethodException {

    utente = new Utente();
    utente.setId(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(anyString())).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("ExceptionTest");

    Method privateStringMethod = ServletGetAsteUtente.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);

    Throwable t = assertThrows(InvocationTargetException.class,
      () -> {
            privateStringMethod.invoke(servletGetAsteUtente, request, response);
      });

    assertEquals(IllegalStateException.class, t.getCause().getClass());

    verify(request, times(1)).getSession();
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(0)).setAttribute(anyString(), any());
  }

  @Test
  @DisplayName("doPost Test ")
  void doPost() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

    doGetTest();
    doGetTestException();
  }
}