package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.utils.production.Retriever;

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
class UtenteDaoImplUnitTest {

  @Mock
  private Retriever mockRetriever;
  @Mock
  private DataSource mockDataSource;
  @Mock
  private Connection mockConn;
  @Mock
  private PreparedStatement mockPreparedStmnt;
  @Mock
  private ResultSet mockResultSet;


  //costruttore vuoto
  public UtenteDaoImplUnitTest () { }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws SQLException {
    when(mockRetriever.getIstance(any())).thenReturn(mockDataSource);
    when(mockDataSource.getConnection()).thenReturn(mockConn);
    when(mockDataSource.getConnection(anyString(), anyString())).thenReturn(mockConn);
    doNothing().when(mockConn).commit();
    when(mockConn.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStmnt);
    doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
    when(mockPreparedStmnt.execute()).thenReturn(Boolean.TRUE);
    when(mockPreparedStmnt.getGeneratedKeys()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    // when(mockResultSet.getInt(Fields.GENERATED_KEYS)).thenReturn(userId);
  }

  @After
  public void tearDown() {
  }



  @Test  /*signature da testare ==> public void doCreate(Utente item) */
  void testDoCreate() throws SQLException {
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
}