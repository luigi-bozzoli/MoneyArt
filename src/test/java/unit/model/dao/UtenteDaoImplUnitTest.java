package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.*;

@RunWith(MockitoJUnitRunner.class)
public class UtenteDaoImplUnitTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;

  private static Utente user;
  private static Utente userFollowed;


  //costruttore vuoto
  public UtenteDaoImplUnitTest () { }

  @BeforeClass
  public static void setUpClass() {
    userFollowed = new Utente("MarioVip", "RossiVip", null, "mariorossivip@unisa.it",
            "m_red", null, new byte[10], 2000000.2);
    userFollowed.setId(99);

    user = new Utente("Mario", "Rossi", null, "mariorossi@unisa.it",
            "m_red", userFollowed, new byte[10], 2.2);
    user.setId(100);
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws SQLException { //istruisco gli oggetti mock
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    when(resultSet.getInt(anyInt())).thenReturn(user.getId());
  }

  @After
  public void tearDown() {
  }


  @Test
  public void testDoCreate() throws SQLException {
   UtenteDaoImpl userDao = new UtenteDaoImpl(dataSource);

   boolean result = userDao.doCreate(user);

    assertTrue(result);
  }

/*
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