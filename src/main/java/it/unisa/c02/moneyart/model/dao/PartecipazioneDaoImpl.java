package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Part;
import javax.sql.DataSource;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia PartecipazioneDao.
 */

public class PartecipazioneDaoImpl implements PartecipazioneDao {

  /**
   * Costruttore, utilizza un datasource istanziato esternamente.
   */
  public PartecipazioneDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public PartecipazioneDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item nel DataBase.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Partecipazione item) {
    String sql = "INSERT INTO " + TABLE_NAME
        + "(id, id_utente, id_asta, offerta) "
        + " VALUES (?, ?, ?, ?) ";

    try (
        Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getIdUtente(), Types.INTEGER);
      preparedStatement.setObject(3, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(4, item.getOfferta(), Types.DOUBLE);
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Ricerca nel database un item tramite un identificativo univoco.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Partecipazione doRetrieveById(int id) {
    String sql =
        "select * from " + TABLE_NAME
          + " where id = ? ";

    Partecipazione partecipazione = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      partecipazione = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return partecipazione;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Partecipazione> doRetrieveAll(String filter) {

    String sql = "select * from " + TABLE_NAME;
    List<Partecipazione> partecipanti = null;

    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY" + filter;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      ResultSet rs = preparedStatement.executeQuery();
      partecipanti = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return partecipanti;
  }

  /**
   * Ricerca nel database tutti gli utenti che hanno partecipato ad un'asta.
   *
   * @param id identificativo di un'asta
   * @return la collezione di utenti trovata nel database
   */
  @Override
  public List<Partecipazione> doRetrieveByAuctionId(int id) {
    String sql = "select distinct id_utente from " + TABLE_NAME
        + " where id_asta = ? ";

    List<Partecipazione> utenti = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      utenti = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Partecipazione partecipazione = new Partecipazione();
        Utente utente = new Utente();
        utente.setId(rs.getObject("id_utente", Utente.class));
        partecipazione.setIdUtente(utente);
        utenti.add(utente);

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return utenti;
  }



  /**
   * Restituisce tutte le aste a cui ha partecipato un utente.
   *
   * @param id identificativo di un utente
   * @return la collezione di aste trovate nel database
   */
  @Override
  public List<Asta> doRetrieveByUserId(int id) {
    String sql = "select id_asta from " + TABLE_NAME
        + " where id_utente = ? ";

    List<Asta> aste = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      aste = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setId(rs.getObject("id_asta", Integer.class));
        aste.add(asta);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return aste;
  }

  /**
   * Ricerca tutte le offerte avanzate da un utente.
   *
   * @param id identificativo di un utente
   * @return una lista di offerte
   */
  @Override
  public List<Double> doRetrieveOffers(int id) {
    String sql = "select offerta from " + TABLE_NAME
        + " where id_utente = ? and id_asta = ? ";

    List<Double> offerte = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      offerte = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {

        offerte.add(rs.getObject("offerta", Double.class));

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return offerte;
  }
  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */

  @Override
  public void doUpdate(Partecipazione item) {
    String sql = "UPDATE " + TABLE_NAME
        + " set id = ?, id_utente = ?, id_asta = ? , offerta = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(7, item.getId(), Types.INTEGER);
      preparedStatement.setObject(1, item.getIdUtente(), Types.INTEGER);
      preparedStatement.setObject(3, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(4, item.getOfferta(), Types.DOUBLE);
      preparedStatement.setObject(7, item.getId(), Types.INTEGER);
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
  public void doDelete(Partecipazione item) {
    String sql = "delete from " + TABLE_NAME
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
   * Metodo privato per restituire un singolo oggetto Partecipazione dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Partecipazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Partecipazione getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Partecipazione partecipazione = null;

    if (rs.next()) {
      partecipazione = new Partecipazione();
      partecipazione.setId(rs.getObject("id", Integer.class));

      Utente utente = new Utente();
      utente.setId(rs.getObject("id_utente", Integer.class));
      partecipazione.setIdUtente(utente);

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      partecipazione.setIdAsta(asta);

      partecipazione.setOfferta(rs.getObject("offerta", Double.class));
    }

    return partecipazione;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Partecipazione
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Partecipazione
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Partecipazione> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Partecipazione> partecipanti = new ArrayList<>();
    while (rs.next()) {
      Partecipazione partecipazione = new Partecipazione();

      partecipazione.setId(rs.getObject("id", Integer.class));

      Utente utente = new Utente();
      utente.setId(rs.getObject("id_utente", Integer.class));
      partecipazione.setIdUtente(utente);

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      partecipazione.setIdAsta(asta);

      partecipazione.setOfferta(rs.getObject("offerta", Double.class));
      partecipanti.add(partecipazione);


    }

    return partecipanti;
  }
  
  /**
   * Variabili d'istanza.
   */
  private DataSource ds;
  private static final String TABLE_NAME = "partecipazione";

}
