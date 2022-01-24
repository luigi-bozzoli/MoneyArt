package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UtenteServiceImplIntegrationTest {

  private static DataSource dataSource;

  private UtenteDao utenteDao;
  private OperaDao operaDao;
  private NotificaDao notificaDao;
  private PartecipazioneDao partecipazioneDao;

  private UtenteService utenteService;

  @BeforeAll
  public static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx;
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
  void setUp() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/test_utente.sql"));
    runner.runScript(reader);
    connection.close();

    notificaDao = new NotificaDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);

    utenteService = new UtenteServiceImpl(utenteDao, operaDao, notificaDao, partecipazioneDao);
  }

  @AfterEach
  void tearDown() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
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
                      "sonoadmin@moneyart.it",
                      "francoadmin",
                      new Utente(),
                      md.digest("admin".getBytes()),
                      0d
              ), "admin"),
              Arguments.of(new Utente(
                      "Aurelio",
                      "Sepe",
                      null,
                      "aurelio.1@moneyart.it",
                      "auryzepe",
                      new Utente(),
                      md.digest("aurelioBello".getBytes()),
                      0d), "aurelioBello"),
              Arguments.of(new Utente(
                      "Alfonso",
                      "Cannavale",
                      null,
                      "alfonso.7@moneyart.it",
                      "alfonzo",
                      new Utente(),
                      md.digest("Alfonzino".getBytes()),
                      0d), "Alfonzino"));
    }
  }

  //Provider che restituisce gli utenti presenti nel db
  static class UtenteDBProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      Utente u1 = new Utente("Alfonso", "Cannavale", null, "alfonso.cannavale@gmail.com", "alfcan", new Utente(), md.digest("pippo123".getBytes()), 1000d);
      u1.setId(1);
      Utente u2 = new Utente("Daniele", "Galloppo", null, "daniele.galloppo@gmail.com", "DG266", new Utente(), md.digest("pippo123".getBytes()), 2500d);
      u2.setId(2);
      Utente u3 = new Utente("Michael", "De Santis", null, "michael.desantis@gmail.com", "shoyll", new Utente(), md.digest("pippo123".getBytes()), 2000d);
      u3.setId(3);

      Connection connection = dataSource.getConnection();
      Blob b = connection.createBlob();
      b.setBytes(1, "0x717171".getBytes());
      connection.close();

      Opera o = new Opera("The Shibosis", "Descrizione",Opera.Stato.IN_POSSESSO, b, u2, u1, "");
      o.setId(1);
      u1.setOpereCreate(Arrays.asList(o));
      u2.setOpereInPossesso(Arrays.asList(o));

      Asta a = new Asta();
      a.setId(1);

      Partecipazione p1 = new Partecipazione(a, u3, 1000);
      Partecipazione p2 = new Partecipazione(a, u2, 1500);
      p1.setId(1);
      p2.setId(2);
      u3.setPartecipazioni(Arrays.asList(p1));
      u2.setPartecipazioni(Arrays.asList(p2));

      Notifica n1 = new Notifica(u3, a, null, Notifica.Tipo.SUPERATO, "Contenuto della notifica.", false);
      Notifica n2 = new Notifica(u2, a, null, Notifica.Tipo.VITTORIA, "Contenuto della notifica.", false);
      n1.setId(1);
      n2.setId(2);
      u3.setNotifiche(Arrays.asList(n1));
      u2.setNotifiche(Arrays.asList(n2));

      return Stream.of(
              Arguments.of(u1),
              Arguments.of(u2),
              Arguments.of(u3)
              );
    }
  }

  static class ListUtenteDBProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      Utente u1 = new Utente("Alfonso", "Cannavale", null, "alfonso.cannavale@gmail.com", "alfcan", new Utente(), md.digest("passja@1234".getBytes()), 1000d);
      u1.setId(1);
      Utente u2 = new Utente("Daniele", "Galloppo", null, "daniele.galloppo@gmail.com", "DG266", new Utente(), md.digest("pippo123".getBytes()), 2500d);
      u2.setId(2);
      Utente u3 = new Utente("Michael", "De Santis", null, "michael.desantis@gmail.com", "shoyll", new Utente(), md.digest("passja@1234".getBytes()), 2000d);
      u3.setId(3);

      Connection connection = dataSource.getConnection();
      Blob b = connection.createBlob();
      b.setBytes(1, "0x717171".getBytes());
      connection.close();

      Opera o = new Opera("The Shibosis", "Descrizione",Opera.Stato.IN_POSSESSO, b, u2, u1, "");
      o.setId(1);
      u1.setOpereCreate(Arrays.asList(o));
      u2.setOpereInPossesso(Arrays.asList(o));

      Asta a = new Asta();
      a.setId(1);

      Partecipazione p1 = new Partecipazione(a, u3, 1000);
      Partecipazione p2 = new Partecipazione(a, u2, 1500);
      p1.setId(1);
      p2.setId(2);
      u3.setPartecipazioni(Arrays.asList(p1));
      u2.setPartecipazioni(Arrays.asList(p2));

      Notifica n1 = new Notifica(u3, a, null, Notifica.Tipo.SUPERATO, "Contenuto della notifica.", false);
      Notifica n2 = new Notifica(u2, a, null, Notifica.Tipo.VITTORIA, "Contenuto della notifica.", false);
      n1.setId(1);
      n2.setId(2);
      u3.setNotifiche(Arrays.asList(n1));
      u2.setNotifiche(Arrays.asList(n2));

      return Stream.of(Arguments.of(Arrays.asList(u1,u2,u3)));
    }
  }

  static class UtenteAmountProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      Utente u1 = new Utente("Alfonso", "Cannavale", null, "alfonso.cannavale@gmail.com", "alfcan", new Utente(), md.digest("pippo123".getBytes()), 1000d);
      u1.setId(1);
      Utente u2 = new Utente("Daniele", "Galloppo", null, "daniele.galloppo@gmail.com", "DG266", new Utente(), md.digest("pippo123".getBytes()), 2500d);
      u2.setId(2);
      Utente u3 = new Utente("Michael", "De Santis", null, "michael.desantis@gmail.com", "shoyll", new Utente(), md.digest("pippo123".getBytes()), 2000d);
      u3.setId(3);

      Connection connection = dataSource.getConnection();
      Blob b = connection.createBlob();
      b.setBytes(1, "0x717171".getBytes());
      connection.close();

      Opera o = new Opera("The Shibosis", "Descrizione",Opera.Stato.IN_POSSESSO, b, u2, u1, "");
      o.setId(1);
      u1.setOpereCreate(Arrays.asList(o));
      u2.setOpereInPossesso(Arrays.asList(o));

      Asta a = new Asta();
      a.setId(1);

      Partecipazione p1 = new Partecipazione(a, u3, 1000);
      Partecipazione p2 = new Partecipazione(a, u2, 1500);
      p1.setId(1);
      p2.setId(2);
      u3.setPartecipazioni(Arrays.asList(p1));
      u2.setPartecipazioni(Arrays.asList(p2));

      Notifica n1 = new Notifica(u3, a, null, Notifica.Tipo.SUPERATO, "Contenuto della notifica.", false);
      Notifica n2 = new Notifica(u2, a, null, Notifica.Tipo.VITTORIA, "Contenuto della notifica.", false);
      n1.setId(1);
      n2.setId(2);
      u3.setNotifiche(Arrays.asList(n1));
      u2.setNotifiche(Arrays.asList(n2));

      return Stream.of(
              Arguments.of(u1, 100d),
              Arguments.of(u2, 50d),
              Arguments.of(u3, 210d)
      );
    }
  }

  @Nested
  @DisplayName("Test Suite Check User")
  class TestCheckUser {

    @DisplayName("Check User with Email")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkUserEmail(Utente utente, String pwd) {
      utenteDao.doCreate(utente);

      Utente result = utenteService.checkUser(utente.getEmail(), pwd);
      result.setPassword(utente.getPassword());

      Assertions.assertEquals(utente.getId(), result.getId());
    }

    @DisplayName("Check User with Username")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkUserUsername(Utente utente, String pwd) {
      utenteDao.doCreate(utente);

      Utente result = utenteService.checkUser(utente.getUsername(), pwd);
      result.setPassword(utente.getPassword());

      Assertions.assertEquals(utente.getId(), result.getId());
    }

    @Test
    @DisplayName("Check User with username null")
    void checkUserUsernameNull() {
      Assertions.assertThrows(Exception.class, () -> utenteService.checkUser(null, "password"));
    }

    @DisplayName("Check User with pwd null")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkUserPwdNull(Utente utente) {
      Assertions.assertThrows(Exception.class, () -> utenteService.checkUser(utente.getEmail(), null));
    }

    @DisplayName("Check User return null")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkUserNull(Utente utente) {
      //questi utenti non esistono nel database
      Assertions.assertNull(utenteService.checkUser(utente.getUsername(), utente.getPassword().toString()));
    }

  }

  @Nested
  @DisplayName("Test Suite Get User Information")
  class TestGetUserInformation {

    @DisplayName("Get User Information Id")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void getUserInformation(Utente oracle) {

      Utente result = utenteService.getUserInformation(oracle.getId());

      assertEquals(oracle.getId(), result.getId());
      assertEquals(oracle.getUsername(), result.getUsername());
      assertEquals(oracle.getOpereInPossesso().size(), result.getOpereInPossesso().size());
      assertEquals(oracle.getNotifiche().size(), result.getNotifiche().size());
      assertEquals(oracle.getPartecipazioni().size(), result.getPartecipazioni().size());
    }

    @DisplayName("Get User Information Username")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void getUserInformationUsername(Utente oracle) {
      Utente result = utenteService.getUserInformation(oracle.getUsername());

      assertEquals(oracle.getId(), result.getId());
      assertEquals(oracle.getUsername(), result.getUsername());
      assertEquals(oracle.getOpereInPossesso().size(), result.getOpereInPossesso().size());
      assertEquals(oracle.getNotifiche().size(), result.getNotifiche().size());
      assertEquals(oracle.getPartecipazioni().size(), result.getPartecipazioni().size());
    }

    @DisplayName("Get User Information w username null")
    @Test
    void getUserInformationUsernameNull() {
      assertThrows(Exception.class, () -> utenteService.getUserInformation(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Sign Up User")
  class TestSignUp {

    @DisplayName("Sign Up User")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void signUpUser(Utente utente) {
      assertTrue(utenteService.signUpUser(utente));
    }

    @DisplayName("Sign Up User False")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void signUpUserFalse(Utente utente) {
      utenteDao.doCreate(utente);

      assertFalse(utenteService.signUpUser(utente));
    }

    @Test
    @DisplayName("Sign Up with utente null")
    void signUpUserNull() {
      assertThrows(Exception.class, () -> utenteService.signUpUser(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Update User")
  class TestUpdateUser {

    @DisplayName("Update User")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void updateUser(Utente utente) {
      utenteDao.doCreate(utente);
      utente.setNome("Nuovo Nome");

      utenteService.updateUser(utente);

      Utente result = utenteDao.doRetrieveById(utente.getId());
      result.setPassword(utente.getPassword());

      Assertions.assertEquals(utente.getId(), result.getId());
      Assertions.assertEquals(utente.getNome(), result.getNome());
    }

    @Test
    @DisplayName("Update User with paramater null")
    void updateUserNull() {
      Assertions.assertThrows(Exception.class, () -> utenteService.updateUser(null));
    }

  }

  @DisplayName("Get All Users")
  @ParameterizedTest
  @ArgumentsSource(ListUtenteDBProvider.class)
  void getAllUsers(List<Utente> oracle) {
    List<Utente> result = utenteService.getAllUsers();

    assertEquals(oracle.size(), result.size());

  }

  @Nested
  @DisplayName("Test Suite Get Users Sorted Followers")
  class TestGetUsersSortedByFollowers {

    @DisplayName("Get Users Sorted By Followers ASC")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void getUsersSortedByFollowers(List<Utente> utenti) {
      //aggiungo follower - array ordinato in senso crescente
      Utente u1 = utenti.get(0);
      Utente u2 = utenti.get(1);
      Utente u3 = utenti.get(2);
      u1.setSeguito(u2);
      u2.setSeguito(u1);
      u3.setSeguito(u1);
      //aggiorno su db
      for(Utente u : utenti)
        utenteDao.doUpdate(u);


      List<Utente> result = utenteService.getUsersSortedByFollowers("ASC");
      List<Utente> oracle = new ArrayList<>();
      oracle.add(0,u3);
      oracle.add(1,u2);
      oracle.add(2,u1);

      Assertions.assertEquals(oracle.size(), result.size());
      for(int i = 0; i < oracle.size(); i++) {
        assertEquals(oracle.get(i).getId(), result.get(i).getId());
      }

    }

    @DisplayName("Get Users Sorted By Followers DESC")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void getUsersSortedByFollowersDesc(List<Utente> utenti) {
      //aggiungo follower
      Utente u1 = utenti.get(0);
      Utente u2 = utenti.get(1);
      Utente u3 = utenti.get(2);
      u1.setSeguito(u2);
      u2.setSeguito(u1);
      u3.setSeguito(u1);
      //aggiorno su db
      for(Utente u : utenti)
        utenteDao.doUpdate(u);

      //ordino array
      List<Utente> oracle = new ArrayList<>();
      oracle.add(0, u1);
      oracle.add(1, u2);
      oracle.add(2, u3);

      List<Utente> result = utenteService.getUsersSortedByFollowers("DESC");

      Assertions.assertEquals(oracle.size(), result.size());
      for(int i = 0; i < utenti.size(); i++) {
        assertEquals(oracle.get(i).getId(), result.get(i).getId());
      }

    }

  }

  @Nested
  @DisplayName("Test Suite Search Users")
  class SearchUsers {

    @DisplayName("Search Users")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void searchUsers(Utente utente) {
      List<Utente> oracle = new ArrayList<>();
      oracle.add(utente);

      List<Utente> result = utenteService.searchUsers(utente.getUsername());

      Assertions.assertEquals(oracle.size(), result.size());
      for(int i = 0; i < result.size(); i++)
        Assertions.assertEquals(oracle.get(i).getId(), result.get(i).getId());
    }

    @DisplayName("Search Users null")
    @Test
    void searchUsersNull() {
      Assertions.assertThrows(Exception.class, () -> utenteService.searchUsers(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Check Username")
  class CheckUsername {

    @DisplayName("Check Username Existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void checkUsernameExsisting(Utente utente) {
      boolean result = utenteService.checkUsername(utente.getUsername());

      assertTrue(result);
    }

    @DisplayName("Check Username Not Existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkUsernameNotExsisting(Utente utente) {
      boolean result = utenteService.checkUsername(utente.getUsername());

      assertFalse(result);
    }

    @DisplayName("Check Username null")
    @Test
    void checkUsernameNull() {
      assertThrows(Exception.class, () -> utenteService.checkUsername(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Check Email")
  class CheckEmail {

    @DisplayName("Check Email Existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void checkEmailExisting(Utente utente) {
      System.out.println(utenteService.getUserInformation(utente.getUsername()));
      boolean result = utenteService.checkEmail(utente.getEmail());
      System.out.println(result);

      assertTrue(result);
    }

    @DisplayName("Check Email Not Existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void checkEmailNotExisting(Utente utente) {
      boolean result = utenteService.checkEmail(utente.getEmail());

      assertFalse(result);
    }

    @DisplayName("Check Email null")
    @Test
    void checkEmailNull() {
      assertThrows(Exception.class, () -> utenteService.checkEmail(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Follow")
  class Follow {

    @DisplayName("Follow")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void follow (List<Utente> utenti) {
      Utente follower = utenti.get(1);
      Utente followed = utenti.get(0);

      boolean result = utenteService.follow(follower, followed);

      Utente followerResult = utenteDao.doRetrieveByUsername(follower.getUsername());

      assertTrue(result);
      assertEquals(followed.getId(), followerResult.getSeguito().getId());
    }

    @DisplayName("Follow already present")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void followFalse (List<Utente> utenti) {
      Utente follower = utenti.get(1);
      Utente followed = utenti.get(0);

      follower.setSeguito(followed);
      utenteDao.doUpdate(follower);

      boolean result = utenteService.follow(follower, followed);

      assertFalse(result);
    }

    @DisplayName("Follow w follower null")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void followerNull(Utente utente) {
      Assertions.assertThrows(Exception.class, () -> utenteService.follow(null, utente));
    }

    @DisplayName("Follow w followed null")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void followedNull(Utente utente) {
      Assertions.assertThrows(Exception.class, () -> utenteService.follow(utente, null));
    }

    @DisplayName("Followed not existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void followedNotExisting(Utente follower) {
      Utente followed = new Utente("Franco","Battiato",null,
              "sonoadmin@moneyart.it","francoadmin",new Utente(),
              "admin".getBytes(),0d);

      Assertions.assertThrows(Exception.class, () -> utenteService.follow(follower, followed));
    }

  }

  @Nested
  @DisplayName("Test Suite Unfollow")
  class Unfollow {

    @DisplayName("Unfollow")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void unfollow (List<Utente> utenti) {
      Utente follower = utenti.get(1);
      Utente followed = utenti.get(0);

      follower.setSeguito(followed);
      utenteDao.doUpdate(follower);

      boolean result = utenteService.unfollow(follower);

      Utente followerResult = utenteDao.doRetrieveByUsername(follower.getUsername());

      assertTrue(result);
      assertNull(followerResult.getSeguito().getId());
    }

    @DisplayName("Unfollow w getSeguito null")
    @ParameterizedTest
    @ArgumentsSource(ListUtenteDBProvider.class)
    void unfollowFalse (List<Utente> utenti) {
      Utente follower = utenti.get(1);

      boolean result = utenteService.unfollow(follower);

      assertFalse(result);
    }


    @DisplayName("Unollow w follower null")
    @ParameterizedTest
    @ArgumentsSource(UtenteDBProvider.class)
    void unfollowerNull(Utente utente) {
      Assertions.assertThrows(Exception.class, () -> utenteService.unfollow(null));
    }

  }

  @Nested
  @DisplayName("Test Suite Deposit")
  class TestDeposit {

    @DisplayName("Deposit")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void deposit(Utente utente, double amount) {
      boolean result = utenteService.deposit(utente, amount);

      Utente resultUtente = utenteDao.doRetrieveById(utente.getId());

      assertTrue(result);
      assertEquals(utente.getSaldo()+amount, resultUtente.getSaldo());
    }

    @DisplayName("Deposit Utente Null")
    @Test
    void depositUtenteNull() {
      assertThrows(Exception.class, () -> utenteService.deposit(null, 100));
    }

    @DisplayName("Deposit Zero")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void depositZero(Utente utente) {
      assertThrows(Exception.class, () -> utenteService.deposit(utente, 0));
    }

    @DisplayName("Deposit Negative Amount")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void depositNegative(Utente utente, double amount) {
      assertThrows(Exception.class, () -> utenteService.deposit(utente, -amount));
    }

    @DisplayName("Deposit Utente Not Existing")
    @ParameterizedTest
    @ArgumentsSource(UtenteProvider.class)
    void depositFalse(Utente utente) {
      assertFalse(utenteService.withdraw(utente, 100));
    }

  }

  @Nested
  @DisplayName("Test Suite Withdraw")
  class TestWithdraw {
    
    @DisplayName("Withdraw")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void withdraw(Utente utente, double amount) {
      boolean result = utenteService.withdraw(utente, amount);

      Utente resultUtente = utenteDao.doRetrieveById(utente.getId());

      assertTrue(result);
      assertEquals(utente.getSaldo()-amount, resultUtente.getSaldo());
    }

    @DisplayName("Withdraw Utente Null")
    @Test
    void withdrawUtenteNull() {
      assertThrows(Exception.class, () -> utenteService.withdraw(null, 100));
    }

    @DisplayName("Withdraw amount negative")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void withdrawAmountNegative(Utente utente, double amount) {
      assertThrows(Exception.class, () -> utenteService.withdraw(utente, -amount));
    }

    @DisplayName("Withdraw amount > saldo")
    @ParameterizedTest
    @ArgumentsSource(UtenteAmountProvider.class)
    void withdrawAmountMagSaldo(Utente utente, double amount) {
      assertFalse(utenteService.withdraw(utente, utente.getSaldo()+amount));
    }
    
  }

  @DisplayName("Get Balance")
  @ParameterizedTest
  @ArgumentsSource(UtenteDBProvider.class)
  void getBalance(Utente utente) {
    assertEquals(utente.getSaldo(), utenteService.getBalance(utente));
  }

  @DisplayName("Encrypt Password")
  @ParameterizedTest
  @ArgumentsSource(UtenteProvider.class)
  void encryptPassword(Utente u, String pwd) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    assertArrayEquals(md.digest(pwd.getBytes()), utenteService.encryptPassword(pwd));
  }

  @DisplayName("Encrypt Password pwd null")
  @Test
  void encryptPasswordNull() {
    assertThrows(Exception.class, () -> utenteService.encryptPassword(null));
  }

}