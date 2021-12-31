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

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia NotificaDao.
 */
public class NotificaDaoImpl implements NotificaDao {

  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Notifica item) {


    String insertSql = "INSERT INTO " + TABLE_NAME
        + "(id_utente,id_rivendita,id_asta ,letta, tipo,contenuto) "
        + " VALUES(?, ? , ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
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

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Notifica doRetrieveById(int id) {
    String insertSql =
        "select * from " + TABLE_NAME
        + " where id = ? ";
    Notifica notifica = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
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

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Notifica> doRetrieveAll(String filter) {
    String insertSql = "select * from " + TABLE_NAME;
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
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

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Notifica item) {
    String insertSql = "UPDATE " + TABLE_NAME
        + " set id_utente = ?, id_rivendita = ? ,id_asta = ? , letta = ?, tipo = ?,contenuto = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
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

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Notifica item) {
    String insertSql = "delete from " + TABLE_NAME
        + " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();


    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }

  }

  private static DataSource ds;

  //il DataSource viene creato allo startup del server e messo nel context delle servlet,
  // in teoria si dovrebbe passare dalla servlet come parametro di un costruttore della classe DAO
  // todo costruttore NotificaDaoImpl(ds) e cancellare lo snippet sottostante


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
