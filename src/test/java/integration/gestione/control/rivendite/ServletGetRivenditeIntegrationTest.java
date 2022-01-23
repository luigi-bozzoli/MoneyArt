package integration.gestione.control.rivendite;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.control.ServletGetRivendite;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaServiceImpl;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletGetRivenditeIntegrationTest {

  private static DataSource dataSource;
  private ServletGetRivendite servletGetRivendite;

  private RivenditaService rivenditaService;

  private AstaDao astaDao;
  private NotificaDao notificaDao;
  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private RivenditaDao rivenditaDao;
  private PartecipazioneDao partecipazioneDao;

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
  }

  @BeforeEach
  public void setUp() throws SQLException, FileNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_rivendita.sql"));
    runner.runScript(reader);
    connection.close();

    operaDao = new OperaDaoImpl(dataSource);
    rivenditaDao = new RivenditaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    astaDao = new AstaDaoImpl(dataSource);

    rivenditaService = new RivenditaServiceImpl(utenteDao, operaDao, rivenditaDao, notificaDao,
        astaDao, partecipazioneDao);

    servletGetRivendite = new ServletGetRivendite();

    Field injectedObject = servletGetRivendite.getClass().getDeclaredField("rivenditaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletGetRivendite, rivenditaService);

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
  @DisplayName("doGet Rivendite inCorso Sorted By Prezzo Ajax Test")
  void doGetInCorsoSortedByPrezzoAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("inCorso");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Prezzo");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite inCorso Sorted By Followers Ajax Test")
  void doGetInCorsoSortedByFollowersAjax() throws IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("inCorso");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Followers");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Terminate Sorted By Prezzo Ajax Test")
  void doGetTerminateSortedByPrezzoAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("terminate");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Prezzo");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Terminate Sorted By Followers Ajax Test")
  void doGetTerminateSortedByFollowersAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("terminate");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Followers");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Default Sorted By Prezzo  Ajax Test")
  void doGetDefaultSortedByPrezzoAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Prezzo");
    when(request.getParameter("order")).thenReturn("AnyOrder");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Default Sorted By Followers Ajax Test")
  void doGetDefaultSortedByFollowersAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("Followers");
    when(request.getParameter("order")).thenReturn("AnyOrder");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite inCorso No Criteria Ajax Test")
  void doGetInCorsoNoCriteriaAjax() throws IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("inCorso");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Terminate No Criteria Ajax Test")
  void doGetTerminateNoCriteriaAjax() throws IOException, NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("terminate");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite Default No Criteria Ajax Test")
  void doGetDefaultNoCriteriaAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("XMLHttpRequest");
    when(request.getParameter("criteria")).thenReturn("");
    when(request.getParameter("order")).thenReturn("");
    doNothing().when(response).setContentType(anyString());
    doNothing().when(response).setCharacterEncoding(anyString());
    when(response.getWriter()).thenReturn(writer);
    doNothing().when(writer).write(anyString());

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(3)).getParameter(anyString());
    verify(request, times(1)).getHeader(anyString());
    verify(response, times(1)).setContentType(anyString());
    verify(response, times(1)).setCharacterEncoding(anyString());
    verify(response, times(1)).getWriter();
    verify(writer, times(1)).write(anyString());

  }

  @Test
  @DisplayName("doGet Rivendite inCorso No Ajax Test")
  void doGetInCorsoNoAjax() throws IOException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("inCorso");
    when(request.getHeader(anyString())).thenReturn("");

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doGet Rivendite Terminate No Ajax Test")
  void doGetTerminateNoAjax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("terminate");
    when(request.getHeader(anyString())).thenReturn("");

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());

  }

  @Test
  @DisplayName("doGet Rivendite Default No Ajax Test")
  void doGetDefaultNoAjax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    when(request.getParameter("action")).thenReturn("");
    when(request.getHeader(anyString())).thenReturn("");

    Method privateStringMethod = ServletGetRivendite.class
      .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletGetRivendite, request, response);

    verify(request, times(1)).getHeader(anyString());
    verify(request, times(1)).getParameter(anyString());
    verify(request, times(1)).setAttribute(anyString(), any());

  }
}