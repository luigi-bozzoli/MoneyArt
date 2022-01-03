package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia PartecipazioneDao.
 */

public class PartecipazioneDaoImpl implements PartecipazioneDao {

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
    String insertSql = "INSERT INTO" + TABLE_NAME
        + "(id, id_utente, id_asta, offerta) "
        + " VALUES (?, ?, ?, ?) ";

    try (
        Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql,
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
      throw new IllegalArgumentException(e.getMessage());
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
    String insertSql =
        "select * from " + TABLE_NAME
          + " where id = ? ";

    Partecipazione partecipazione = null;


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setInt(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      partecipazione = new Partecipazione();

      while (rs.next()) {
        partecipazione.setId(rs.getObject("id", Integer.class));
        partecipazione.setIdUtente(rs.getObject("id_utente", Integer.class));
        partecipazione.setIdAsta(rs.getObject("id_asta", Integer.class));
        partecipazione.setOfferta(rs.getObject("offerta", Double.class));

      }

      return partecipazione;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
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

    String insertSql = "select * from " + TABLE_NAME;
    List<Partecipazione> partecipanti = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

      partecipanti = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Partecipazione partecipazione = new Partecipazione();

        partecipazione.setId(rs.getObject("id", Integer.class));
        partecipazione.setIdUtente(rs.getObject("id_utente", Integer.class));
        partecipazione.setIdAsta(rs.getObject("id_asta", Integer.class));
        partecipazione.setOfferta(rs.getObject("offerta", Double.class));
        partecipanti.add(partecipazione);

      }
      return partecipanti;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Ricerca nel database tutti gli utenti che hanno partecipato ad un'asta.
   *
   * @param id identificativo di un'asta
   * @return la collezione di utenti trovata nel database
   */
  @Override
  public List<Utente> doRetrieveByUserId(int id) {
    String insertSql = "select id_utente from" + TABLE_NAME
        + " where id_asta = ? ";

    List<Utente> utenti = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

      utenti = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Utente utente = new Utente();

        utente.setUsername(rs.getObject("username", String.class));
        utente.setNome(rs.getObject("nome", String.class));
        utente.setCognome(rs.getObject("cognome", String.class));
        utenti.add(utente);

      }
      return utenti;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }

  }



  /**
   * Restituisce tutte le aste a cui ha partecipato un utente.
   *
   * @param id identificativo di un utente
   * @return la collezione di aste trovate nel database
   */
  @Override
  public List<Asta> doRetrieveByAuctionId(int id) {
    String insertSql = "select id_asta from" + TABLE_NAME
        + " where id_utente = ? ";

    List<Asta> aste = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

      aste = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Asta asta = new Asta();

        asta.setDataInizio(rs.getObject("data_inizio", Date.class));
        asta.setDataFine(rs.getObject("data_fine", Date.class));
        aste.add(asta);

      }
      return aste;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Ricerca tutte le offerte avanzate da un utente.
   *
   * @param id identificativo di un utente
   * @return una lista di offerte
   */
  @Override
  public List<Double> doRetrieveOffers(int id) {
    String insertSql = "select offerta from" + TABLE_NAME
        + " where id_utente = ? ";

    List<Double> offerte = null;

    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

      offerte = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {

        Partecipazione partecipazione = new Partecipazione();

        offerte.add(rs.getObject("offerta", Double.class));

      }
      return offerte;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }
  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */

  @Override
  public void doUpdate(Partecipazione item) {
    String insertSql = "UPDATE " + TABLE_NAME
        + " set id = ?, id_utente = ?, id_asta = ? , offerta = ? "
        + " where id = ?";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setObject(7, item.getId(), Types.INTEGER);
      preparedStatement.setObject(1, item.getIdUtente(), Types.INTEGER);
      preparedStatement.setObject(3, item.getIdAsta(), Types.INTEGER);
      preparedStatement.setObject(4, item.getOfferta(), Types.DOUBLE);
      preparedStatement.setObject(7, item.getId(), Types.INTEGER);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }
  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */

  @Override
  public void doDelete(Partecipazione item) {
    String insertSql = "delete from " + TABLE_NAME
        + " where id = ? ";


    try (Connection connection = ds.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setObject(1, item.getId(), Types.INTEGER);

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Variabili d'istanza.
   */
  private static DataSource ds;
  private static final String TABLE_NAME = "partecipazione";

}
