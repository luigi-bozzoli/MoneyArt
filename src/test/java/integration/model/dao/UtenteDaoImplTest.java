package integration.model.dao;

import static com.google.inject.internal.util.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

@DisplayName("UtenteDao")
class UtenteDaoImplTest {

  private static DataSource dataSource;

  private UtenteDao utenteDao;

  @BeforeAll
  public static void generalSetUp() {
    TomcatJNDI tomcatJNDI = new TomcatJNDI();
    tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
    tomcatJNDI.start();
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      dataSource = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  public void setUp() {
    utenteDao = new UtenteDaoImpl(dataSource);
  }

  @AfterEach
  public void tearDown() throws SQLException, IOException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/ddl_moneyart.sql"));
    runner.runScript(reader);
    connection.close();

  }

  static class UtenteProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      return Stream.of(
          Arguments.of(new Utente(
              "Franco",
              "Battiato",
              null,
              "prova@moneyart.it",
              "franco",
              new Utente(),
              md.digest("admin".getBytes()),
              0d
          )),
          Arguments.of(new Utente(
              "Aurelio",
              "Sepe",
              null,
              "aurelio@moneyart.it",
              "aury",
              new Utente(),
              md.digest("aurelioBello".getBytes()),
              0d)),
          Arguments.of(new Utente(
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

  @Nested
  @DisplayName("Create")
  class CreateTest {

    @DisplayName("Create New User")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void doCreateNewUser(Utente utente) {
      utenteDao.doCreate(utente);
      Utente admin2 = utenteDao.doRetrieveById(utente.getId());
      assertEquals(utente, admin2);
    }


    @DisplayName("Create Exsisting User")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void doCreateExistingUser(Utente utente) {
      Utente utente2 = new Utente(
          utente.getNome(),
          utente.getCognome(),
          utente.getFotoProfilo(),
          utente.getEmail(),
          utente.getUsername(),
          utente.getSeguito(),
          utente.getPassword(),
          utente.getSaldo()
      );
      utenteDao.doCreate(utente);
      utenteDao.doCreate(utente2);

      Assertions.assertNull(utente2.getId());
    }


  }

  @Nested
  @DisplayName("Retrieve")
  class RetrieveTest {


    @ParameterizedTest
    @DisplayName("Retrieve existing user")
    @ArgumentsSource(UtenteProvider.class)
    void retriveExistingUser(Utente utente) {
      utenteDao.doCreate(utente);

      Utente result = utenteDao.doRetrieveById(utente.getId());
      assertEquals(result, utente);
    }

    @ParameterizedTest
    @DisplayName("Retrieve non existing user")
    @ArgumentsSource(UtenteProvider.class)
    void retirveNonExistingUser(Utente utente) {
      utenteDao.doCreate(utente);

      Utente result = utenteDao.doRetrieveById(utente.getId() + 1);
      Assertions.assertNull(result);
    }

  }


  private static class ListUsersProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {

      MessageDigest md = MessageDigest.getInstance("SHA-256");
      //lista 1
      Utente utente0 = new Utente(
          "Alfonso",
          "Cannavale",
          null,
          "alfonso.cannavale@gmail.com",
          "alfcan",
          new Utente(),
          md.digest("pippo123".getBytes()),
          1000d
      );

      Utente utente1 = new Utente(
          "Nicolò",
          "Delogu",
          null,
          "nicolò.delogu@gmail.com",
          "XJustUnluckyX",
          utente0,
          md.digest("pippo123".getBytes()),
          1500d
      );

      Utente utente2 = new Utente(
          "Michael",
          "De Santis",
          null,
          "michael.desantis@gmail.com",
          "shoyll",
          utente0,
          md.digest("pippo123".getBytes()),
          2000d
      );

      Utente utente3 = new Utente(
          "Daniele",
          "Galloppo",
          null,
          "daniele.galloppo@gmail.com",
          "DG266",
          utente2,
          md.digest("pippo123".getBytes()),
          2500d
      );

      List<Utente> utenti1 = of(utente0, utente1, utente2, utente3);
      Utente utente4 = new Utente(
          "Dario",
          "Mazza",
          null,
          "dario.mazza@gmail.com",
          "xDaryamo",
          new Utente(),
          md.digest("pippo123".getBytes()),
          3000d
      );

      utente4.setSaldoDisponibile(2514d);

      Utente utente5 = new Utente(
          "Mario",
          "Peluso",
          null,
          "mario.peluso@gmail.com",
          "MarioPeluso",
          new Utente(),
          md.digest("pippo123".getBytes()),
          956d
      );

      utente5.setSaldoDisponibile(456.01d);

      Utente utente6 = new Utente(
          "Aurelio",
          "Sepe",
          null,
          "aurelio.sepe@gmail.com",
          "AurySepe",
          utente5,
          md.digest("pippo123".getBytes()),
          200d
      );

      Utente utente7 = new Utente(
          "Stefano",
          "Zarro",
          null,
          "stefano.zarro@gmail.com",
          "stepzar",
          utente6,
          md.digest("pippo123".getBytes()),
          10d
      );
      List<Utente> utenti2 = of(utente4, utente5, utente6, utente7);
      return Stream.of(Arguments.of(utenti1), Arguments.of(utenti2),
          Arguments.of(new ArrayList<Utente>()));
    }
  }

  @Nested
  @DisplayName("Retrive All")
  class RetrieveAllTest {

    @ParameterizedTest
    @DisplayName("Retrieve all")
    @ArgumentsSource(ListUsersProvider.class)
    void RetireveAllTest(List<Utente> utenti) {
      for (Utente utente : utenti) {
        utenteDao.doCreate(utente);
        Utente followed = new Utente();
        followed.setId(utente.getSeguito().getId());
        utente.setSeguito(followed);
      }
      List<Utente> result = utenteDao.doRetrieveAll(null);
      Assertions.assertArrayEquals(utenti.toArray(), result.toArray());

    }
  }


  @ParameterizedTest
  @DisplayName("Update")
  @ArgumentsSource(UtenteProvider.class)
  void doUpdate(Utente utente) {

    //prima aggiungo l'utente nel db
    utenteDao.doCreate(utente);
    //modifico l'utente
    utente.setNome("Prova");
    utente.setCognome("Prova");
    utente.setSaldo(100d);
    utente.setEmail("prova@gmail.com");
    //aggiorno l'utente
    utenteDao.doUpdate(utente);
    //riprendo l'oggetto dal db
    Utente result = utenteDao.doRetrieveById(utente.getId());
    //verifico che l'aggiornamento ha funzionato
    assertEquals(utente, result);
  }

  @ParameterizedTest
  @DisplayName("Update")
  @ArgumentsSource(UtenteProvider.class)
  void doDelete(Utente utente) {

    //aggiungo l'utente al db
    utenteDao.doCreate(utente);
    //verifico che è stato aggiunto
    Utente result = utenteDao.doRetrieveById(utente.getId());
    Assertions.assertNotNull(result);
    //elimino l'elemento
    utenteDao.doDelete(utente);
    //verifico che è stato eliminato
    result = utenteDao.doRetrieveById(utente.getId());
    Assertions.assertNull(result);
  }

  @ParameterizedTest
  @DisplayName("Retrieve By Username")
  @ArgumentsSource(UtenteProvider.class)
  void doRetrieveByUsername(Utente utente) {

    //aggiungo l'utente al db
    utenteDao.doCreate(utente);
    //estraggo l'utente dal db usando l'username
    Utente result = utenteDao.doRetrieveByUsername(utente.getUsername());
    //verifica che sono uguali
    Assertions.assertEquals(utente, result);

  }

  @ParameterizedTest
  @DisplayName("Retrieve By Email")
  @ArgumentsSource(UtenteProvider.class)
  void doRetrieveByEmail(Utente utente) {
    //aggiungo l'utente al db
    utenteDao.doCreate(utente);
    //estraggo l'utente dal db usando l'email
    Utente result = utenteDao.doRetrieveByEmail(utente.getEmail());
    //verifica che sono uguali
    Assertions.assertEquals(utente, result);
  }

  private static class ListUsersAndNamesProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {

      MessageDigest md = MessageDigest.getInstance("SHA-256");
      //lista 1
      Utente utente0 = new Utente(
          "Alfonso",
          "Cannavale",
          null,
          "alfonso.cannavale@gmail.com",
          "alfcan",
          new Utente(),
          md.digest("pippo123".getBytes()),
          1000d
      );

      Utente utente1 = new Utente(
          "Nicolò",
          "Delogu",
          null,
          "nicolò.delogu@gmail.com",
          "XJustUnluckyX",
          utente0,
          md.digest("pippo123".getBytes()),
          1500d
      );

      Utente utente2 = new Utente(
          "Michael",
          "De Santis",
          null,
          "michael.desantis@gmail.com",
          "shoyll",
          utente0,
          md.digest("pippo123".getBytes()),
          2000d
      );

      Utente utente3 = new Utente(
          "Daniele",
          "Galloppo",
          null,
          "daniele.galloppo@gmail.com",
          "DG266",
          utente2,
          md.digest("pippo123".getBytes()),
          2500d
      );

      Utente utente4 = new Utente(
          "Dario",
          "Mazza",
          null,
          "dario.mazza@gmail.com",
          "xDaryamo",
          new Utente(),
          md.digest("pippo123".getBytes()),
          3000d
      );


      Utente utente5 = new Utente(
          "Mario",
          "Peluso",
          null,
          "mario.peluso@gmail.com",
          "MarioPeluso",
          new Utente(),
          md.digest("pippo123".getBytes()),
          956d
      );

      utente5.setSaldoDisponibile(456.01d);

      Utente utente6 = new Utente(
          "Aurelio",
          "Sepe",
          null,
          "aurelio.sepe@gmail.com",
          "AurySepe",
          utente5,
          md.digest("pippo123".getBytes()),
          200d
      );

      Utente utente7 = new Utente(
          "Stefano",
          "Zarro",
          null,
          "stefano.zarro@gmail.com",
          "stepzar",
          utente6,
          md.digest("pippo123".getBytes()),
          10d
      );
      List<Utente> utenti1 = of(utente2, utente7, utente5);
      List<Utente> utenti2 = of(utente0, utente6, utente2);
      List<Utente> utenti3 = of(utente2, utente5, utente1);

      //oracoli
      List<Utente> oracolo1 = of(utente2, utente7);
      List<Utente> oracolo2 = of(utente7);
      List<Utente> oracolo3 = of(utente0, utente6);

      return Stream.of(Arguments.of(utenti1, "s", oracolo1),
          Arguments.of(utenti1, "step", oracolo2), Arguments.of(utenti2, "A", oracolo3),
          Arguments.of(utenti3, "prova", new ArrayList<Utente>()),
          Arguments.of(new ArrayList<Utente>(), "prova", new ArrayList<Utente>()),
          Arguments.of(new ArrayList<Utente>(),"",new ArrayList<Utente>()));
    }
  }

  @ParameterizedTest
  @DisplayName("Ricerca utente")
  @ArgumentsSource(ListUsersAndNamesProvider.class)
  void researchUser(List<Utente> utenti,String username,List<Utente> oracolo) {
    //aggiungo al db la lista di utenti
    for (Utente utente : utenti) {
      utenteDao.doCreate(utente);
      Utente followed = new Utente();
      followed.setId(utente.getSeguito().getId());
      utente.setSeguito(followed);
    }
    //ottengo il risultato della ricerca
    List<Utente> ricercati = utenteDao.researchUser(username);
    //verifico che corrisponde all'oracolo
    Assertions.assertEquals(oracolo.size(),ricercati.size());
    Assertions.assertTrue(ricercati.containsAll(oracolo));

  }

  @Test
  void doRetrieveFollowersByUserId() {
  }


}