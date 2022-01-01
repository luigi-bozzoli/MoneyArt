package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
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
      preparedStatement.setString(3, item.getNome());
      preparedStatement.setDouble(4, item.getPrezzo());
      preparedStatement.setString(5, item.getDescrizione());
      preparedStatement.setBlob(6, item.getImmagine());
      preparedStatement.setString(7, item.getCertificato());
      preparedStatement.setString(8, item.getStato().toString());
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
    String retriveSql = "SELECT * FROM " + TABLE_NAME
                    + " WHERE id = ? ";
    Opera opera = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retriveSql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      opera = new Opera();

      while (rs.next()) {
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getString("nome"));
        opera.setPrezzo(rs.getDouble("prezzo"));
        opera.setDescrizione(rs.getString("descrizione"));
        opera.setImmagine(rs.getBlob("immagine"));
        opera.setCertificato(rs.getString("certificato"));
        opera.setStato(Opera.Stato.valueOf(rs.getString("stato")));
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
    String retriveSql = "SELECT * FROM " + TABLE_NAME;
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retriveSql)) {

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getString("nome"));
        opera.setPrezzo(rs.getDouble("prezzo"));
        opera.setDescrizione(rs.getString("descrizione"));
        opera.setImmagine(rs.getBlob("immagine"));
        opera.setCertificato(rs.getString("certificato"));
        opera.setStato(Opera.Stato.valueOf(rs.getString("stato")));

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
      preparedStatement.setString(3, item.getNome());
      preparedStatement.setDouble(4, item.getPrezzo());
      preparedStatement.setString(5, item.getDescrizione());
      preparedStatement.setBlob(6, item.getImmagine());
      preparedStatement.setString(7, item.getCertificato());
      preparedStatement.setString(8, item.getStato().toString());
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
  public List<Opera> doRetriveAllByPossessore(Utente utente) {
    String retriveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_utente = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retriveSql)) {
      preparedStatement.setInt(1, utente.getId());

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getString("nome"));
        opera.setPrezzo(rs.getDouble("prezzo"));
        opera.setDescrizione(rs.getString("descrizione"));
        opera.setImmagine(rs.getBlob("immagine"));
        opera.setCertificato(rs.getString("certificato"));
        opera.setStato(Opera.Stato.valueOf(rs.getString("stato")));

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
  public List<Opera> doRetriveAllByArtista(Utente artista) {
    String retriveSql = "SELECT * FROM " + TABLE_NAME
            + " WHERE id_artista = ?";
    List<Opera> opere = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(retriveSql)) {
      preparedStatement.setInt(1, artista.getId());

      ResultSet rs = preparedStatement.executeQuery();
      opere = new ArrayList<>();

      while (rs.next()) {
        Opera opera = new Opera();
        opera.setId(rs.getObject("id", Integer.class));
        opera.setPossessore(rs.getObject("id_utente", Integer.class));
        opera.setArtista(rs.getObject("id_artista", Integer.class));
        opera.setNome(rs.getString("nome"));
        opera.setPrezzo(rs.getDouble("prezzo"));
        opera.setDescrizione(rs.getString("descrizione"));
        opera.setImmagine(rs.getBlob("immagine"));
        opera.setCertificato(rs.getString("certificato"));
        opera.setStato(Opera.Stato.valueOf(rs.getString("stato")));

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
