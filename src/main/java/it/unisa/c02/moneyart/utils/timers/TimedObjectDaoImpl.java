package it.unisa.c02.moneyart.utils.timers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;


/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia TimedObjecDao.
 */
public class TimedObjectDaoImpl implements TimedObjectDao {

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public TimedObjectDaoImpl(DataSource ds) {
    this.ds = ds;
  }


  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public boolean doCreate(TimedObject item) {
    String sql = "INSERT INTO " + TABLE_NAME
        + "(attribute, task_type, task_date) "
        + "VALUES(?, ?, ?)";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutput out = null;

      out = new ObjectOutputStream(bos);
      out.writeObject(item.getAttribute());

      byte[] yourBytes = bos.toByteArray();
      out.close();

      Blob blob = new SerialBlob(yourBytes);
      preparedStatement.setBlob(1, blob);
      preparedStatement.setObject(2, item.getTaskType(), Types.VARCHAR);
      preparedStatement.setObject(3, item.getTaskDate(), Types.TIMESTAMP);
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
        return true;
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
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
        Blob blob = rs.getBlob("attribute");
        ObjectInputStream objectInputStream = new ObjectInputStream(blob.getBinaryStream());
        Serializable serializable = (Serializable) objectInputStream.readObject();
        objectInputStream.close();

        timedObject.setAttribute(serializable);
        timedObject.setId(rs.getObject("id", Integer.class));
        timedObject.setTaskType(rs.getObject("task_type", String.class));
        timedObject.setTaskDate(rs.getObject("task_date", Date.class));

      }

    } catch (SQLException | IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return timedObject;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
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
        Blob blob = rs.getBlob("attribute");
        ObjectInputStream objectInputStream = new ObjectInputStream(blob.getBinaryStream());
        Serializable serializable = (Serializable) objectInputStream.readObject();
        objectInputStream.close();

        timedObject.setAttribute(serializable);
        timedObject.setId(rs.getObject("id", Integer.class));
        timedObject.setTaskType(rs.getObject("task_type", String.class));
        timedObject.setTaskDate(rs.getObject("task_date", Date.class));

        timedObjects.add(timedObject);

      }

    } catch (SQLException | IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return timedObjects;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(TimedObject item) {

    String sql = "UPDATE " + TABLE_NAME
        + " SET attribute = ?, task_type = ?, task_date = ? WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutput out = null;

      out = new ObjectOutputStream(bos);
      out.writeObject(item.getAttribute());

      byte[] yourBytes = bos.toByteArray();
      out.close();

      Blob blob = new SerialBlob(yourBytes);
      preparedStatement.setBlob(1, blob);
      preparedStatement.setObject(2, item.getTaskType(), Types.VARCHAR);
      preparedStatement.setObject(3, item.getTaskDate(), Types.TIMESTAMP);
      preparedStatement.setObject(4, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
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

  private static final String TABLE_NAME = "timer";

}
