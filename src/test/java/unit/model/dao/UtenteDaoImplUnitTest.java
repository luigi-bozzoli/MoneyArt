package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UtenteDaoImplUnitTest {

  @Mock
  private static DataSource dataSource;
  @Mock
  private static Connection connection;
  @Mock
  private static PreparedStatement preparedStatement;
  @Mock
  private static ResultSet resultSet;


  private static Utente user;
  private static Utente userFollowed;


  //costruttore vuoto
  public UtenteDaoImplUnitTest () { }

  @BeforeAll
  public static void setUpClass() {
    userFollowed = new Utente("MarioVip", "RossiVip", null, "mariorossivip@unisa.it",
            "m_red", null, new byte[10], 2000000.2);
    userFollowed.setId(99);

    user = new Utente("Mario", "Rossi", null, "mariorossi@unisa.it",
            "m_red", userFollowed, new byte[10], 2.2);
    user.setId(100);
  }

  @AfterAll
  public static void tearDownClass() {
  }

  @BeforeEach
  public void setUp() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
  }

  @AfterEach
  public void tearDown() {
  }

  @Test
  @DisplayName("doCreate")
  public void doCreate() throws SQLException {
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    when(resultSet.getInt(anyInt())).thenReturn(user.getId());

    UtenteDaoImpl userDao = new UtenteDaoImpl(dataSource);
    boolean result = userDao.doCreate(user);

    assertTrue(result);
  }

  @Test
  @DisplayName("doCreateCatch")
  public void doCreateCatch() throws SQLException {
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

    assertTrue(!(new UtenteDaoImpl(dataSource).doCreate(user)));

  }


  @Test
  @DisplayName("doRetrieveById")
  void doRetrieveById() throws SQLException {

    /*Istruisco i mock di connessione per questo metodo +
    Istruisco il finto comportamento di prelevazione dell'opera dal db
    */
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(user.getId());
    when(resultSet.getObject("id_seguito", Integer.class)).thenReturn(user.getSeguito().getId());
    when(resultSet.getObject("email", String.class)).thenReturn(user.getEmail());
    when(resultSet.getObject("pwd", String.class)).thenReturn(user.getPassword().toString());
    when(resultSet.getObject("username", String.class)).thenReturn(user.getUsername());
    when(resultSet.getObject("nome", String.class)).thenReturn(user.getNome());
    when(resultSet.getObject("cognome", String.class)).thenReturn(user.getCognome());
    when(resultSet.getObject("foto", Blob.class)).thenReturn(user.getFotoProfilo());
    when(resultSet.getObject("saldo", Double.class)).thenReturn(user.getSaldo());


    Utente result = new UtenteDaoImpl(dataSource).doRetrieveById(user.getId());

    assertTrue(user.getId() == result.getId());
  }

  @Test
  @DisplayName("doRetrieveByIdCatch")
  void doRetrieveByIdCatch() throws SQLException {

    when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

    Utente userResult = new UtenteDaoImpl(dataSource).doRetrieveById(user.getId());
    assertNull(userResult);
  }

  @Test
  void doRetrieveAll() throws SQLException {

    //costruisco la lista oracolo del nostro test unit
    Utente us1 = user;
    Utente us2 = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
            "s_ano", user, new byte[10], 0.002);
    user.setId(user.getId()+50);

    List<Utente> utentiOracolo = Arrays.asList(us1, us2);

    /*Istruisco i mock di connessione per questo metodo +
    Istruisco il finto comportamento di prelevazione dell'opera dal db
    */

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(preparedStatement.executeQuery(anyString())).thenReturn(resultSet);

    when(resultSet.getObject("id", Integer.class)).thenReturn(us1.getId(), us2.getId());
    when(resultSet.getObject("id_seguito", Integer.class)).thenReturn(us1.getSeguito().getId(), us2.getSeguito().getId());
    when(resultSet.getObject("email", String.class)).thenReturn(us1.getEmail(), us2.getEmail());
    when(resultSet.getObject("pwd", String.class)).thenReturn(us1.getPassword().toString(), us2.getPassword().toString());
    when(resultSet.getObject("username", String.class)).thenReturn(us1.getUsername(), us2.getUsername());
    when(resultSet.getObject("nome", String.class)).thenReturn(us1.getNome(), us2.getNome());
    when(resultSet.getObject("cognome", String.class)).thenReturn(us1.getCognome(), us2.getCognome());
    when(resultSet.getObject("foto", Blob.class)).thenReturn(us1.getFotoProfilo(), us2.getFotoProfilo());
    when(resultSet.getObject("saldo", Double.class)).thenReturn(us1.getSaldo(), us2.getSaldo());


    List<Utente> utentiRetrieve = new UtenteDaoImpl(dataSource).doRetrieveAll("id");

    System.out.println(utentiOracolo);
    System.out.println(utentiRetrieve);

    assertTrue(utentiOracolo.get(0).getId() == utentiRetrieve.get(0).getId() && utentiOracolo.get(1).getId() == utentiRetrieve.get(1).getId());

  }


  @Test
  @DisplayName("doRetrieveAllCatch")
  void doRetrieveAllCatch() throws SQLException {

    when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

    UtenteDaoImpl userDao = new UtenteDaoImpl(dataSource);
    List<Utente> users = userDao.doRetrieveAll("id");
    assertNull(users);
  }

/*
  @Test
  void doRetrieveByUsername() {
  }

  @Test
  void doRetrieveFollowersByUserId() {
  }

  @Test
  void researchUser() {
  }

  @Test
  void doRetrieveByEmail() {
  }

 */

}