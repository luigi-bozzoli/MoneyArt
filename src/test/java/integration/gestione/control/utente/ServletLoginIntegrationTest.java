package integration.gestione.control.utente;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletLogin;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.*;
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
class ServletLoginIntegrationTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher dispatcher;
    @Mock
    HttpSession session;

    private static DataSource dataSource;

    private ServletLogin servletLogin;
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

        servletLogin = new ServletLogin();

        //inject manuale della variabile di istanza =)
        Field injectedObject = servletLogin.getClass().getDeclaredField("utenteService");
        injectedObject.setAccessible(true);
        injectedObject.set(servletLogin, utenteService);
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
    void doPost() throws ServletException, IOException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

       Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
        utenteDao.doCreate(utente);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(utente.getUsername());
        when(request.getParameter("password")).thenReturn("stelle");

        doNothing().when(session).setAttribute(anyString(), any());
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(any(), any());
        doNothing().when(request).setAttribute(anyString(), anyString());

        Method privateStringMethod = ServletLogin.class
               .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

       privateStringMethod.setAccessible(true);
       privateStringMethod.invoke(servletLogin, request, response);


        verify(request, times(2)).getParameter(anyString());
        verify(request, times(1)).getSession();
        verify(request, times(1)).getRequestDispatcher(anyString());
        verify(dispatcher, times(1)).forward(any(), any());
        verify(request, times(1)).setAttribute(anyString(), anyString());
        verify(dispatcher, times(1)).forward(any(), any());

    }

    @Test
    @DisplayName("doPostUserNull")
    void doPostUserNull() throws ServletException, IOException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
        utenteDao.doCreate(utente);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(""); //no username
        when(request.getParameter("password")).thenReturn(""); //no password
        doNothing().when(request).setAttribute(anyString(), anyString());
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(any(), any());

        Method privateStringMethod = ServletLogin.class
                .getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);

        privateStringMethod.setAccessible(true);
        privateStringMethod.invoke(servletLogin, request, response);


        verify(request, times(2)).getParameter(anyString());
        verify(request, times(1)).getSession();
        verify(request, times(1)).getRequestDispatcher(anyString());
        verify(dispatcher, times(1)).forward(any(), any());
        verify(request, times(1)).setAttribute(anyString(), anyString());
        verify(dispatcher, times(1)).forward(any(), any());

    }

}
