package integration.gestione.control.utente;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletLogin;
import it.unisa.c02.moneyart.gestione.utente.control.ServletSignup;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServletSignupIntegrationTest {

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  RequestDispatcher dispatcher;
  @Mock
  HttpSession session;

  private static DataSource dataSource;

  private ServletSignup servletSignup;
  private UtenteService utenteService;

  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private NotificaDao notificaDao;
  private PartecipazioneDao partecipazioneDao;

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

    utenteDao = new UtenteDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);

    utenteService= new UtenteServiceImpl( utenteDao, operaDao, notificaDao, partecipazioneDao);

    servletSignup = new ServletSignup();

    Field injectedObject = servletSignup.getClass().getDeclaredField("utenteService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletSignup, utenteService);
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

    Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
      "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);

    when(request.getSession()).thenReturn(session);
    doNothing().when(session).setAttribute(anyString(), anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    when(request.getParameter("name")).thenReturn(utente.getNome());
    when(request.getParameter("surname")).thenReturn(utente.getCognome());
    when(request.getParameter("email")).thenReturn(utente.getEmail());
    when(request.getParameter("username")).thenReturn(utente.getUsername());
    when(request.getParameter("password")).thenReturn("stelle");

    Method privateStringMethod = ServletSignup.class
      .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletSignup, request, response);

    verify(request, times(5)).getParameter(anyString());
    verify(request, times(1)).getSession();
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(session, times(1)).setAttribute(anyString(), any());
    verify(dispatcher, times(1)).forward(any(), any());

  }

  @Test
  @DisplayName("doPost Non Existing User Test")
  void doPostExistingUser() throws ServletException, IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
      "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
    utenteDao.doCreate(utente);

    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    when(request.getParameter("name")).thenReturn(utente.getNome());
    when(request.getParameter("surname")).thenReturn(utente.getCognome());
    when(request.getParameter("email")).thenReturn(utente.getEmail());
    when(request.getParameter("username")).thenReturn(utente.getUsername());
    when(request.getParameter("password")).thenReturn("stelle");

    Method privateStringMethod = ServletSignup.class
      .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletSignup, request, response);

    verify(request, times(5)).getParameter(anyString());
    verify(request, times(1)).getSession();
    verify(request, times(1)).setAttribute(anyString(), anyString());
    verify(dispatcher, times(1)).forward(any(), any());

  }
}
