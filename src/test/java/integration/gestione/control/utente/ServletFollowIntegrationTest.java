package integration.gestione.control.utente;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletFollow;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.*;
import it.unisa.c02.moneyart.model.dao.interfaces.*;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServletFollowIntegrationTest {

  private static DataSource dataSource;
  private ServletFollow servletFollow;

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
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_utente.sql"));
    runner.runScript(reader);
    connection.close();

    operaDao = new OperaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);

    utenteService = new UtenteServiceImpl(utenteDao, operaDao, notificaDao, partecipazioneDao);

    servletFollow = new ServletFollow();

    Field injectedObject = servletFollow.getClass().getDeclaredField("utenteService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletFollow, utenteService);

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
  @DisplayName("doGet Follow No Ajax Test")
  void doGetFollowNoAJAX() throws InvocationTargetException, IllegalAccessException,
      NoSuchMethodException, ServletException, IOException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("follow");
    when(request.getParameter("followed")).thenReturn("3");
    when(request.getHeader(anyString())).thenReturn("");
    doNothing().when(session).setAttribute(anyString(), any());
    doNothing().when(session).removeAttribute(anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletFollow.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletFollow, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(session, times(1)).setAttribute(anyString(), any());
    verify(session, times(1)).removeAttribute(anyString());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
  }

  @Test
  @DisplayName("doGet Follow Ajax Test")
  void doGetFollowAJAX() throws InvocationTargetException, IllegalAccessException,
    NoSuchMethodException, IOException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("follow");
    when(request.getParameter("followed")).thenReturn("3");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    doNothing().when(session).setAttribute(anyString(), any());
    doNothing().when(session).removeAttribute(anyString());
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletFollow.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletFollow, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(2)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(session, times(1)).setAttribute(anyString(), any());
    verify(session, times(1)).removeAttribute(anyString());
    verify(response, times(1)).getWriter();
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(writer, times(1)).write(anyString());
  }

  @Test
  @DisplayName("doGet UnFollow No Ajax Test")
  void doGetUnFollowNoAJAX() throws InvocationTargetException, IllegalAccessException,
    NoSuchMethodException, ServletException, IOException {

    Utente utente = utenteDao.doRetrieveById(2);
    utenteService.follow(utente, utenteDao.doRetrieveById(1));

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("unfollow");
    when(request.getHeader(anyString())).thenReturn("");
    doNothing().when(session).setAttribute(anyString(), any());
    doNothing().when(session).removeAttribute(anyString());
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    doNothing().when(dispatcher).forward(any(), any());

    Method privateStringMethod = ServletFollow.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletFollow, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(session, times(1)).setAttribute(anyString(), any());
    verify(session, times(1)).removeAttribute(anyString());
    verify(request, times(1)).getRequestDispatcher(anyString());
    verify(dispatcher, times(1)).forward(any(), any());
  }

  @Test
  @DisplayName("doGet UnFollow Ajax Test")
  void doGetUnFollowAJAX() throws InvocationTargetException, IllegalAccessException,
    NoSuchMethodException, IOException {

    Utente utente = utenteDao.doRetrieveById(2);
    utenteService.follow(utente, utenteDao.doRetrieveById(1));

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("unfollow");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    doNothing().when(session).setAttribute(anyString(), any());
    doNothing().when(session).removeAttribute(anyString());
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletFollow.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletFollow, request, response);

    verify(request, times(1)).getSession();
    verify(session, times(1)).getAttribute(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(session, times(1)).setAttribute(anyString(), any());
    verify(session, times(1)).removeAttribute(anyString());
    verify(response, times(1)).getWriter();
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(writer, times(1)).write(anyString());
  }

  @Test
  @DisplayName("doGet Exception Test")
  void doGetException() throws NoSuchMethodException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");

    Method privateStringMethod = ServletFollow.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);

    Throwable t = assertThrows(InvocationTargetException.class,
      () -> {
        privateStringMethod.invoke(servletFollow, request, response);
      });

    assertEquals(IllegalStateException.class, t.getCause().getClass());

    verify(request, times(1)).getSession();
    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(session, times(1)).getAttribute(anyString());
  }

}
