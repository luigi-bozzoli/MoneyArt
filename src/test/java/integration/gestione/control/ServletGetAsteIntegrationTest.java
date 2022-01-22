package integration.gestione.control;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletGetAste;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
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
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletGetAsteIntegrationTest {

  private static DataSource dataSource;
  private ServletGetAste servletGetAste;
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
  PrintWriter writer;

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

    servletGetAste = new ServletGetAste();

    Field injectedObject = servletGetAste.getClass().getDeclaredField("astaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletGetAste, service);
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
  @DisplayName("doGet Test Ajax GetAsteInCorsoSortedByPrezzo")
  void doGetTestAjaxInCorsoSortedByPrezzo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      IOException {

    when(request.getParameter("action")).thenReturn("inCorso");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Prezzo");
    when(request.getParameter("order")).thenReturn("DESC");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetAste.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAste, request, response);

    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(3)).getParameter(anyString());
    verify(request, times(0)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doGet Test GetAste Ajax NoCriteria")
  void doGetTestAjaxNoCriteria() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetAste.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAste, request, response);

    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(3)).getParameter(anyString());
    verify(request, times(0)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doGet Test GetAste NoAjax")
  void doGetTestNoAjax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("");
    when(request.getParameter("criteria")).thenReturn("");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetAste.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAste, request, response);

    verify(response, times(0)).setContentType(anyString());
    verify(response, times(0)).setCharacterEncoding(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doPost Test")
  void doPost() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException {

    doGetTestAjaxInCorsoSortedByPrezzo();
    doGetTestAjaxNoCriteria();
    doGetTestNoAjax();
  }
}
