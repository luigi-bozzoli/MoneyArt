package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
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

  public AstaDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public AstaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public boolean doCreate(Asta item) {

    String sql = "INSERT INTO " + TABLE_NAME
            + "(id_opera, data_inizio, data_fine, stato) "
            + "VALUES(?, ?, ?, ?)";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getOpera().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getDataInizio(), Types.DATE);
      preparedStatement.setObject(3, item.getDataFine(), Types.DATE);
      preparedStatement.setObject(4, item.getStato().toString(), Types.VARCHAR);

      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();

    }return false;
  }

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Asta doRetrieveById(int id) {
    String sql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id = ?";

    Asta asta = null;

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        Opera opera = new Opera();
        opera.setId(rs.getObject("id_opera", Integer.class));
        asta.setOpera(opera);
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));
      }

    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql = "SELECT * FROM " + TABLE_NAME;

    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY " + filter;
    }

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(sql);

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        Opera opera = new Opera();
        opera.setId(rs.getObject("id_opera", Integer.class));
        asta.setOpera(opera);
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql = "UPDATE " + TABLE_NAME
            + " SET id_opera = ?, data_inizio = ?, data_fine = ?, stato = ? WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getOpera().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getDataInizio(), Types.DATE);
      preparedStatement.setObject(3, item.getDataFine(), Types.DATE);
      preparedStatement.setObject(4, item.getStato().toString().toUpperCase(), Types.VARCHAR);

      preparedStatement.setObject(5, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Asta item) {
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

  /**
   * Restituisce tutte le aste che si trovano nello stato specificato.
   *
   * @param s lo stato in cui si devono trovare le aste cercate
   * @return tutte le aste che si trovano nello stato s
   */
  @Override
  public List<Asta> doRetrieveByStato(Asta.Stato s) {
    String sql = "SELECT * FROM " + TABLE_NAME
            + " WHERE stato = ?";

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setObject(1, s.toString().toUpperCase(), Types.VARCHAR);

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        Opera opera = new Opera();
        opera.setId(rs.getObject("id_opera", Integer.class));
        asta.setOpera(opera);
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return aste;
  }

  /**
   * Restituisce le aste associate ad una specifica opera.
   *
   * @param id l'identificativo dell'opera di cui si vogliono recuperare le aste
   * @return le aste associate all'opera specificata
   */
  @Override
  public List<Asta> doRetrieveByOperaId(int id) {
    String sql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_opera = ?";

    List<Asta> aste = new ArrayList<>();

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setObject(1, id, Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id", Integer.class));
        Opera opera = new Opera();
        //opera.setId(rs.getObject("id_opera", Integer.class));
        opera.setId(id);
        asta.setOpera(opera);
        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        asta.setStato(Asta.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));

        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return aste;
  }

  private DataSource ds;
  private static final String TABLE_NAME = "asta";
}