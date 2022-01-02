package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
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
 * definiti nell'interfaccia SegnalazioneDao.
 */
public class SegnalazioneDaoImpl implements SegnalazioneDao {
  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Segnalazione item) {
    String insertSql = "INSERT INTO " + TABLE_NAME
        + "(id_asta,commento,letta) "
        + " VALUES(?, ? , ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(2, item.getCommento(), Types.VARCHAR);
      preparedStatement.setObject(3, item.isLetta(), Types.BOOLEAN);

      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getObject(1, Integer.class));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Segnalazione doRetrieveById(int id) {
    String retrieveSql =
        "select * from " + TABLE_NAME
          + " where id = ? ";

    Segnalazione segnalazione = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      preparedStatement.setObject(1, id, Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();
      segnalazione = new Segnalazione();

      if (rs.next()) {
        segnalazione.setId(rs.getObject("id", Integer.class));
        segnalazione.setIdAsta(rs.getObject("id_asta", Integer.class));
        segnalazione.setCommento(rs.getObject("commento", String.class));
        segnalazione.setLetta(rs.getObject("letta", Boolean.class));
      }

    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
    return segnalazione;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Segnalazione> doRetrieveAll(String filter) {
    String retrieveSql =
        "select * from " + TABLE_NAME;

    if (filter != null && !filter.equals("")) {
      retrieveSQL.concat(filter);
    }

    List<Segnalazione> segnalazioni = null;


    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      segnalazioni = new ArrayList<>();

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setId(rs.getObject("id", Integer.class));
        segnalazione.setIdAsta(rs.getObject("id_asta", Integer.class));
        segnalazione.setCommento(rs.getObject("commento", String.class));
        segnalazione.setLetta(rs.getObject("letta", Boolean.class));

        segnalazioni.add(segnalazione);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }

    return segnalazioni;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Segnalazione item) {
    String updateSql =
        "UPDATE " + TABLE_NAME
        + " set id_asta = ?, commento = ?, letta = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setObject(1, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(2, item.getCommento(), Types.VARCHAR);
      preparedStatement.setObject(3, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setObject(4, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }



  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Segnalazione item) {
    String deleteSql =
        "delete from " + TABLE_NAME
        + " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {

      preparedStatement.setObject(1, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }

  /**
   * Ricerca nel database tutte le segnalazioni collegate
   * ad una particolare asta.
   *
   * @param id l'identificativo dell'asta segnalata
   * @return una collezione di segnalazioni di una specifica asta
   */
  @Override
  public List<Segnalazione> doRetrieveByAuctionId(int id) {
    String retrieveSql =
        "select * from " + TABLE_NAME
        + " where id_asta = ? ";

    List<Segnalazione> segnalazioni = null;

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      segnalazioni = new ArrayList<>();
      preparedStatement.setObject(1, id, Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setId(rs.getObject("id", Integer.class));
        segnalazione.setIdAsta(rs.getObject("id_asta", Integer.class));
        segnalazione.setCommento(rs.getObject("commento", String.class));
        segnalazione.setLetta(rs.getObject("letta", Boolean.class));
        segnalazioni.add(segnalazione);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }

    return segnalazioni;
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

  private static final String TABLE_NAME = "segnalazione";
}
