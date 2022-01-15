package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import org.apache.ibatis.jdbc.SQL;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SegnalazioneDaoImplUnitTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;
  @Mock
  private Connection connection;

  private SegnalazioneDao segnalazioneDao;

  @BeforeEach
  void setUp() throws SQLException {
    segnalazioneDao = new SegnalazioneDaoImpl(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    doNothing().when(preparedStatement).setObject(anyInt(), any(), anyInt());
  }

  @AfterEach
  void tearDown() {
  }

  @ParameterizedTest
  @ValueSource(ints = {10,11,12,13})
  void doCreate(int oracle) throws SQLException {
    when(connection.prepareStatement(anyString(),anyInt())).thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(resultSet.getInt(anyInt())).thenReturn(oracle);

    Asta asta = new Asta();
    asta.setId(oracle);
    Segnalazione segnalazione = new Segnalazione(asta, "prova", false);

    boolean value = segnalazioneDao.doCreate(segnalazione);

    Assertions.assertEquals(true, value);
    Assertions.assertEquals(oracle, segnalazione.getId());
  }

  @Test
  void doCreateCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(ex);

    Asta asta = new Asta();
    asta.setId(1);
    Segnalazione segnalazione = new Segnalazione(asta, "prova", false);

    segnalazioneDao.doCreate(segnalazione);

    Assertions.assertNull(segnalazione.getId());
  }

  @Test
  void doRetrieveById() throws SQLException {
    Asta asta = new Asta();

    asta.setId(1);

    Segnalazione segnalazione = new Segnalazione(asta, "prova", false);
    segnalazione.setId(1);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(segnalazione.getId());
    when(resultSet.getObject("id_asta", Integer.class)).thenReturn(segnalazione.getAsta().getId());
    when(resultSet.getObject("commento", String.class)).thenReturn(segnalazione.getCommento());
    when(resultSet.getObject("letta", Boolean.class)).thenReturn(segnalazione.isLetta());

    Segnalazione segnalazioneRetrieve = segnalazioneDao.doRetrieveById(1);

    Assertions.assertEquals(segnalazione, segnalazioneRetrieve);
  }

  @Test
  void doRetrieveByIdCatch() throws SQLException {

    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    Segnalazione segnalazione = segnalazioneDao.doRetrieveById(1);

    Assertions.assertNull(segnalazione);

  }

  @Test
  void doRetrieveAll() throws SQLException {
    Asta asta = new Asta();

    asta.setId(1);

    Segnalazione segnalazione = new Segnalazione(asta, "prova", false);
    segnalazione.setId(1);

    Asta asta2 = new Asta();

    asta2.setId(2);

    Segnalazione segnalazione2 = new Segnalazione(asta2, "prova", false);
    segnalazione.setId(2);

    List<Segnalazione> segnalazioneOracolo = Arrays.asList(segnalazione, segnalazione2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(segnalazione.getId(), segnalazione2.getId());
    when(resultSet.getObject("id_asta", Integer.class)).thenReturn(segnalazione.getAsta().getId(), segnalazione2.getAsta().getId());
    when(resultSet.getObject("commento", String.class)).thenReturn(segnalazione.getCommento(), segnalazione2.getCommento());
    when(resultSet.getObject("letta", Boolean.class)).thenReturn(segnalazione.isLetta(), segnalazione2.isLetta());

    List<Segnalazione> segnalazioneRetrieve = segnalazioneDao.doRetrieveAll("id");

    Assertions.assertArrayEquals(segnalazioneRetrieve.toArray(), segnalazioneOracolo.toArray());

  }

  @Test
  void doRetrieveAllCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    List<Segnalazione> segnalazioni = segnalazioneDao.doRetrieveAll(null);

    Assertions.assertNull(segnalazioni);
  }

  @Test
  void doRetrieveByAuctionId() throws SQLException {
    Asta asta = new Asta();

    asta.setId(1);

    Segnalazione segnalazione = new Segnalazione(asta, "prova", false);
    segnalazione.setId(1);

    Asta asta2 = new Asta();

    asta2.setId(1);

    Segnalazione segnalazione2 = new Segnalazione(asta2, "prova", false);
    segnalazione.setId(2);

    List<Segnalazione> segnalazioneOracolo = Arrays.asList(segnalazione, segnalazione2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(segnalazione.getId(), segnalazione2.getId());
    when(resultSet.getObject("id_asta", Integer.class)).thenReturn(segnalazione.getAsta().getId(), segnalazione2.getAsta().getId());
    when(resultSet.getObject("commento", String.class)).thenReturn(segnalazione.getCommento(), segnalazione2.getCommento());
    when(resultSet.getObject("letta", Boolean.class)).thenReturn(segnalazione.isLetta(), segnalazione2.isLetta());

    List<Segnalazione> segnalazioneRetrieve = segnalazioneDao.doRetrieveByAuctionId(1);

    Assertions.assertArrayEquals(segnalazioneRetrieve.toArray(), segnalazioneOracolo.toArray());
  }

  @Test
  void doRetrieveByAuctionIdCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    List<Segnalazione> segnalazioni = segnalazioneDao.doRetrieveByAuctionId(1);

    Assertions.assertNull(segnalazioni);
  }

  /*
  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }
  */
}