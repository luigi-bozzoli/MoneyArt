package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.utils.production.Retriever;

import org.eclipse.jetty.util.Fields;  /*_____?_____*/
import static org.mockito.Mockito.verify;
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
  private Retriever retriever;
  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;


  private static Utente user; //non lo mocco, tanto serve solo il suo costruttore
  private static Utente userFollowed;

  //Potrebbe essere moccato anche l'utente, ma non ha senso perché andiamo solo ad istanziarlo, e quindi ad usare il costruttore di Utente


  //costruttore vuoto
  public UtenteDaoImplUnitTest () { }

  @BeforeClass
  public static void setUpClass() throws Exception {
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
    //when(retriever.getIstance(any())).thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1); /*modificata*/
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    System.out.println("\naoooooo"+user.getId()); /*debug*/
    when(resultSet.getInt(anyInt())).thenReturn(user.getId()); /*modificata*/
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






/*  metodo senza mod bool

  @Test  /*signature da testare ==> public void doCreate(Utente item)
  public void testDoCreate() throws SQLException {
    UtenteDaoImpl userDao = new UtenteDaoImpl(mockDataSource);

   userDao.doCreate(new Utente("Mario","Rossi", null, "mariorossi@unisa.it",
                   "m_red", null, new byte[10], 2.2));

    //verify and assert
    verify(mockConn, times(1)).prepareStatement(anyString(), anyInt());
    verify(mockPreparedStmnt, times(9)).setObject(anyInt(), anyString(), Mockito.anyObject());
    verify(mockPreparedStmnt, times(1)).executeUpdate();
    verify(mockConn, times(1)).commit();
    verify(mockResultSet, times(2)).next();
    //verify(mockResultSet, times(1));

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