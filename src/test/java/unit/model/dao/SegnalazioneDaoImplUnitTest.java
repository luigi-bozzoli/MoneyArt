package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
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
  void doRetrieveByAuctionId() {
  }
}