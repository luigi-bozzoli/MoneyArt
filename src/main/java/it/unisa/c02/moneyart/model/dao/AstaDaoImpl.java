package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia AstaDao.
 */
public class AstaDaoImpl implements AstaDao {

  public AstaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Asta item) {

    String insertSql = "INSERT INTO " + TABLE_NAME
            + "(id_opera, data_inizio, data_fine, stato) "
            + "VALUES(?, ?, ?, ?)";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getIdOpera(), Types.INTEGER);
      preparedStatement.setObject(2, item.getDataInizio(), Types.DATE);
      preparedStatement.setObject(3, item.getDataFine(), Types.DATE);
      preparedStatement.setString(4, item.getStato().toString());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }
  }

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Asta doRetrieveById(int id) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id = ?";

    Asta asta = new Asta();

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        asta.setId(rs.getObject("id", Integer.class));
        asta.setIdOpera(rs.getObject("id_opera", Integer.class));
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }

    return asta;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Asta> doRetrieveAll(String filter) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME;

    // TODO: gestisci eventuali filtri

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(retrieveSql);

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        asta.setIdOpera(rs.getObject("id_opera", Integer.class));
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }

    return aste;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Asta item) {
    String updateSql = "UPDATE " + TABLE_NAME
            + " set id_opera = ?, data_inizio = ?, data_fine = ?, stato = ? WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setObject(1, item.getIdOpera(), Types.INTEGER);
      preparedStatement.setObject(2, item.getDataInizio(), Types.DATE);
      preparedStatement.setObject(3, item.getDataFine(), Types.DATE);
      preparedStatement.setObject(4, item.getStato().toString().toUpperCase());

      preparedStatement.setObject(5, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }
  }

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Asta item) {
    String deleteSql = "DELETE FROM " + TABLE_NAME
            + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }

  }

  /**
   * Restituisce tutte le aste che si trovano nello stato specificato.
   *
   * @param s lo stato in cui si devono trovare le aste cercate
   * @return tutte le aste che si trovano nello stato s
   */
  @Override
  public List<Asta> doRetrieveByStato(Asta.Stato s) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE stato = ?";

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      preparedStatement.setString(1, s.toString().toUpperCase());

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        asta.setIdOpera(rs.getObject("id_opera", Integer.class));
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }

    return aste;
  }

  /**
   * Restituisce le aste associate ad una specifica opera.
   *
   * @param o l'opera di cui si vogliono recuperare le aste
   * @return le aste associate all'opera specificata
   */
  @Override
  public List<Asta> doRetrieveByOpera(Opera o) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_opera = ?";

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      preparedStatement.setObject(1, o.getId(), Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        asta.setIdOpera(rs.getObject("id_opera", Integer.class));
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Gestire meglio le eccezioni
    }

    return aste;
  }

  private DataSource ds;
  private static final String TABLE_NAME = "asta";
}