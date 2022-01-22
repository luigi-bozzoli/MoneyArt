package integration.gestione.control;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.aste.control.ServletBestOffer;
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
class ServletBestOfferIntegrationTest {

  private static DataSource dataSource;
  private ServletBestOffer servletBestOffer;
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
  RequestDispatcher dispatcher;

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

    servletBestOffer = new ServletBestOffer();

    Field injectedObject = servletBestOffer.getClass().getDeclaredField("astaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletBestOffer, service);
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
  @DisplayName("doGet Test")
  void doGet() throws NoSuchMethodException, InvocationTargetException,
    IllegalAccessException, ServletException, IOException {

    when(request.getParameter("id")).thenReturn("2");
    doNothing().when(request).setAttribute(anyString(), any());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletBestOffer.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletBestOffer, request, response);

    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());
    verify(dispatcher, times(1)).forward(any(), any());
  }

  @Test
  @DisplayName("doPost Test")
  void doPost() throws ServletException, IOException, InvocationTargetException,
      NoSuchMethodException, IllegalAccessException {

    doGet();
  }

}
