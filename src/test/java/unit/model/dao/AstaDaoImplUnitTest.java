package unit.model.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AstaDaoImplUnitTest {

  @Mock
  private DataSource dataSource;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  @Mock
  private Connection connection;

  @Mock
  private Statement statement;

  private AstaDao astaDao;


  @BeforeEach
  void setUp() throws SQLException {
    astaDao = new AstaDaoImpl(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    doNothing().when(preparedStatement).setObject(anyInt(), any(), anyInt());
  }

  @AfterEach
  void tearDown() {
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  void doCreate(int oracolo) throws SQLException {
    Asta asta = new Asta(new Opera(), new Date(), new Date(), Asta.Stato.ELIMINATA);
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(resultSet.getInt(1)).thenReturn(oracolo);

    astaDao.doCreate(asta);

    Assertions.assertEquals(asta.getId(), oracolo);
  }

  @Test
  void doCreateCatch() throws SQLException {
    Asta asta = new Asta(new Opera(), new Date(), new Date(), Asta.Stato.ELIMINATA);
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(ex);

    astaDao.doCreate(asta);

    Assertions.assertNull(asta.getId());
  }


  @Test
  void doRetrieveById() throws SQLException {
    Opera opera = new Opera();
    opera.setId(1);
    Asta asta =
        new Asta(opera, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta.setId(1);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(asta.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(asta.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(asta.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(asta.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(asta.getStato().toString());

    Asta astaRetrieve = astaDao.doRetrieveById(asta.getId());

    Assertions.assertEquals(asta, astaRetrieve);
  }

  @Test
  void doRetrieveByIdCatch() throws SQLException {

    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    Asta asta = astaDao.doRetrieveById(1);

    Assertions.assertNull(asta);

  }

  @Test
  void doRetrieveAll() throws SQLException {
    Opera opera1 = new Opera();
    opera1.setId(1);
    Asta asta1 =
        new Asta(opera1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);
    Asta asta2 =
        new Asta(opera2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.TERMINATA);
    asta2.setId(2);

    List<Asta> asteOracolo = Arrays.asList(asta1, asta2);

    when(connection.createStatement()).thenReturn(statement);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(statement.executeQuery(anyString())).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(asta1.getId(), asta2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(asta1.getOpera().getId(),
        asta2.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(asta1.getDataInizio(),
        asta2.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(asta1.getDataFine(),
        asta2.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(asta1.getStato().toString(),
        asta2.getStato().toString());

    List<Asta> asteRetrieve = astaDao.doRetrieveAll("id");

    Assertions.assertArrayEquals(asteRetrieve.toArray(), asteOracolo.toArray());
  }

  @Test
  void doRetrieveAllCatch() throws SQLException {
    when(connection.createStatement()).thenThrow(new SQLException());


    List<Asta> aste = astaDao.doRetrieveAll("id");
    Assertions.assertEquals(0, aste.size());
  }


  @Test
  void doRetrieveByStato() throws SQLException {
    Opera opera1 = new Opera();
    opera1.setId(1);
    Asta asta1 =
        new Asta(opera1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);
    Asta asta2 =
        new Asta(opera2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.TERMINATA);
    asta2.setId(2);

    List<Asta> asteOracolo = Arrays.asList(asta2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn( asta2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(
        asta2.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(
        asta2.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(
        asta2.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(
        asta2.getStato().toString());

    List<Asta> asteRetrieve = astaDao.doRetrieveByStato(Asta.Stato.TERMINATA);

    Assertions.assertArrayEquals(asteRetrieve.toArray(), asteOracolo.toArray());

  }

  @Test
  void doRetrieveByStatoCatch() throws SQLException{


    Opera opera1 = new Opera();
    opera1.setId(1);
    Asta asta1 =
        new Asta(opera1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);
    Asta asta2 =
        new Asta(opera2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.TERMINATA);
    asta2.setId(2);

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn( asta2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(
        asta2.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(
        asta2.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(
        asta2.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(
        asta2.getStato().toString());

    List<Asta> aste = astaDao.doRetrieveByStato(Asta.Stato.TERMINATA);
    Assertions.assertEquals(0, aste.size());
  }

  @Test
  void doRetrieveByOperaId() throws SQLException {
    Opera opera1 = new Opera();
    opera1.setId(1);
    Asta asta1 =
        new Asta(opera1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);
    Asta asta2 =
        new Asta(opera2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.TERMINATA);
    asta2.setId(2);

    List<Asta> asteOracolo = Arrays.asList(asta2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn( asta2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(
        asta2.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(
        asta2.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(
        asta2.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(
        asta2.getStato().toString());

    List<Asta> asteRetrieve = astaDao.doRetrieveByOperaId(2);

    Assertions.assertArrayEquals(asteRetrieve.toArray(), asteOracolo.toArray());
  }

  @Test
  void doRetrieveByOperaIdCatch() throws SQLException{
    Opera opera1 = new Opera();
    opera1.setId(1);
    Asta asta1 =
        new Asta(opera1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.IN_CORSO);
    asta1.setId(1);

    Opera opera2 = new Opera();
    opera2.setId(2);
    Asta asta2 =
        new Asta(opera2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
            Asta.Stato.TERMINATA);
    asta2.setId(2);

    List<Asta> asteOracolo = Arrays.asList(asta2);

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn( asta2.getId());
    when(resultSet.getObject("id_opera", Integer.class)).thenReturn(
        asta2.getOpera().getId());
    when(resultSet.getObject("data_inizio", Date.class)).thenReturn(
        asta2.getDataInizio());
    when(resultSet.getObject("data_fine", Date.class)).thenReturn(
        asta2.getDataFine());
    when(resultSet.getObject("stato", String.class)).thenReturn(
        asta2.getStato().toString());

    List<Asta> asteRetrieve = astaDao.doRetrieveByOperaId(2);

    Assertions.assertEquals(0, asteRetrieve.size());
  }
}