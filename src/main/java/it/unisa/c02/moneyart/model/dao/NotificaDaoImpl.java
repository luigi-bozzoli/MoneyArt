package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
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
 * definiti nell'interfaccia NotificaDao.
 */
public class NotificaDaoImpl implements NotificaDao {


  public NotificaDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  /**
   * Costruttore, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
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


    String sql = "INSERT INTO " + TABLE_NAME
        + "(id_utente,id_rivendita,id_asta ,letta, tipo,contenuto) "
        + " VALUES(?, ? , ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getUtente().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getRivendita().getId(), Types.INTEGER);
      preparedStatement.setObject(3, item.getAsta().getId(), Types.INTEGER);
      preparedStatement.setObject(4, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setObject(5, item.getTipo().toString(), Types.VARCHAR);
      preparedStatement.setObject(6, item.getContenuto(), Types.VARCHAR);
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
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  @Override
  public Notifica doRetrieveById(int id) {
    String sql =
        "select * from " + TABLE_NAME
            + " where id = ? ";
    Notifica notifica = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      notifica = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifica;
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
    String sql = "select * from " + TABLE_NAME;
    List<Notifica> notifiche = null;
    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY " + filter;
    }


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      ResultSet rs = preparedStatement.executeQuery();
      notifiche = getMultipleResultFromResultSet(rs);


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifiche;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Notifica item) {
    String sql = "UPDATE " + TABLE_NAME
        + " set id_utente = ?, id_rivendita = ? ,id_asta = ? , letta = ?, tipo = ?,contenuto = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getUtente().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getRivendita().getId(), Types.INTEGER);
      preparedStatement.setObject(3, item.getAsta().getId(), Types.INTEGER);
      preparedStatement.setObject(4, item.isLetta(), Types.BOOLEAN);
      preparedStatement.setObject(5, item.getTipo().toString(), Types.VARCHAR);
      preparedStatement.setObject(6, item.getContenuto(), Types.VARCHAR);
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
  public void doDelete(Notifica item) {
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
   * Restituisce tutte le notifiche destinate ad uno specifico utente.
   *
   * @param id l'id del destinatario delle notifiche
   * @return tutte le notifiche destinate a utente
   */
  @Override
  public List<Notifica> doRetrieveAllByUserId(int id) {
    String sql = "select * from " + TABLE_NAME + " where id_utente = ? ";
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      notifiche = getMultipleResultFromResultSet(rs);


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifiche;
  }

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica asta.
   *
   * @param id l'id dell'asta a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento ad asta
   */
  @Override
  public List<Notifica> doRetrieveAllByAuctionId(int id) {
    String sql = "select * from " + TABLE_NAME + " where id_asta = ? ";
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      notifiche = getMultipleResultFromResultSet(rs);


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifiche;
  }

  /**
   * Restituisce tutte le notifiche che fanno riferimento ad una specifica rivendita.
   *
   * @param id l'id della rivendita a cui devono far riferimento le notifiche
   * @return tutte le notifiche che fanno riferimento a rivendita
   */
  @Override
  public List<Notifica> doRetrieveAllByRivenditaId(int id) {
    String sql = "select * from " + TABLE_NAME + " where id_rivendita = ? ";
    List<Notifica> notifiche = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      notifiche = getMultipleResultFromResultSet(rs);


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifiche;
  }

  /**
   * Metodo privato per restituire un singolo oggetto Notifica dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Notifica
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Notifica getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Notifica notifica = null;
    if (rs.next()) {
      notifica = new Notifica();
      notifica.setId(rs.getObject("id", Integer.class));

      Utente utente = new Utente();
      utente.setId(rs.getObject("id_utente", Integer.class));
      notifica.setUtente(utente);

      Rivendita rivendita = new Rivendita();
      rivendita.setId(rs.getObject("id_rivendita", Integer.class));
      notifica.setRivendita(rivendita);

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      notifica.setAsta(asta);

      notifica.setLetta(rs.getObject("letta", Boolean.class));
      notifica.setTipo(Notifica.Tipo.valueOf(rs.getObject("tipo", String.class).toUpperCase()));
      notifica.setContenuto(rs.getObject("contenuto", String.class));


    }
    return notifica;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Notifica
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Notifica
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Notifica> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Notifica> notifiche = new ArrayList<>();
    while (rs.next()) {
      Notifica notifica = new Notifica();
      notifica.setId(rs.getObject("id", Integer.class));

      Utente utente = new Utente();
      utente.setId(rs.getObject("id_utente", Integer.class));
      notifica.setUtente(utente);

      Rivendita rivendita = new Rivendita();
      rivendita.setId(rs.getObject("id_rivendita", Integer.class));
      notifica.setRivendita(rivendita);

      Asta asta = new Asta();
      asta.setId(rs.getObject("id_asta", Integer.class));
      notifica.setAsta(asta);

      notifica.setLetta(rs.getObject("letta", Boolean.class));
      notifica.setTipo(Notifica.Tipo.valueOf(rs.getObject("tipo", String.class).toUpperCase()));
      notifica.setContenuto(rs.getObject("contenuto", String.class));


    }
    return notifiche;
  }


  private DataSource ds;


  private static final String TABLE_NAME = "notifica";
}
