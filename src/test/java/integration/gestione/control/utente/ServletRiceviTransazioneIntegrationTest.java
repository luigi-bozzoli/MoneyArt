package integration.gestione.control.utente;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.control.ServletRiceviTransazione;
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
import it.unisa.c02.moneyart.utils.payment.PayPalPayment;
import it.unisa.c02.moneyart.utils.payment.PaymentAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayment;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServletRiceviTransazioneIntegrationTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher dispatcher;
    @Mock
    HttpSession session;

    private static DataSource dataSource;

    private ServletRiceviTransazione servletRiceviTransazione;
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

        servletRiceviTransazione = new ServletRiceviTransazione();

        //inject manuale della variabile di istanza =)
        Field injectedObject = servletRiceviTransazione.getClass().getDeclaredField("utenteService");
        injectedObject.setAccessible(true);
        injectedObject.set(servletRiceviTransazione, utenteService);
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
    @DisplayName("doGetStripePaymentCatch Test")
    void doGetStripePaymentCatch() throws Exception {

        Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
        utenteDao.doCreate(utente);

        //creo il pagamento (il pagamento può essere o Stripe o Paypal, quindi uno dei due dovrà essere sempre nullo)
        StripePayment sp = new StripePayment();
        String stripeId = sp.makePayment(345.98);
        PaymentAdapter paymentAdapter = mock(PaymentAdapter.class);
        when(paymentAdapter.recievePayment(anyString())).thenReturn(345.98);

        when(request.getParameter("session_id")).thenReturn(stripeId); //stripeId
        when(request.getParameter("paymentId")).thenReturn(null); //payPalId --> null, perchè è stato usato Stripe
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);
        //if(stripeId != null) entriamo in questo ramo
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(any(), any());

        //se per caso entriamo nel catch
        doNothing().when(response).sendError(anyInt(), anyString());


        Method privateStringMethod = ServletRiceviTransazione.class
                .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        privateStringMethod.setAccessible(true);
        privateStringMethod.invoke(servletRiceviTransazione, request, response);

        verify(request, times(2)).getParameter(anyString());
        verify(request, times(1)).getSession();
        verify(session, times(1)).getAttribute(anyString());
        verify(response, times(1)).sendError(anyInt(), anyString());

    }

    @Test
    @DisplayName("doGetPayPalPaymentCatch Test")
    void doGetPayPalPaymentCatch() throws Exception {

        Utente utente = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "s_ano", new Utente(), "stelle".getBytes(StandardCharsets.UTF_8), 0.002);
        utenteDao.doCreate(utente);

        //creo il pagamento (il pagamento può essere o Stripe o Paypal, quindi uno dei due dovrà essere sempre nullo)
        PayPalPayment pp = new PayPalPayment();
        String payPalId = pp.makePayment(345.98);
        PaymentAdapter paymentAdapter = mock(PaymentAdapter.class);
        when(paymentAdapter.recievePayment(payPalId)).thenReturn(345.98);

        when(request.getParameter("session_id")).thenReturn(null); //stripeId --> null, perchè è stato usato Paypal
        when(request.getParameter("paymentId")).thenReturn(payPalId); //payPalId
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);
        //else ---> (stripeId == null) --> entriamo in questo ramo
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(any(), any());

        //se per caso entriamo nel catch
        doNothing().when(response).sendError(anyInt(), anyString());


        Method privateStringMethod = ServletRiceviTransazione.class
                .getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        privateStringMethod.setAccessible(true);
        privateStringMethod.invoke(servletRiceviTransazione, request, response);


        verify(request, times(2)).getParameter(anyString());
        verify(request, times(1)).getSession();
        verify(session, times(1)).getAttribute(anyString());
        verify(response, times(1)).sendError(anyInt(), anyString());
    }

    @Test
    @DisplayName("doPost Test")
    void doPost() throws Exception {
        doGetPayPalPaymentCatch();
    }

}