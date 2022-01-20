package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OperaDaoImplIntegrationTest {

    private static DataSource dataSource;

    private OperaDao operaDao;

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
/*
    static class OperaProvider implements ArgumentsProvider {


        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");


            return Stream.of(
                    Arguments.of(new Opera(
                            "Stelle che cadono",
                            "Ci sono stelle che cadono",
                            "IN_POSSESSO", // lo stato ci va qui
                            "prova@moneyart.it", //immagine
                            "franco", // possessore
                            new Utente(), //artista
                            md.digest("admin".getBytes()), //certificato
                            0d
                    )),
                    Arguments.of(new Opera(
                            "Aurelio",
                            "Sepe",
                            null,
                            "aurelio@moneyart.it",
                            "aury",
                            new Utente(),
                            md.digest("aurelioBello".getBytes()),
                            0d)),
                    Arguments.of(new Opera(
                            "Alfonso",
                            "Cannavale",
                            null,
                            "alfonso@moneyart.it",
                            "alfcan",
                            new Utente(),
                            md.digest("Alfonsino".getBytes()),
                            0d)
                    ));
        }
    }



















    //__________________________________________________________________________________________________________________________________________________________________________

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

 }}
 */

}