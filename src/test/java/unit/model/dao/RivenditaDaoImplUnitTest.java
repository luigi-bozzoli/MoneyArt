package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RivenditaDaoImplUnitTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;

  private RivenditaDao rivenditaDao;

  //Costruttore vuoto
  public RivenditaDaoImplUnitTest () { }

  @BeforeEach
  public void setUpClass() throws SQLException {

    rivenditaDao = new RivenditaDaoImpl(dataSource);

    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(preparedStatement).setObject(anyInt(), any(), anyInt());
    doNothing().when(connection).commit();
  }

  @AfterEach
  public void tearDown() {

  }

  @ParameterizedTest
  @ValueSource(ints = {1})
  public void testDoCreate(int oracle) throws SQLException {

    Rivendita result = new Rivendita(new Opera(), Rivendita.Stato.IN_CORSO, 0d);

    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(resultSet.getInt(1)).thenReturn(oracle);

    rivenditaDao.doCreate(result);

    assertEquals(oracle, result.getId());
  }

  @Test
  void doRetrieveById() throws SQLException {

    Opera opera = new Opera();
    opera.setId(1);

    Rivendita resell = new Rivendita(
      opera,
      Rivendita.Stato.IN_CORSO,
      0d
    );

    resell.setId(1);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.getObject("id", Integer.class)).thenReturn(resell.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(resell.getOpera().getId());
    when(resultSet.getObject("stato", String.class)).thenReturn(resell.getStato().toString());
    when(resultSet.getObject("prezzo", Double.class)).thenReturn(resell.getPrezzo());

    Rivendita result = rivenditaDao.doRetrieveById(resell.getId());
    assertEquals(resell, result);
  }

  @Test
  void doRetrieveByIdCatch() throws SQLException {

    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    Rivendita rivendita = rivenditaDao.doRetrieveById(1);

    Assertions.assertNull(rivendita);

  }

  @Test
  void doRetrieveAll() throws SQLException {

    Opera opera1 = new Opera();
    opera1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);

    Rivendita resell1 = new Rivendita(
      opera1,
      Rivendita.Stato.IN_CORSO,
      0d
    );
    resell1.setId(1);

    Rivendita resell2 = new Rivendita(
      opera2,
      Rivendita.Stato.IN_CORSO,
      0d
    );
    resell2.setId(2);

    List<Rivendita> rivenditeOracolo = Arrays.asList(resell1, resell2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.getObject("id", Integer.class)).thenReturn(resell1.getId(), resell2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(resell1.getOpera().getId(),
      resell2.getOpera().getId());
    when(resultSet.getObject("stato", String.class)).thenReturn(resell1.getStato().toString(),
      resell2.getStato().toString());
    when(resultSet.getObject("prezzo", Double.class)).thenReturn(resell1.getPrezzo(),
      resell2.getPrezzo());

    List<Rivendita> rivenditeRetrieve = rivenditaDao.doRetrieveAll("id");
    assertArrayEquals(rivenditeOracolo.toArray(), rivenditeRetrieve.toArray());
  }

  @Test
  void doRetrieveAllCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    List<Rivendita> rivendite = rivenditaDao.doRetrieveAll(null);
    Assertions.assertNull(rivendite);
  }

  @Test
  void doRetrieveByStato() throws SQLException {

    Opera opera1 = new Opera();
    opera1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);

    Rivendita resell1 = new Rivendita(
      opera1,
      Rivendita.Stato.IN_CORSO,
      0d
    );
    resell1.setId(1);

    Rivendita resell2 = new Rivendita(
      opera2,
      Rivendita.Stato.TERMINATA,
      0d
    );
    resell2.setId(2);

    List<Rivendita> rivenditeOracolo = Arrays.asList(resell2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.getObject("id", Integer.class)).thenReturn(resell2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn((resell2.getOpera().getId()));
    when(resultSet.getObject("stato", String.class)).thenReturn(resell2.getStato().toString());
    when(resultSet.getObject("prezzo", Double.class)).thenReturn(resell2.getPrezzo());

    List<Rivendita> rivenditeRetrieve = rivenditaDao.doRetrieveByStato(Rivendita.Stato.TERMINATA);
    assertArrayEquals(rivenditeOracolo.toArray(), rivenditeRetrieve.toArray());
  }

  @Test
  void doRetrieveByStatoCatch() throws SQLException{

    Opera opera1 = new Opera();
    opera1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);

    Rivendita resell1 = new Rivendita(
      opera1,
      Rivendita.Stato.IN_CORSO,
      0d
    );
    resell1.setId(1);

    Rivendita resell2 = new Rivendita(
      opera2,
      Rivendita.Stato.TERMINATA,
      0d
    );
    resell2.setId(2);

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.getObject("id", Integer.class)).thenReturn(resell2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn((resell2.getOpera().getId()));
    when(resultSet.getObject("stato", String.class)).thenReturn(resell2.getStato().toString());
    when(resultSet.getObject("prezzo", Double.class)).thenReturn(resell2.getPrezzo());

    List<Rivendita> rivenditeRetrieve = rivenditaDao.doRetrieveByStato(Rivendita.Stato.TERMINATA);
    assertNull(rivenditeRetrieve);
  }

  @Test
  void doUpdate() throws SQLException {

    Opera opera = new Opera();
    opera.setId(1);

    Rivendita resell = new Rivendita(
      opera,
      Rivendita.Stato.IN_CORSO,
      0d
    );

    resell.setId(1);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    rivenditaDao.doUpdate(resell);
    verify(preparedStatement, times(4)).setObject(anyInt(), any(), anyInt());
    verify(preparedStatement, times(1)).executeUpdate();
  }

  @Test
  void doUpdateCatch() throws SQLException {

    Opera opera = new Opera();
    opera.setId(1);

    Rivendita resell = new Rivendita(
      opera,
      Rivendita.Stato.IN_CORSO,
      0d
    );

    resell.setId(1);

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
    rivenditaDao.doUpdate(resell);
    verify(preparedStatement, times(0)).setObject(anyInt(), any(), anyInt());
    verify(preparedStatement, times(0)).executeUpdate();
  }

  @Test
  void doDelete() throws SQLException {

    Opera opera = new Opera();
    opera.setId(1);

    Rivendita resell = new Rivendita(
      opera,
      Rivendita.Stato.IN_CORSO,
      0d
    );

    resell.setId(1);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    rivenditaDao.doDelete(resell);
    verify(preparedStatement, times(1)).setObject(anyInt(), any(), anyInt());
    verify(preparedStatement, times(1)).executeUpdate();
  }

  @Test
  void doDeleteCatch() throws SQLException {

    Opera opera = new Opera();
    opera.setId(1);

    Rivendita resell = new Rivendita(
      opera,
      Rivendita.Stato.IN_CORSO,
      0d
    );

    resell.setId(1);

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
    rivenditaDao.doDelete(resell);
    verify(preparedStatement, times(0)).setObject(anyInt(), any(), anyInt());
    verify(preparedStatement, times(0)).executeUpdate();
  }
}
