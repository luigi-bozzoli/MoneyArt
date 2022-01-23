package integration.gestione.control.rivendite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.control.ServletGetValoreOpera;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.control.ServletRivendita;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.ContractProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletRivenditaIntegrationTest {

  private static DataSource dataSource;
  private RivenditaService service;
  private OperaService operaService;
  ServletRivendita servletRivendita;

  private OperaDao operaDao;
  private NotificaDao notificaDao;
  private UtenteDao utenteDao;
  private RivenditaDao rivenditaDao;
  private AstaDao astaDao;
  private PartecipazioneDao partecipazioneDao;

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  HttpSession session;
  @Mock
  RequestDispatcher dispatcher;
  @Mock
  PrintWriter printWriter;

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
  public void setUp() throws SQLException, FileNotFoundException, NoSuchFieldException, IllegalAccessException {

    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_rivendita.sql"));
    runner.runScript(reader);
    connection.close();

    operaDao = new OperaDaoImpl(dataSource);
    notificaDao = new NotificaDaoImpl(dataSource);
    rivenditaDao = new RivenditaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    astaDao = new AstaDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);

    service = new RivenditaServiceImpl(utenteDao, operaDao, rivenditaDao, notificaDao,astaDao,partecipazioneDao);

    MoneyArtNft moneyArtNft = (new ContractProducer()).contractInizializer();
    operaService = new OperaServiceImpl(operaDao,moneyArtNft);
    servletRivendita = new ServletRivendita();

    Field injectedObject = servletRivendita.getClass().getDeclaredField("rivenditaService");
    injectedObject.setAccessible(true);
    injectedObject.set(servletRivendita, service);
    Field injectedObject2 = servletRivendita.getClass().getDeclaredField("operaService");
    injectedObject2.setAccessible(true);
    injectedObject2.set(servletRivendita, operaService);
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
  @DisplayName("doGet Success Test")
  void doGetSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      ServletException, IOException {

    Utente utente = utenteDao.doRetrieveById(3);

    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter(anyString())).thenReturn("1");
    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    Method privateStringMethod = ServletRivendita.class.
        getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletRivendita, request, response);
    Opera opera = operaDao.doRetrieveById(1);

    Assertions.assertEquals(opera.getStato(), Opera.Stato.IN_VENDITA);

  }

  @Test
  @DisplayName("doGet Fail Test")
  void doGetFail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      ServletException, IOException {

    Utente utente = utenteDao.doRetrieveById(2);

    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter(anyString())).thenReturn("1");
    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    Method privateStringMethod = ServletRivendita.class.
        getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletRivendita, request, response);
    Opera opera = operaDao.doRetrieveById(1);

    Assertions.assertEquals(opera.getStato(), Opera.Stato.IN_POSSESSO);

  }

  @Test
  void doPost()
      throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Utente utente = utenteDao.doRetrieveById(3);

    when(session.getAttribute("utente")).thenReturn(utente);
    when(request.getParameter(anyString())).thenReturn("1");
    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

    Method privateStringMethod = ServletRivendita.class.
        getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

    privateStringMethod.setAccessible(true);
    privateStringMethod.invoke(servletRivendita, request, response);
    Opera opera = operaDao.doRetrieveById(1);

    Assertions.assertEquals(opera.getStato(), Opera.Stato.IN_VENDITA);

  }

}