package integration.model.dao;

import com.mysql.cj.jdbc.Blob;
import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
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
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("PartecipazioneDao")
public class PartecipazioneDaoImplIntegrationTest {

  private static DataSource dataSource;

  private PartecipazioneDao partecipazioneDao;

  @BeforeAll
  public static void generalSetUp() {
    Context initCtx = null;
    Context envCtx = null;
    try{
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if(envCtx == null){
      try{
        initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
        dataSource = (DataSource) envCtx.lookup("jdbc/storage");
      } catch (NamingException e) {
        e.printStackTrace();
      }
    }
  }

  @BeforeEach
  public void setUp() throws SQLException, FileNotFoundException {
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/model/db/ddl_moneyart.sql"));
    runner.runScript(reader);
    connection.close();

    try {
      populateDBTest();
    } catch (NoSuchAlgorithmException | SQLException e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_all.sql"));
    runner.runScript(reader);
    connection.close();
  }

  static class PartecipazioneProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Asta a1 = new Asta();
      a1.setId(1);

      Utente u1 = new Utente();
      u1.setId(1);

      Utente u2 = new Utente();
      u2.setId(2);


      return Stream.of(
              Arguments.of(new Partecipazione(
                      a1,
                      u1,
                      99d
              )),
              Arguments.of(new Partecipazione(
                      a1,
                      u2,
                      100d
              )),
              Arguments.of(new Partecipazione(
                      a1,
                      u1,
                      105d
              ))
      );
    }

  }

  static class ListPartecipazioneProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Asta a1 = new Asta();
      a1.setId(1);

      Utente u1 = new Utente();
      u1.setId(1);

      Utente u2 = new Utente();
      u2.setId(2);

      List<Partecipazione> list = new ArrayList<>();
      list.add(new Partecipazione(a1,u1,99d));
      list.add(new Partecipazione(a1,u2,100d));
      list.add(new Partecipazione(a1,u1,105d));

      return Stream.of(Arguments.of(list));
    }

  }

  @DisplayName("Create Partecipazione")
  @ParameterizedTest
  @ArgumentsSource(PartecipazioneProvider.class)
  void doCreatePartecipazione(Partecipazione p){
    partecipazioneDao.doCreate(p);
    Partecipazione result = partecipazioneDao.doRetrieveById(p.getId());

    Assertions.assertEquals(p, result);
  }

  @Nested
  @DisplayName("Retrieve Test Suite")
  class RetrieveTest {

    @ParameterizedTest
    @DisplayName("Retrieve existing Partecipazione")
    @ArgumentsSource(PartecipazioneProvider.class)
    void retrieveExistingUser(Partecipazione p) {
      partecipazioneDao.doCreate(p);

      Partecipazione result = partecipazioneDao.doRetrieveById(p.getId());
      assertEquals(result, p);
    }

    @ParameterizedTest
    @DisplayName("Retrieve non existing Partecipazione")
    @ArgumentsSource(PartecipazioneProvider.class)
    void retrieveNonExistingUser(Partecipazione p) {
      partecipazioneDao.doCreate(p);

      Partecipazione result = partecipazioneDao.doRetrieveById(p.getId() + 1);
      assertNull(result);
    }

  }

  @ParameterizedTest
  @DisplayName("Retrieve All")
  @ArgumentsSource(ListPartecipazioneProvider.class)
  void RetrieveAllTest(List<Partecipazione> partecipazioni) {
    for (Partecipazione p : partecipazioni) {
      partecipazioneDao.doCreate(p);
    }

    List<Partecipazione> result = partecipazioneDao.doRetrieveAll(null);

    Assertions.assertArrayEquals(partecipazioni.toArray(), result.toArray());
  }

  @DisplayName("Retrieve By Auction")
  @ParameterizedTest
  @ArgumentsSource(PartecipazioneProvider.class)
  void RetrieveByAuctionId(Partecipazione p){
    partecipazioneDao.doCreate(p);

    List<Partecipazione> result = partecipazioneDao.doRetrieveAllByAuctionId(p.getAsta().getId());
    Assertions.assertEquals(p, result.get(0));
  }

  @DisplayName("Retrieve By User")
  @ParameterizedTest
  @ArgumentsSource(PartecipazioneProvider.class)
  void RetrieveByUserId(Partecipazione p){
    partecipazioneDao.doCreate(p);

    List<Partecipazione> result = partecipazioneDao.doRetrieveAllByUserId(p.getUtente().getId());
    Assertions.assertEquals(p, result.get(0));
  }

  @Test
  @DisplayName("Update Test")
  void doUpdate(){
    Asta a = new Asta();
    a.setId(1);

    Utente u = new Utente();
    u.setId(1);

    Partecipazione p = new Partecipazione(a, u, 999d);
    partecipazioneDao.doCreate(p);
    p.setOfferta(1254d);
    partecipazioneDao.doUpdate(p);

    Partecipazione result = partecipazioneDao.doRetrieveById(p.getId());

    Assertions.assertEquals(p, result);
  }

  @Test
  @DisplayName("Delete Test")
  void doDelete(){
    Asta a = new Asta();
    a.setId(1);

    Utente u = new Utente();
    u.setId(1);

    Partecipazione p = new Partecipazione(a, u, 999d);
    partecipazioneDao.doCreate(p);

    partecipazioneDao.doDelete(p);
    Partecipazione result = partecipazioneDao.doRetrieveById(p.getId());

    Assertions.assertNull(result);
  }

  private static void populateDBTest() throws NoSuchAlgorithmException, SQLException {
    AstaDao astaDao = new AstaDaoImpl(dataSource);
    UtenteDao utenteDao = new UtenteDaoImpl(dataSource);
    OperaDao operaDao = new OperaDaoImpl(dataSource);

    MessageDigest md = MessageDigest.getInstance("SHA-256");

    Utente u1 = new Utente("Franco","Battiato",null,"prova@moneyart.it",
            "franco", new Utente(), md.digest("Franchtiello".getBytes()),10000d);
    Utente u2 = new Utente("Alfonso","Cannavale",null,"alfonso@moneyart.it",
            "alfcan", new Utente(), md.digest("Alfonsino".getBytes()), 9900d);
    Utente u3 = new Utente("Aurelio","Sepe",null,"aurelio@moneyart.it",
            "aury",new Utente(), md.digest("aurelioBello".getBytes()),1000d);

    utenteDao.doCreate(u1);
    utenteDao.doCreate(u2);
    utenteDao.doCreate(u3);

    Blob blob = (Blob) dataSource.getConnection().createBlob();
    Opera opera = new Opera("PIXELARTFOREVER","La pixel art", Opera.Stato.ALL_ASTA,
            blob, u3, u3, "ccccc");

    operaDao.doCreate(opera);

    Asta asta = new Asta(opera, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), Asta.Stato.IN_CORSO);

    astaDao.doCreate(asta);
  }

}
