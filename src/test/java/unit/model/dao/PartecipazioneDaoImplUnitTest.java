package unit.model.dao;

import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
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
  void doRetrieveAllByAuctionId() {
  }

  @Test
  void doRetrieveAllByUserId() {
  }

  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }
}