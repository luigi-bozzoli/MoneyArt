package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
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

  public OperaDaoImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Inserisce un item del database.
   *
   * @param item l'oggetto da inserire nel database
   */
  @Override
  public void doCreate(Opera item) {
    String insertSql = "INSERT INTO " + TABLE_NAME
            + "(id_utente, id_artista, nome, prezzo, descrizione, immagine, certificato, stato)"
            + " VALUES(?, ? , ?, ?, ?, ?, ?, ?) ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
                 PreparedStatement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setObject(1, item.getPossessore(), Types.INTEGER);
      preparedStatement.setObject(2, item.getArtista(), Types.INTEGER);
      preparedStatement.setObject(3, item.getNome(), Types.VARCHAR);
      preparedStatement.setObject(4, item.getPrezzo(), Types.DOUBLE);
      preparedStatement.setObject(5, item.getDescrizione(), Types.VARCHAR);
      preparedStatement.setObject(6, item.getImmagine(), Types.BLOB);
      preparedStatement.setObject(7, item.getCertificato(), Types.VARCHAR);
      preparedStatement.setObject(8, item.getStato().toString(), Types.VARCHAR);
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
      opera = new Opera();

      while (rs.next()) {
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getObject("nome", String.class));
        opera.setPrezzo(rs.getObject("prezzo", Double.class));
        opera.setDescrizione(rs.getObject("descrizione", String.class));
        opera.setImmagine(rs.getObject("immagine", Blob.class));
        opera.setCertificato(rs.getObject("certificato", String.class));
        opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));
      }

      return opera;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
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
      retrieveSql += "ORDER BY " + filter;
    }

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getObject("nome", String.class));
        opera.setPrezzo(rs.getObject("prezzo", Double.class));
        opera.setDescrizione(rs.getObject("descrizione", String.class));
        opera.setImmagine(rs.getObject("immagine", Blob.class));
        opera.setCertificato(rs.getObject("certificato", String.class));
        opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));

        opere.add(opera);
      }

      return opere;

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
  public void doUpdate(Opera item) {
    String updateSql = "UPDATE " + TABLE_NAME
            + " SET id_utente = ?, id_artista = ?, nome = ?, prezzo = ?,"
            + " descrizione = ?, immagine = ?, certificato = ?, stato = ?"
            + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setObject(1, item.getPossessore(), Types.INTEGER);
      preparedStatement.setObject(2, item.getArtista(), Types.INTEGER);
      preparedStatement.setObject(3, item.getNome(), Types.VARCHAR);
      preparedStatement.setObject(4, item.getPrezzo(), Types.DOUBLE);
      preparedStatement.setObject(5, item.getDescrizione(), Types.VARCHAR);
      preparedStatement.setObject(6, item.getImmagine(), Types.BLOB);
      preparedStatement.setObject(7, item.getCertificato(), Types.VARCHAR);
      preparedStatement.setObject(8, item.getStato().toString(), Types.VARCHAR);
      preparedStatement.setObject(9, item.getId(), Types.INTEGER);
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
  public void doDelete(Opera item) {
    String deleteSql = "DELETE FROM " + TABLE_NAME
            + " WHERE id = ?";

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {

      preparedStatement.setObject(1, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Restituisce tutte le opere in possesso di uno specifico utente.
   *
   * @param utente l'utente possessore delle opere
   * @return lista di opere in possesso dell'utente
   */
  @Override
  public List<Opera> doRetrieveAllByPossessore(Utente utente) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_utente = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      preparedStatement.setInt(1, utente.getId());

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getObject("nome", String.class));
        opera.setPrezzo(rs.getObject("prezzo", Double.class));
        opera.setDescrizione(rs.getObject("descrizione", String.class));
        opera.setImmagine(rs.getObject("immagine", Blob.class));
        opera.setCertificato(rs.getObject("certificato", String.class));
        opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));

        opere.add(opera);
      }

      return opere;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Restituisce tutte le opere create e caricate da uno specifico utente.
   *
   * @param artista l'utente creatore delle opere
   * @return lista di opere create dall'utente
   */
  @Override
  public List<Opera> doRetrieveAllByArtista(Utente artista) {
    String retrieveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_artista = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retrieveSql)) {
      preparedStatement.setInt(1, artista.getId());

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getObject("nome", String.class));
        opera.setPrezzo(rs.getObject("prezzo", Double.class));
        opera.setDescrizione(rs.getObject("descrizione", String.class));
        opera.setImmagine(rs.getObject("immagine", Blob.class));
        opera.setCertificato(rs.getObject("certificato", String.class));
        opera.setStato(Opera.Stato.valueOf(rs.getObject("stato", String.class)));

        opere.add(opera);
      }

      return opere;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private DataSource ds;
  private static final String TABLE_NAME = "opera";
}
