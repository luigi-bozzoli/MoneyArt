package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
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
 * definiti nell'interfaccia SegnalazioneDao.
 */
public class SegnalazioneDaoImpl implements SegnalazioneDao {

  /**
   * Costruttore, utilizza un datasource istanziato esternamente.
   */
  public SegnalazioneDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public SegnalazioneDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Segnalazione item) {
    String sql = "INSERT INTO " + TABLE_NAME
        + "(id_asta,commento,letta) "
        + " VALUES(?, ? , ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getAsta().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getCommento(), Types.VARCHAR);
      preparedStatement.setObject(3, item.isLetta(), Types.BOOLEAN);

      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getObject(1, Integer.class));
      }
    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql =
        "select * from " + TABLE_NAME
          + " where id = ? ";

    Segnalazione segnalazione = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setObject(1, id, Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();
      segnalazione = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql =
        "select * from " + TABLE_NAME;

    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY " + filter;
    }

    List<Segnalazione> segnalazioni = null;


    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      segnalazioni = new ArrayList<>();

      ResultSet rs = preparedStatement.executeQuery();
      segnalazioni = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql =
        "UPDATE " + TABLE_NAME
        + " set id_asta = ?, commento = ?, letta = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getAsta(), Types.INTEGER);
      preparedStatement.setObject(2, item.getCommento(), Types.VARCHAR);
      preparedStatement.setObject(3, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setObject(4, item.getId(), Types.INTEGER);
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
  public void doDelete(Segnalazione item) {
    String sql =
        "delete from " + TABLE_NAME
        + " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setObject(1, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
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
    String sql =
        "select * from " + TABLE_NAME
        + " where id_asta = ? ";

    List<Segnalazione> segnalazioni = null;

    try (Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      segnalazioni = new ArrayList<>();
      preparedStatement.setObject(1, id, Types.INTEGER);

      ResultSet rs = preparedStatement.executeQuery();
      segnalazioni = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return segnalazioni;
  }

  /**
   * Metodo privato per restituire un singolo oggetto Segnalazione dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Segnalazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Segnalazione getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Segnalazione segnalazione = null;

    if (rs.next()) {
      segnalazione.setId(rs.getObject("id", Integer.class));

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      segnalazione.setAsta(asta);

      segnalazione.setCommento(rs.getObject("commento", String.class));
      segnalazione.setLetta(rs.getObject("letta", Boolean.class));
    }

    return segnalazione;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Segnalazione
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Segnalazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Segnalazione> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Segnalazione> segnalazioni = new ArrayList<>();
    while (rs.next()) {
      Segnalazione segnalazione = new Segnalazione();
      segnalazione.setId(rs.getObject("id", Integer.class));

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      segnalazione.setAsta(asta);

      segnalazione.setCommento(rs.getObject("commento", String.class));
      segnalazione.setLetta(rs.getObject("letta", Boolean.class));

      segnalazioni.add(segnalazione);
    }

    return segnalazioni;
  }

  private static DataSource ds;

  private static final String TABLE_NAME = "segnalazione";
}
