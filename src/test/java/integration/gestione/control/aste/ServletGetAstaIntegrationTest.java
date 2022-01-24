package integration.gestione.control.aste;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletGetAsta;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletGetAstaIntegrationTest {

  private static DataSource dataSource;
  private ServletGetAsta servletGetAsta;
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
  @Mock
  RequestDispatcher dispatcher;
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

    servletGetAsta = new ServletGetAsta();

    Field injectedObject = servletGetAsta.getClass().getDeclaredField("astaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletGetAsta, service);
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
  @DisplayName("doGet Test Ajax")
  void doGetTestAjax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException, ServletException {

    when(request.getSession()).thenReturn(session);
    when(request.getParameter("id")).thenReturn("1");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletGetAsta.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAsta, request, response);

    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(dispatcher, times(0)).forward(any(), any());
    verify(request, times(0)).getRequestDispatcher(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(0)).setAttribute(anyString(), any());
  }

  @Test
  @DisplayName("doGet Test NoAjax Terminata")
  void doGetTestNoAjaxTerminata() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException, ServletException {

    when(request.getSession()).thenReturn(session);
    when(request.getParameter("id")).thenReturn("1");
    when(request.getHeader(anyString())).thenReturn("");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletGetAsta.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAsta, request, response);

    verify(response, times(0)).setContentType(anyString());
    verify(response, times(0)).setCharacterEncoding(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(2)).setAttribute(anyString(), any());
  }

  @Test
  @DisplayName("doGet Test NoAjax InCorso")
  void doGetTestNoAjaxInCorso() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
    IOException, ServletException {

    when(request.getSession()).thenReturn(session);
    when(request.getParameter("id")).thenReturn("2");
    when(request.getHeader(anyString())).thenReturn("");
    doNothing().when(request).setAttribute(anyString(), any());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    doNothing().when(writer).write(anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletGetAsta.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetAsta, request, response);

    verify(response, times(0)).setContentType(anyString());
    verify(response, times(0)).setCharacterEncoding(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(2)).setAttribute(anyString(), any());
  }

}
