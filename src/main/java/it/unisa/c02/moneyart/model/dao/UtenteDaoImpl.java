package it.unisa.c02.moneyart.model.dao;


import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.sql.Blob;
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
 * definiti nell'interfaccia UtenteDao.
 */
public class UtenteDaoImpl implements UtenteDao {

  public UtenteDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  public UtenteDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Utente item) {
    String sql =
        "INSERT INTO " + TABLE_NAME
            +
            "(id_sequito, email, pwd, username, nome, cognome, foto, saldo) "
            + " VALUES(?, ? , ?, ?, ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getSeguito().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getEmail().toLowerCase(), Types.VARCHAR);
      preparedStatement.setObject(3, item.getPassword(), Types.VARCHAR);
      preparedStatement.setObject(4, item.getUsername(), Types.VARCHAR);
      preparedStatement.setObject(5, item.getNome(), Types.VARCHAR);
      preparedStatement.setObject(6, item.getCognome(), Types.VARCHAR);
      preparedStatement.setObject(7, item.getFotoProfilo(), Types.BLOB);
      preparedStatement.setObject(8, item.getSaldo(), Types.DOUBLE);
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
  public Utente doRetrieveById(int id) {
    String sql =
        "select * from " + TABLE_NAME
            +
            " where id = ? ";
    Utente utente = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      utente = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utente;
  }

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  @Override
  public List<Utente> doRetrieveAll(String filter) {
    String sql =
        "select * from " + TABLE_NAME;
    List<Utente> utenti = null;
    if (filter != null && !filter.equals("")) {
      sql += " ORDER BY " + filter;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      ResultSet rs = preparedStatement.executeQuery();
      utenti = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utenti;
  }

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Utente item) {
    String sql =
        "UPDATE " + TABLE_NAME
            +
            " set id_seguito = ?, email = ?, pwd = ?, username = ?, nome = ?, cognome = ?, "
            + " foto = ?, saldo = ?"
            + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getSeguito().getId(), Types.INTEGER);
      preparedStatement.setString(2, item.getEmail().toLowerCase());
      preparedStatement.setString(3, item.getPassword());
      preparedStatement.setString(4, item.getUsername());
      preparedStatement.setString(5, item.getNome());
      preparedStatement.setString(6, item.getCognome());
      preparedStatement.setBlob(7, item.getFotoProfilo());
      preparedStatement.setObject(8, item.getSaldo(), Types.DOUBLE);
      preparedStatement.setObject(9, item.getId(), Types.INTEGER);
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
  public void doDelete(Utente item) {
    String sql =
        "delete from " + TABLE_NAME
            +
            " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();


    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Restituisce l'utente in base all'username.
   *
   * @param username l'username dell'utente
   * @return l'utente con quell'username
   */
  @Override
  public Utente doRetrieveByUsername(String username) {
    String sql =
        "select * from " + TABLE_NAME
            +
            " where username = ? ";
    Utente utente = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setString(1, username);

      ResultSet rs = preparedStatement.executeQuery();
      utente = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utente;
  }

  /**
   * Restituisce tutti gli utenti che seguono uno specifico utente.
   *
   * @param id l'identificativo dell'utente di cui vogliamo conoscere i follower
   * @return i follower di quell'utente
   */
  @Override
  public List<Utente> doRetrieveFollowersByUserId(int id) {

    String sql = "SELECT * from " + TABLE_NAME + " where id_seguito = ? ";

    List<Utente> utenti = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      utenti = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utenti;
  }

  /**
   * Metodo privato per restituire un singolo oggetto Notifica dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Notifica
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Utente getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Utente utente = null;
    if (rs.next()) {
      utente = new Utente();
      utente.setId(rs.getObject("id", Integer.class));

      Utente seguito = new Utente();
      seguito.setId(rs.getObject("id_seguito", Integer.class));
      utente.setSeguito(seguito);

      utente.setEmail(rs.getObject("email", String.class));
      utente.setPassword(rs.getObject("pwd", String.class));
      utente.setUsername(rs.getObject("username", String.class));
      utente.setNome(rs.getObject("nome", String.class));
      utente.setCognome(rs.getObject("cognome", String.class));
      utente.setFotoProfilo(rs.getObject("foto", Blob.class));
      utente.setSaldo(rs.getObject("saldo", Float.class));

    }
    return utente;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Utente
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Utente
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Utente> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Utente> utenti = new ArrayList<>();
    while (rs.next()) {
      Utente utente = new Utente();
      utente.setId(rs.getObject("id", Integer.class));

      Utente seguito = new Utente();
      seguito.setId(rs.getObject("id_seguito", Integer.class));
      utente.setSeguito(seguito);

      utente.setEmail(rs.getObject("email", String.class));
      utente.setPassword(rs.getObject("pwd", String.class));
      utente.setUsername(rs.getObject("username", String.class));
      utente.setNome(rs.getObject("nome", String.class));
      utente.setCognome(rs.getObject("cognome", String.class));
      utente.setFotoProfilo(rs.getObject("foto", Blob.class));
      utente.setSaldo(rs.getObject("saldo", Float.class));
      utenti.add(utente);
    }
    return utenti;
  }

  private DataSource ds;

  private static final String TABLE_NAME = "utente";
}
