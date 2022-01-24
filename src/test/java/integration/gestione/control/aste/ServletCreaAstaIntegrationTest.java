package integration.gestione.control.aste;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletCreaAsta;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletGetAste;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.*;
import it.unisa.c02.moneyart.model.dao.interfaces.*;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.production.ContractProducer;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletCreaAstaIntegrationTest {

  private static DataSource dataSource;
  private ServletCreaAsta servletCreaAsta;
  private AstaService astaService;
  private OperaService operaService;
  private MoneyArtNft moneyArtNft;

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
  public void setUp() throws SQLException, FileNotFoundException, NoSuchFieldException,
      IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_notifica.sql"));
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
    timerScheduler.registerTimedService("avviaAsta",(x) ->{});

    ContractProducer cp = new ContractProducer();
    moneyArtNft = cp.contractInizializer();

    astaService = new AstaServiceImpl(astaDao, operaDao, utenteDao, partecipazioneDao,
      timerScheduler, astaLockingSingleton, notificaDao);
    operaService = new OperaServiceImpl(operaDao, moneyArtNft);

    servletCreaAsta = new ServletCreaAsta();

    Field injectedObject = servletCreaAsta.getClass().getDeclaredField("astaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletCreaAsta, astaService);

    Field injectedObject2 = servletCreaAsta.getClass().getDeclaredField("operaService");
    injectedObject2.setAccessible(true);
    injectedObject2.set(servletCreaAsta, operaService);

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
  @DisplayName("doPost Test")
  void doPost() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = new Utente();
    utente.setId(5);

    Opera opera = new Opera();
    opera.setId(3);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("id")).thenReturn(opera.getId().toString());
    when(request.getParameter("inizio")).thenReturn("30/01/2022");
    when(request.getParameter("fine")).thenReturn("10/02/2022");
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletCreaAsta.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletCreaAsta, request, response);

    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(dispatcher, times(1)).forward(any(), any());

  }

  @Test
  @DisplayName("doPost Asta Fault Test")
  void doPostAstaFault() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = new Utente();
    utente.setId(5);

    Opera opera = new Opera();
    opera.setId(3);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("id")).thenReturn(opera.getId().toString());
    when(request.getParameter("inizio")).thenReturn("22-01-2022");
    when(request.getParameter("fine")).thenReturn("10-02-2022");
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());
    doNothing().when(response).sendRedirect(anyString());

    Method privateStringMethod = ServletCreaAsta.class
      .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletCreaAsta, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).setAttribute(anyString(), any());
  }

  @Test
  @DisplayName("doPost Opera Fault Test")
  void doPostOperaFault() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = new Utente();
    utente.setId(5);

    Opera opera = new Opera();
    opera.setId(8);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("id")).thenReturn(opera.getId().toString());
    when(request.getParameter("inizio")).thenReturn("30-01-2022");
    when(request.getParameter("fine")).thenReturn("10-02-2022");
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());
    doNothing().when(response).sendRedirect(anyString());

    Method privateStringMethod = ServletCreaAsta.class
      .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletCreaAsta, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doPost Catch Exception Test")
  void doPostCatchException() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = new Utente();
    utente.setId(5);

    Opera opera = new Opera();
    opera.setId(3);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("id")).thenReturn(opera.getId().toString());
    when(request.getParameter("inizio")).thenReturn("30/01-2022");
    when(request.getParameter("fine")).thenReturn("10-02-2022");
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());
    doNothing().when(response).sendRedirect(anyString());

    Method privateStringMethod = ServletCreaAsta.class
      .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletCreaAsta, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(2)).setAttribute(anyString(), any());

  }
}
