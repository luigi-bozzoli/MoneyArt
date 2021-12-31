package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
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


  public NotificaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

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

  private DataSource ds;


  private static final String TABLE_NAME = "notifica";

  /**
   * Restituisce tutte le notifiche destinate ad uno specifico utente.
   *
   * @param utente il destinatario delle notifiche
   * @return tutte le notifiche destinate a utente
   */
  @Override
  public List<Notifica> doRetriveAllByUtente(Utente utente) {
    String insertSql = "select * from " + TABLE_NAME + " where id_utente = ? ";
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setInt(1, utente.getId());
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
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica asta.
   *
   * @param asta l'asta a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento ad asta
   */
  @Override
  public List<Notifica> doRetriveAllByAsta(Asta asta) {
    String insertSql = "select * from " + TABLE_NAME + " where id_asta = ? ";
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setInt(1, asta.getId());
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
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica rivendita.
   *
   * @param rivendita la rivendita a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento a rivendita
   */
  @Override
  public List<Notifica> doRetiveAllByRivendita(Rivendita rivendita) {
    String insertSql = "select * from " + TABLE_NAME + " where id_rivendita = ? ";
    List<Notifica> notifiche;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setInt(1, rivendita.getId());
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
}
