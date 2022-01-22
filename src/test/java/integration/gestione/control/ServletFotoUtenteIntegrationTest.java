package integration.gestione.control;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletFotoUtente;
import it.unisa.c02.moneyart.gestione.utente.control.ServletLogin;
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
import org.apache.commons.io.IOUtils;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletFotoUtenteIntegrationTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher dispatcher;
    @Mock
    HttpSession session;

    private static DataSource dataSource;

    private ServletFotoUtente servletFotoUtente;
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

        servletFotoUtente = new ServletFotoUtente();

        //inject manuale della variabile di istanza =)
        Field injectedObject = servletFotoUtente.getClass().getDeclaredField("utenteService");
        injectedObject.setAccessible(true);
        injectedObject.set(servletFotoUtente, utenteService);
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
    @DisplayName("doGet")
    void doGet() throws ServletException, IOException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, SQLException {

        String s1 = "image blob 1";
        Blob image = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));

        Utente utente = new Utente("Stefano", "Zarro", image, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
        utenteDao.doCreate(utente);

        doNothing().when(response).setContentType(anyString());
        when(request.getParameter("id")).thenReturn(Integer.toString(utente.getId()));

        doNothing().when(response).setContentLength(anyInt());

        ServletOutputStream writer = mock(ServletOutputStream.class); //moccato
        when(response.getOutputStream()).thenReturn(writer);
        doNothing().when(writer).write(anyByte());

        Method privateStringMethod = ServletFotoUtente.class
                .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        privateStringMethod.setAccessible(true);
        privateStringMethod.invoke(servletFotoUtente, request, response);

        verify(request, times(1)).getParameter(anyString());
        verify(response, times(1)).setContentType(anyString());
        verify(response, times(1)).setContentLength(anyInt());
        verify(response, times(1)).getOutputStream();

    }


    @Test
    @DisplayName("doGetImgNull")
    void doGetImgNull() throws ServletException, IOException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, SQLException {

        String s1 = "image blob 1";
        Blob image = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));

        Utente utente = new Utente("Stefano", "Zarro", image, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);

        utente.setFotoProfilo(null);
        utenteDao.doCreate(utente);

        doNothing().when(response).setContentType(anyString());
        when(request.getParameter("id")).thenReturn(Integer.toString(utente.getId()));

        //if img==null ******************************************************************************

        ServletContext ctx = mock(ServletContext.class);
        when(session.getServletContext()).thenReturn(ctx); //ritorna qualcosa
        InputStream in = mock(InputStream.class);
        when(ctx.getResourceAsStream(anyString())).thenReturn(in); //ritorna qualsiasi cosa
        IOUtils utils = mock(IOUtils.class);
        when(utils.toByteArray(in)).thenReturn(s1.getBytes(StandardCharsets.UTF_8));

        // ******************************************************************************

        doNothing().when(response).setContentLength(anyInt());

        ServletOutputStream writer = mock(ServletOutputStream.class); //moccato
        when(response.getOutputStream()).thenReturn(writer);
        doNothing().when(writer).write(anyByte());

        Method privateStringMethod = ServletFotoUtente.class
                .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        privateStringMethod.setAccessible(true);
        privateStringMethod.invoke(servletFotoUtente, request, response);


        verify(request, times(1)).getParameter(anyString());
        verify(response, times(1)).setContentType(anyString());
        verify(response, times(1)).setContentLength(anyInt());
        verify(response, times(1)).getOutputStream().write(anyByte());
    }


    @Test
    @DisplayName("doPost Test")
    void doPost() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ServletException, IOException, SQLException {
        doGet();
    }
}