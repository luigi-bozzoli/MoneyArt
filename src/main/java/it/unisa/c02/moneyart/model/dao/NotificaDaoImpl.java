package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class NotificaDaoImpl implements NotificaDao {
  @Override
  public void doCreate(Notifica item) {
    String insertSQL =
        "INSERT INTO " + TABLE_NAME +
            "(id_utente,id_rivendita,id_asta ,letta, tipo,contenuto) "
            + " VALUES(?, ? , ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getIdUtente(), Types.INTEGER);
      preparedStatement.setObject(2, item.getIdRivendita(), Types.INTEGER);
      preparedStatement.setObject(3, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(4, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setString(5, item.getTipo().toString().toLowerCase());
      preparedStatement.setString(6, item.getContenuto());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }


  }

  @Override
  public Notifica doRetrieveById(int id) {
    String insertSQL =
        "select * from " + TABLE_NAME +
            " where id = ? ";
    Notifica notifica = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      notifica = new Notifica();

      while (rs.next()) {
        notifica.setId(rs.getObject("id", Integer.class));
        notifica.setIdUtente(rs.getObject("id_utente", Integer.class));
        notifica.setIdRivendita(rs.getObject("id_rivendita", Integer.class));
        notifica.setIdAsta(rs.getObject("id_asta", Integer.class));
        notifica.setLetta(rs.getObject("letta", Boolean.class));
        notifica.setTipo(Notifica.Tipo.valueOf(rs.getObject("tipo", String.class).toUpperCase()));
        notifica.setContenuto(rs.getObject("contenuto", String.class));


      }
      return notifica;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public List<Notifica> doRetrieveAll(String filter) {
    String insertSQL =
        "select * from " + TABLE_NAME;
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
      notifiche = new ArrayList<>();

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Notifica notifica = new Notifica();
        notifica.setId(rs.getObject("id", Integer.class));
        notifica.setIdUtente(rs.getObject("id_utente", Integer.class));
        notifica.setIdRivendita(rs.getObject("id_rivendita", Integer.class));
        notifica.setIdAsta(rs.getObject("id_asta", Integer.class));
        notifica.setLetta(rs.getObject("letta", Boolean.class));
        notifica.setTipo(Notifica.Tipo.valueOf(rs.getObject("tipo", String.class).toUpperCase()));
        notifica.setContenuto(rs.getObject("contenuto", String.class));
        notifiche.add(notifica);


      }
      return notifiche;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void doUpdate(Notifica item) {
    String insertSQL =
        "UPDATE " + TABLE_NAME +
            " set id_utente = ?, id_rivendita = ? ,id_asta = ? , letta = ?, tipo = ?,contenuto = ? "
            + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
      preparedStatement.setObject(1, item.getIdUtente(), Types.INTEGER);
      preparedStatement.setObject(2, item.getIdRivendita(), Types.INTEGER);
      preparedStatement.setObject(3, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(4, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setString(5, item.getTipo().toString().toLowerCase());
      preparedStatement.setString(6, item.getContenuto());
      preparedStatement.setObject(7, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void doDelete(Notifica item) {
    String insertSQL =
        "delete from " + TABLE_NAME +
            " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();


    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }

  }

  private static DataSource ds;

  static {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      ds = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      System.out.println("Error:" + e.getMessage());
    }
  }

  private static final String TABLE_NAME = "notifica";
}
