package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.sql.*;

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


/*
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