package unit.utils.timers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDaoImpl;
import java.io.Serializable;
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
class TimedObjectDaoImplUnitTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;

  @Mock
  private Statement statement;

  private TimedObjectDao timedObjectDao;

  @BeforeEach
  void setUp() throws SQLException {
    timedObjectDao = new TimedObjectDaoImpl(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    doNothing().when(preparedStatement).setObject(anyInt(), any(), anyInt());
  }

  @AfterEach
  void tearDown() {
  }


  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  void doCreate(int oracolo) throws SQLException {
    TimedObject timedObject = new TimedObject("prova","task",new Date());
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    when(resultSet.getInt(1)).thenReturn(oracolo);

    timedObjectDao.doCreate(timedObject);

    Assertions.assertEquals(timedObject.getId(), oracolo);
  }

  @Test
  void doCreateCatch() throws SQLException {
    TimedObject timedObject = new TimedObject("prova","task",new Date());
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(ex);

    timedObjectDao.doCreate(timedObject);

    Assertions.assertNull(timedObject.getId());
  }

  @Test
  void doRetrieveById() throws SQLException {

    TimedObject timedObject = new TimedObject("prova","task",new Date());
    timedObject.setId(1);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(1, timedObject.getId());
    when(resultSet.next()).thenReturn(Boolean.TRUE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(timedObject.getId());
    when(resultSet.getObject("attribute", Serializable.class)).thenReturn(timedObject.getAttribute());
    when(resultSet.getObject("task_type", String.class)).thenReturn(timedObject.getTaskType());
    when(resultSet.getObject("task_date", Date.class)).thenReturn(timedObject.getTaskDate());

    TimedObject result = timedObjectDao.doRetrieveById(timedObject.getId());
    Assertions.assertEquals(timedObject,result);
  }

  @Test
  void doRetrieveByIdCatch() throws SQLException {

    TimedObject timedObject = new TimedObject("prova", "task", new Date());
    timedObject.setId(1);
    SQLException ex = new SQLException();
    when(connection.prepareStatement(anyString())).thenThrow(ex);

    TimedObject result = timedObjectDao.doRetrieveById(1);

    Assertions.assertNull(result);

  }


    @Test
  void doRetrieveAll() throws SQLException {

      TimedObject timedObject1 = new TimedObject("prova","task",new Date());
      TimedObject timedObject2 = new TimedObject("prova2","task2",new Date());
      List<TimedObject> oracolo = Arrays.asList(timedObject1, timedObject2);
      when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
      when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
      when(preparedStatement.executeQuery()).thenReturn(resultSet);
      when(resultSet.getObject("id", Integer.class)).thenReturn(timedObject1.getId(), timedObject2.getId());
      when(resultSet.getObject("attribute", Serializable.class)).thenReturn(timedObject1.getAttribute(),
          timedObject2.getAttribute());
      when(resultSet.getObject("task_type", String.class)).thenReturn(timedObject1.getTaskType(),
          timedObject2.getTaskType());
      when(resultSet.getObject("task_date", Date.class)).thenReturn(timedObject1.getTaskDate(),
          timedObject2.getTaskDate());

      List<TimedObject> timedObjects = timedObjectDao.doRetrieveAll("id");

      Assertions.assertArrayEquals(timedObjects.toArray(), oracolo.toArray());
  }

  @Test
  void doRetrieveAllCatch() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());


    List<TimedObject> timedObjects = timedObjectDao.doRetrieveAll("id");
    Assertions.assertNull(timedObjects);
  }
}