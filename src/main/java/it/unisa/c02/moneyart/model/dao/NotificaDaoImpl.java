package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
         PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
      preparedStatement.setInt(1, item.getIdUtente());
      preparedStatement.setInt(2, item.getIdRivendita());
      preparedStatement.setInt(3, item.getIdAsta());
      preparedStatement.setBoolean(4, item.isLetta());
      preparedStatement.setString(5, item.getTipo().toString().toLowerCase());
      preparedStatement.setString(6, item.getContenuto());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }


  }

  @Override
  public Notifica doRetrieveById(int id) {
    return null;
  }

  @Override
  public List<Notifica> doRetrieveAll(String filter) {
    return null;
  }

  @Override
  public void doUpdate(Notifica item) {

  }

  @Override
  public void doDelete(Notifica item) {

  }

  private static DataSource ds;

  static {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      ds = (DataSource) envCtx.lookup("jdbc/storage");
      System.out.println(ds);

    } catch (NamingException e) {
      System.out.println("Error:" + e.getMessage());
    }
  }

  private static final String TABLE_NAME = "notifica";
}
