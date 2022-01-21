package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("OperaDaoImplIntegrationTest")
class OperaDaoImplIntegrationTest {

    private static DataSource dataSource;

    private static UtenteDao utenteDao;
    private static OperaDao operaDao;

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
        // Creazione database
        Connection connection = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/model/db/ddl_moneyart.sql"));
        runner.runScript(reader);
        connection.close();
    }

    @BeforeEach
    public void setUp() {
        operaDao = new OperaDaoImpl(dataSource);
        utenteDao = new UtenteDaoImpl(dataSource);
    }

   @AfterEach
    public void tearDown() throws SQLException, FileNotFoundException {
        Connection connection = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
        runner.runScript(reader);
        connection.close();
    }

    static class OperaProvider implements ArgumentsProvider{

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

            String s1 = "image blob 1";
            Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
            Utente u1 = getUtenti().get(0);
            Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
                    b1, u1, u1, "xxxxx");
            o1.setId(1);

            String s2 = "image blob 2";
            Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
            Utente u2 = getUtenti().get(1);
            Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
                    b2, u2, u1, "yyyyyyyyy");
            o2.setId(2);

            String s3 = "image blob 3";
            Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
            Utente u3 = getUtenti().get(2);
            Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
                    b3, u3, u2, "zzzzzz");
            o3.setId(3);

            return Stream.of(
                    Arguments.of(o1),
                    Arguments.of(o2),
                    Arguments.of(o3)
            );

        }
    }

    //questo metodo si può anche eliminare
    static class ListOpereProvider implements ArgumentsProvider{

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            List<Opera> opere = getListOpere();

            return Stream.of(Arguments.of(opere));
        }
    }

    static List<Opera> getListOpere() throws SQLException {
        String s1 = "image blob 1";
        Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
        Utente u1 = new Utente();
        u1.setId(1);
        Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
                b1, u1, u1, "xxxxx");
        o1.setId(1);

        String s2 = "image blob 2";
        Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
        Utente u2 = new Utente();
        u2.setId(2);
        Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
                b2, u2, u1, "yyyyyyyyy");
        o2.setId(2);

        String s3 = "image blob 3";
        Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
        Utente u3 = new Utente();
        u3.setId(3);
        Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
                b3, u3, u1, "zzzzzz");
        o3.setId(3);

        List<Opera> opere = new ArrayList<>();
        opere.add(o1);
        opere.add(o2);
        opere.add(o3);

        return opere;
    }

    //ritorna una lista di utenti già creati
    static List<Utente> getUtenti () {

        Utente u1 = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                "mpelGYUgugyugYUG", new Utente(), new byte[10], 2000000.2);
        u1.setId(1);

        Utente u2 = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                "vuivuovuuGYUGIY", new Utente(), new byte[10], 2.2);
        u2.setId(2);

        Utente u3 = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "vghuivuiVGHUIVUI", new Utente(), new byte[10], 0.002);
        u3.setId(3);

        List<Utente> utenti = Arrays.asList(u1, u2, u3);

        return utenti;
    }

    static void createUtentiOnDb() {
        for (Utente u : getUtenti()) {
            utenteDao.doCreate(u);
        }
    }

    //__________________________________________________________________________________________________________________________________________________________________________


    @Nested
    @DisplayName("Test Suite DoCreate")
    class testDoCreate {

       @DisplayName("doCreate")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doCreate(Opera opera) {
          createUtentiOnDb();
          assertTrue(operaDao.doCreate(opera));
        }


    }

 /*

    @Test
    void doCreate() {
    }

    @Test
    void doRetrieveById() {
    }

    @Test
    void doRetrieveAll() {
    }

    @Test
    void doUpdate() {
    }

    @Test
    void doDelete() {
    }

    @Test
    void doRetrieveAllByOwnerId() {
    }

    @Test
    void doRetrieveAllByArtistId() {
    }

    @Test
    void doRetrieveAllByName() {

   }

*/

}