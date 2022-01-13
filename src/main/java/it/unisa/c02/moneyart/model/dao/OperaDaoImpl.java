package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
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
 * definiti nell'intefaccia OperaDao.
 */
public class OperaDaoImpl implements OperaDao {

  /**
   * Costruttore di OperaDaoImpl, permette di specificare il datasource utilizzato.
   *
   * @param ds il datasource utilizzato
   */
  public OperaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Costruttore vuoto.
   */
  public OperaDaoImpl() {
    this.ds = Retriever.getIstance(DataSource.class);
  }

  /**
   * Inserisce un item del database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public boolean doCreate(Opera item) {
    String insertSql = "INSERT INTO " + TABLE_NAME
        + "(id_utente, id_artista, nome, descrizione, immagine, certificato, stato)"
        + " VALUES(?, ?, ?, ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
             PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getPossessore().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getArtista().getId(), Types.INTEGER);
      preparedStatement.setObject(3, item.getNome(), Types.VARCHAR);
      preparedStatement.setObject(4, item.getDescrizione(), Types.VARCHAR);
      preparedStatement.setObject(5, item.getImmagine(), Types.BLOB);
      preparedStatement.setObject(6, item.getCertificato(), Types.VARCHAR);
      preparedStatement.setObject(7, item.getStato().toString(), Types.VARCHAR);
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet != null && resultSet.next()) {
        item.setId(resultSet.getInt(1));
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
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
  public Opera doRetrieveById(int id) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
        + " WHERE id = ? ";
    Opera opera = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      opera = getSingleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return opera;
  }

  /**
   * Ricerca nel database tutti gli item,
   * eventualmente ordinati tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return lista di item trovata nel database
   */
  @Override
  public List<Opera> doRetrieveAll(String filter) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME;
    List<Opera> opere = null;

    if (filter != null && !filter.equals("")) {
      retrieveSql += " ORDER BY " + filter;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      ResultSet rs = preparedStatement.executeQuery();
      opere = getMultipleResultFromResultSet(rs);


    } catch (SQLException e) {
      e.printStackTrace();
    }

    return opere;
  }

  /**
   * Aggiorna l'item bel database.
   *
   * @param item l'item da aggiornare
   */
  @Override
  public void doUpdate(Opera item) {
    String updateSql = "UPDATE " + TABLE_NAME
        + " SET id_utente = ?, id_artista = ?, nome = ?,"
        + " descrizione = ?, immagine = ?, certificato = ?, stato = ?"
        + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setObject(1, item.getPossessore().getId(), Types.INTEGER);
      preparedStatement.setObject(2, item.getArtista().getId(), Types.INTEGER);
      preparedStatement.setObject(3, item.getNome(), Types.VARCHAR);
      preparedStatement.setObject(4, item.getDescrizione(), Types.VARCHAR);
      preparedStatement.setObject(5, item.getImmagine(), Types.BLOB);
      preparedStatement.setObject(6, item.getCertificato(), Types.VARCHAR);
      preparedStatement.setObject(7, item.getStato().toString(), Types.VARCHAR);
      preparedStatement.setObject(8, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Elimina item dal database.
   *
   * @param item l'item da eliminare
   */
  @Override
  public void doDelete(Opera item) {
    String deleteSql = "DELETE FROM " + TABLE_NAME
        + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {

      preparedStatement.setObject(1, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * Restituisce tutte le opere che sono in possesso dell'utente.
   *
   * @param id l'id dell'utente possessore delle opere
   * @return tutte le opere in possesso dell'utente
   */
  @Override
  public List<Opera> doRetrieveAllByOwnerId(int id) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
        + " WHERE id_utente = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      opere = getMultipleResultFromResultSet(rs);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return opere;
  }

  /**
   * Restituisce tutte le opere create dall'utente.
   *
   * @param id l'id dell'utente creatore delle opere
   * @return tutte le opere create dall'utente
   */
  @Override
  public List<Opera> doRetrieveAllByArtistId(int id) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
        + " WHERE id_artista = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      opere = getMultipleResultFromResultSet(rs);
    } catch (SQLException e) {

      e.printStackTrace();
    }

    return opere;
  }

  /**
   * Restituisce tutte le opere avente un dato nome.
   *
   * @param name il nome dell'opera da cercare delle
   * @return tutte le opere con il nome dell'argomento
   */
  @Override
  public List<Opera> doRetrieveAllByName(String name) {

    if (name != null) {
      String retrieveSql = "SELECT * FROM " + TABLE_NAME
          + " WHERE nome LIKE %?%";

      List<Opera> opere = null;

      try (Connection connection = ds.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
        preparedStatement.setString(1, name);

        ResultSet rs = preparedStatement.executeQuery();
        opere = getMultipleResultFromResultSet(rs);
      } catch (SQLException e) {

        e.printStackTrace();
      }

      return opere;
    }

    return null;
  }

  /**
   * Metodo privato per restituire un singolo oggetto Opera dopo aver
   * effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return l'oggetto Opera
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private Opera getSingleResultFromResultSet(ResultSet rs) throws SQLException {
    Opera opera = null;
    if (rs.next()) {
      opera = new Opera();
      opera.setId(rs.getObject("id", Integer.class));

      Utente possessore = new Utente();
      possessore.setId(rs.getObject("id_utente", Integer.class));
      opera.setPossessore(possessore);

      Utente artista = new Utente();
      artista.setId(rs.getObject("id_artista", Integer.class));
      opera.setArtista(artista);

      opera.setNome(rs.getObject("nome", String.class));
      opera.setDescrizione(rs.getObject("descrizione", String.class));
      opera.setImmagine(rs.getBlob("immagine"));
      opera.setCertificato(rs.getObject("certificato", String.class));
      opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));
    }
    return opera;
  }

  /**
   * Metodo privato per restituire una collezione di oggetti Opera
   * dopo aver effettuato un'interrogazione al db.
   *
   * @param rs il ResultSet
   * @return la collezione di oggetti Opera
   * @throws SQLException l'eccezione sql lanciata in caso di errore
   */
  private List<Opera> getMultipleResultFromResultSet(ResultSet rs) throws SQLException {
    List<Opera> opere = new ArrayList<>();
    while (rs.next()) {
      Opera opera = new Opera();
      opera.setId(rs.getObject("id", Integer.class));

      Utente possessore = new Utente();
      possessore.setId(rs.getObject("id_utente", Integer.class));
      opera.setPossessore(possessore);

      Utente artista = new Utente();
      artista.setId(rs.getObject("id_artista", Integer.class));
      opera.setArtista(artista);

      opera.setNome(rs.getObject("nome", String.class));
      opera.setDescrizione(rs.getObject("descrizione", String.class));
      opera.setImmagine(rs.getBlob("immagine"));
      opera.setCertificato(rs.getObject("certificato", String.class));
      opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));
      opere.add(opera);
    }
    return opere;
  }


  private DataSource ds;
  private static final String TABLE_NAME = "opera";
}
