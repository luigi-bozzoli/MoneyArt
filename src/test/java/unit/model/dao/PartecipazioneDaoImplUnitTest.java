package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import org.apache.ibatis.jdbc.SQL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.google.inject.internal.util.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartecipazioneDaoImplUnitTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;
  @Mock
  private Connection connection;

  private PartecipazioneDao partecipazioneDao;

  @BeforeEach
  void setUp() throws SQLException {
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
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
    Utente utente = new Utente();
    utente.setId(oracle);
    Partecipazione partecipazione = new Partecipazione(asta, utente, 999d);

    boolean value = partecipazioneDao.doCreate(partecipazione);

    Assertions.assertEquals(true, value);
    Assertions.assertEquals(oracle, partecipazione.getId());
  }

  @Test
  void doCreateCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(ex);

    Asta asta = new Asta();
    asta.setId(1);
    Utente utente = new Utente();
    utente.setId(1);
    Partecipazione partecipazione = new Partecipazione(asta, utente, 999d);

    partecipazioneDao.doCreate(partecipazione);

    Assertions.assertNull(partecipazione.getId());
  }

  @Test
  void doRetrieveById() {
  }

  @Test
  void doRetrieveByIdCatch() throws SQLException {

    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    Partecipazione partecipazione = partecipazioneDao.doRetrieveById(1);

    Assertions.assertNull(partecipazione);

  }

  @Test
  void doRetrieveAll() {
  }

  @Test
  void doRetrieveAllCatch() throws SQLException {
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    List<Partecipazione> partecipazioni = partecipazioneDao.doRetrieveAll("id");
    Assertions.assertNull(partecipazioni);
  }

  @ParameterizedTest
  @ValueSource(ints = {1})
  void doRetrieveAllByAuctionId(int idAuction) throws SQLException {
    Utente utente = new Utente();
    Asta asta = new Asta();

    utente.setId(1);
    asta.setId(idAuction);

    Partecipazione partecipazione = new Partecipazione(asta, utente, 999d);
    partecipazione.setId(1);

    Utente utente2 = new Utente();
    Asta asta2 = new Asta();

    utente2.setId(2);
    asta2.setId(idAuction);

    Partecipazione partecipazione2 = new Partecipazione(asta2, utente2, 999d);
    partecipazione2.setId(2);

    List<Partecipazione> partecipazioneOracolo = Arrays.asList(partecipazione, partecipazione2);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(resultSet.getObject("id", Integer.class)).thenReturn(partecipazione.getId(),
            partecipazione2.getId());
    when(resultSet.getObject("id_utente", Integer.class)).thenReturn(partecipazione.getUtente().getId(),
            partecipazione2.getUtente().getId());
    when(resultSet.getObject("id_asta", Integer.class)).thenReturn(partecipazione.getAsta().getId(),
            partecipazione2.getAsta().getId());
    when(resultSet.getObject("offerta", Double.class)).thenReturn(partecipazione.getOfferta(),
            partecipazione2.getOfferta());

    List<Partecipazione> partecipazioneRetrieve = partecipazioneDao.doRetrieveAllByAuctionId(idAuction);

    Assertions.assertArrayEquals(partecipazioneRetrieve.toArray(), partecipazioneOracolo.toArray());
  }

  @Test
  void doRetrieveAllByAuctionIdCatch() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

    List<Partecipazione> partecipazioneRetrieve = partecipazioneDao.doRetrieveAllByAuctionId(1);

    Assertions.assertNull(partecipazioneRetrieve);
  }

  @Test
  void doRetrieveAllByUserId() {
  }

  @Test
  void doRetrieveAllByUserIdCatch() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

    List<Partecipazione> partecipazioneRetrieve = partecipazioneDao.doRetrieveAllByUserId(1);

    Assertions.assertNull(partecipazioneRetrieve);
  }

  /*
  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }
}