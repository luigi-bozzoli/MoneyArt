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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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


    static List<Opera> getListOpere() throws SQLException {
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

    static void createOpereOnDb() throws SQLException {
        createUtentiOnDb();
        for (Opera op : getListOpere()){
            operaDao.doCreate(op);
        }
    }



    @Nested
    @DisplayName("Test Suite CRUD")
    class testCRUD {

       @DisplayName("doCreate")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doCreate(Opera opera) {
          createUtentiOnDb();
          assertTrue(operaDao.doCreate(opera));
        }

        @DisplayName("doCreateCatch")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doCreateCatch(Opera opera) {
            createUtentiOnDb();
            assertTrue(operaDao.doCreate(opera));
        }

        @DisplayName("doCreateErr")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doCreateErr(Opera opera) {
            createUtentiOnDb();
            assertTrue(!(operaDao.doCreate(null)));
        }

        @DisplayName("doCreateExistingOpera")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doCreateExistingOpera(Opera opera) {
            createUtentiOnDb();
            operaDao.doCreate(opera);
            assertTrue(!(operaDao.doCreate(opera)));
        }


        @DisplayName("doUpdate")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doUpdate(Opera opera) {
            createUtentiOnDb();
            //aggiunta opera nel db
            operaDao.doCreate(opera);
            //modifica opera
            opera.setNome("nome Modificato");
            opera.setDescrizione("descrizione Modificata");
            opera.setCertificato("GYIOGUIGUIPGPGUIGUP");
            //aggiorno opera
            operaDao.doUpdate(opera);
            //riprendo l'oggetto dal db
            Opera result = operaDao.doRetrieveById(opera.getId());

            //verifico che l'aggiornamento ha funzionato
            assertTrue(opera.getId() == result.getId());

        }


        @DisplayName("doDelete")
        @Test
        void doDelete() throws SQLException {
            createOpereOnDb(); //mi crea 3 opere nel db
            operaDao.doDelete(getListOpere().get(0)); //se va a buon fine me ne dovrebbero rimanere solo 2
            assertTrue(operaDao.doRetrieveAll(null).size()==2);
        }

    }

    @Nested
    @DisplayName("Test Suite Retrieve")
    class testRetrieve {

        @DisplayName("doRetriveById")
        @ParameterizedTest
        @ArgumentsSource(OperaProvider.class)
        void doRetriveById(Opera opera) throws SQLException {
            createOpereOnDb();
            Opera result = operaDao.doRetrieveById(opera.getId());
            System.out.println(result); //print
            System.out.println(opera); //print
            assertTrue(result.getId() == opera.getId());

        }

        @DisplayName("doRetriveAll")
        @Test
        void doRetriveAll() throws SQLException {
            createOpereOnDb();
            List<Opera> opereRisultato;

            opereRisultato = operaDao.doRetrieveAll("");

            for (Opera o : opereRisultato) System.out.println("\n"+o); //print
            for (Opera o : getListOpere()) System.out.println("\n"+o); //print

            assertTrue( (getListOpere().get(0).getId() == opereRisultato.get(0).getId())
                    && (getListOpere().get(1).getId() == opereRisultato.get(1).getId())
                    && (getListOpere().get(2).getId() == opereRisultato.get(2).getId()) );

        }


        @DisplayName("doRetrieveAllByOwnerId")
        @Test
        void doRetrieveAllByOwnerId() throws SQLException {
            createOpereOnDb();
            Utente utente = getUtenti().get(0); //questo utente possiede 2 opere

            //creao un'altra opera (con questa utente ne ha 2) e la creo nel db
            String s1 = "image blob 1";
            Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
            Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
                    b1, utente, getUtenti().get(1), "xxxxx");
            o1.setId(7);
            operaDao.doCreate(o1);

            //prendo la lista che mi restituisce il metodo doRetrieveAllByOwnerId su utente
            List <Opera> result = operaDao.doRetrieveAllByOwnerId(utente.getId());

            //per ogni opera della lista result verifico se il possessore è proprio utente
            for (Opera o : result) assertTrue(o.getPossessore().getId()==utente.getId());

        }

        @DisplayName("doRetrieveAllByOwnerId")
        @Test
        void doRetrieveAllByArtistId() throws SQLException {
            createOpereOnDb();
            Utente artista = getUtenti().get(0); //questo utente ha creato 2 opere

            //prendo la lista che mi restituisce il metodo doRetrieveAllByArtistId su utente
            List <Opera> result = operaDao.doRetrieveAllByArtistId(artista.getId());

            //per ogni opera della lista result verifico se il possessore è proprio utente
            for (Opera o : result) assertTrue(o.getArtista().getId()==artista.getId());

        }

        /*le opere di uno stesso artista non possono avere lo stesso nome*/
        @DisplayName("doRetrieveAllByName")
        @Test
        void doRetrieveAllByName() throws SQLException {
            createOpereOnDb();
            Opera opera = getListOpere().get(0); //verifico se ci sono altre opere con il nome uguale a questa

            System.out.println(opera);

           //creo due opere con lo stesso nome dell'opera già presente nel db (opera) e le salvo nel db
            String s1 = "image blob 1";
            Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
            Utente u1 = getUtenti().get(0);
            Opera o1 = new Opera("The Shibosis", "DescrizioneXXXXXXX", Opera.Stato.PREVENDITA,
                    b1, u1, getUtenti().get(2), "xxxxx7777777777777777");
            o1.setId(11);
            operaDao.doCreate(o1);

            String s2 = "image blob 2";
            Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
            Utente u2 = getUtenti().get(1);
            Opera o2 = new Opera("The Shibosis", "DescrizioneYYYYYYY", Opera.Stato.PREVENDITA,
                    b2, u2, getUtenti().get(1), "yyyyyyyyy777777777777777777");
            o2.setId(12);
            operaDao.doCreate(o2);

            //prendo la lista che mi restituisce il metodo doRetrieveAllByName sul nome di opera
            List <Opera> result = operaDao.doRetrieveAllByName(opera.getNome());
            System.out.println(result);

            //per ogni opera della lista result verifico se il nome è proprio Shibosis
            for (Opera o : result) assertTrue(o.getNome().equals(opera.getNome()));

        }

    }

}