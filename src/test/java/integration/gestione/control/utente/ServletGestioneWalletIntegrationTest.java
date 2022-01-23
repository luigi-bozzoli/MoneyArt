package integration.gestione.control.utente;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletFollow;
import it.unisa.c02.moneyart.gestione.utente.control.ServletGestioneWallet;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
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
import javax.servlet.ServletContext;
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
public class ServletGestioneWalletIntegrationTest {

  private static DataSource dataSource;
  private ServletGestioneWallet servletGestioneWallet;

  private UtenteService utenteService;

  private NotificaDao notificaDao;
  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private PartecipazioneDao partecipazioneDao;

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  RequestDispatcher dispatcher;
  @Mock
  HttpSession session;
  @Mock
  ServletContext context;

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
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_utente.sql"));
    runner.runScript(reader);
    connection.close();

    operaDao = new OperaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);

    utenteService = new UtenteServiceImpl(utenteDao, operaDao, notificaDao, partecipazioneDao);

    servletGestioneWallet = new ServletGestioneWallet();

    Field injectedObject = servletGestioneWallet.getClass().getDeclaredField("utenteService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletGestioneWallet, utenteService);

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
  @DisplayName("doGet Withdraw Test")
  void doGetWithdraw() throws ServletException, IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("withdraw");
    when(request.getParameter("amount")).thenReturn("500");
    doNothing().when(session).setAttribute(anyString(), any());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletGestioneWallet.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGestioneWallet, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
  }

  @Test
  @DisplayName("doGet Withdraw Error Test")
  void doGetWithdrawError() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("withdraw");
    when(request.getParameter("amount")).thenReturn("2500");
    doNothing().when(session).setAttribute(anyString(), any());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletGestioneWallet.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGestioneWallet, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
  }

}
