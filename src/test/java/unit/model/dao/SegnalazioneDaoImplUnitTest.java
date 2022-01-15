package unit.model.dao;

import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

  @Test
  void doCreate() {
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