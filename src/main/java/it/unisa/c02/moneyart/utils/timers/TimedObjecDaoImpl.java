package it.unisa.c02.moneyart.utils.timers;

import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

public class TimedObjecDaoImpl implements TimedObjectDao {


  public TimedObjecDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  public TimedObjecDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class, "Timer");
  }

  @Override
  public void doCreate(TimedObject item) {
    String sql = "INSERT INTO " + TABLE_NAME
        + "(attribute, task_type, task_date) "
        + "VALUES(?, ?, ?)";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getAttribute());
      preparedStatement.setObject(2, item.getTaskType(), Types.VARCHAR);
      preparedStatement.setObject(3, item.getTaskDate(), Types.TIMESTAMP);
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public TimedObject doRetrieveById(int id) {
    String sql = "SELECT * FROM " + TABLE_NAME
        + " WHERE id = ?";

    TimedObject timedObject = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        timedObject = new TimedObject();

        timedObject.setId(rs.getObject("id", Integer.class));
        timedObject.setAttribute(rs.getObject("attribute", Serializable.class));
        timedObject.setTaskType(rs.getObject("task_type", String.class));
        timedObject.setTaskDate(rs.getObject("task_date", Date.class));

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return timedObject;
  }

  @Override
  public List<TimedObject> doRetrieveAll(String filter) {
    String sql = "SELECT * FROM " + TABLE_NAME;

    List<TimedObject> timedObjects = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


      ResultSet rs = preparedStatement.executeQuery();
      timedObjects = new ArrayList<>();

      while (rs.next()) {
        TimedObject timedObject = new TimedObject();

        timedObject.setId(rs.getObject("id", Integer.class));
        timedObject.setAttribute(rs.getObject("attribute", Serializable.class));
        timedObject.setTaskType(rs.getObject("task_type", String.class));
        timedObject.setTaskDate(rs.getObject("task_date", Date.class));

        timedObjects.add(timedObject);

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return timedObjects;
  }

  @Override
  public void doUpdate(TimedObject item) {

    String sql = "UPDATE " + TABLE_NAME
        + " SET attribute = ?, task_type = ?, task_date = ? WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getAttribute());
      preparedStatement.setObject(2, item.getTaskType(), Types.VARCHAR);
      preparedStatement.setObject(3, item.getTaskDate(), Types.TIMESTAMP);
      preparedStatement.setObject(4, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void doDelete(TimedObject item) {
    String sql = "DELETE FROM " + TABLE_NAME
        + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private DataSource ds;
  private static String TABLE_NAME = "timer";

}
