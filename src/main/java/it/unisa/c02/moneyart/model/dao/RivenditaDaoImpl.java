package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'intefaccia RivenditaDao.
 */
public class RivenditaDaoImpl implements RivenditaDao {

  public RivenditaDaoImpl() {
    this.ds = (DataSource) Retriever.getInstance(DataSource.class);
  }

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public RivenditaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item del database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public boolean doCreate(Rivendita item) {
    String sql =
        "INSERT INTO " + TABLE_NAME
        + "(id_opera, prezzo, stato) "
        + " VALUES(?, ? , ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getOpera().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getPrezzo(), Types.DOUBLE);
      preparedStatement.setObject(3, item.getStato().toString().toLowerCase());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
    return false;

  }

  /**
   * Ricerca nel database un item tramite un identificativo univoco.
   *
   * @param id l'identificativo dell'item
   * @return item trovato nel database
   */
  @Override
  public Rivendita doRetrieveById(int id) {
    String sql =
        "select * from " + TABLE_NAME
        + " where id = ? ";
    Rivendita rivendita;
    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();

      rivendita = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }

    return rivendita;
  }

  /**
   * Ricerca nel database tutti gli item,
   * eventualmente ordinati tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return lista di item trovata nel database
   */
  @Override
  public List<Rivendita> doRetrieveAll(String filter) {
    String sql =
        "select * from " + TABLE_NAME;
    List<Rivendita> rivendite = null;

    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY " + filter;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      rivendite = new ArrayList<>();

      ResultSet rs = preparedStatement.executeQuery();
      rivendite = getMultipleResultFromResultSet(rs);

      return rivendite;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Aggiorna l'item bel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Rivendita item) {
    String sql =
        "UPDATE " + TABLE_NAME
        + " set id_opera = ?,prezzo = ?, stato = ?"
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getOpera().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getPrezzo(), Types.DOUBLE);
      preparedStatement.setObject(3, item.getStato().toString().toLowerCase());
      preparedStatement.setObject(4, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Elimina item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Rivendita item) {
    String sql =
        "delete from " + TABLE_NAME
        + " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();


    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Ricerca nel database tutti gli item
   * con un determinato stato.
   *
   * @param s stato ricercato
   * @return lista di item trovata nel database
   */
  @Override
  public List<Rivendita> doRetrieveByStato(String s) {
    String sql =
        "select * from " + TABLE_NAME
        + " where stato = ? ";

    List<Rivendita> rivendite = null;

    if (s.isEmpty()) {
      return null;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      rivendite = new ArrayList<>();

      preparedStatement.setObject(1, s);
      ResultSet rs = preparedStatement.executeQuery();
      rivendite = getMultipleResultFromResultSet(rs);

      return rivendite;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }

  }

  /**
   * Metodo privato per restituire un singolo oggetto Rivendita dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Segnalazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Rivendita getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Rivendita rivendita = null;
    if (rs.next()) {
      rivendita = new Rivendita();
      rivendita.setId(rs.getObject("id", Integer.class));

      Opera opera = new Opera();
      opera.setId(rs.getObject("id_opera", Integer.class));
      rivendita.setOpera(opera);

      rivendita.setPrezzo(rs.getObject("prezzo", Double.class));
      rivendita.setStato(Rivendita.Stato.valueOf(rs.getObject("stato", String.class)
          .toUpperCase()));
    }
    return rivendita;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Rivendita
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Segnalazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Rivendita> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Rivendita> rivendite = new ArrayList<>();
    while (rs.next()) {
      Rivendita rivendita = new Rivendita();
      rivendita.setId(rs.getObject("id", Integer.class));

      Opera opera = new Opera();
      opera.setId(rs.getObject("id_opera", Integer.class));
      rivendita.setOpera(opera);

      rivendita.setPrezzo(rs.getObject("prezzo", Double.class));
      rivendita.setStato(Rivendita.Stato.valueOf(rs.getObject("stato", String.class)
          .toUpperCase()));
      rivendite.add(rivendita);


    }
    return rivendite;
  }

  private static DataSource ds;
  private static final String TABLE_NAME = "rivendita";
}
