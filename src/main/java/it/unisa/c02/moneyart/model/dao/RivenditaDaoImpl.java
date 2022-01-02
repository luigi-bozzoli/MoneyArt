package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'intefaccia RivenditaDao.
 */
public class RivenditaDaoImpl implements RivenditaDao {

    /** Costruttore di RivenditaDaoIml
     * Il DataSource viene creato allo startup del server e posto nel context delle servlet
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
    public void doCreate(Rivendita item) {
        String insertSQL =
                "INSERT INTO " + TABLE_NAME +
                        "(id_opera, prezzo, stato) "
                        + " VALUES(?, ? , ?) ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, item.getIdOpera(), Types.INTEGER);
            preparedStatement.setObject(2, item.getPrezzo(), Types.DOUBLE);
            preparedStatement.setObject(3, item.getStato().toString().toLowerCase());
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
    public Rivendita doRetrieveById(int id) {
        String insertSQL =
                "select * from " + TABLE_NAME +
                        " where id = ? ";
        Rivendita rivendita = null;


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            rivendita = new Rivendita();

            while (rs.next()) {
                rivendita.setId(rs.getObject("id", Integer.class));
                rivendita.setIdOpera(rs.getObject("id_opera", Integer.class));
                rivendita.setPrezzo(rs.getObject("prezzo", Double.class));
                rivendita.setStato(Rivendita.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));


            }
            return rivendita;

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
    public List<Rivendita> doRetrieveAll(String filter) {
        String insertSQL =
                "select * from " + TABLE_NAME;
        List<Rivendita> rivendite = null;


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            rivendite = new ArrayList<>();

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Rivendita rivendita = new Rivendita();
                rivendita.setId(rs.getObject("id", Integer.class));
                rivendita.setIdOpera(rs.getObject("id_opera", Integer.class));
                rivendita.setPrezzo(rs.getObject("prezzo", Double.class));
                rivendita.setStato(Rivendita.Stato.valueOf(rs.getObject("stato", String.class).toUpperCase()));
                rivendite.add(rivendita);


            }
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
        String insertSQL =
                "UPDATE " + TABLE_NAME +
                        " set id_opera = ?,prezzo = ?, stato = ?"
                        + " where id = ?";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setObject(1, item.getIdOpera(), Types.INTEGER);
            preparedStatement.setObject(2, item.getStato().toString().toLowerCase());
            preparedStatement.setObject(3, item.getPrezzo(), Types.DOUBLE);
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
        String insertSQL =
                "delete from " + TABLE_NAME +
                        " where id = ? ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setObject(1, item.getId(), Types.INTEGER);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static DataSource ds;
    private static final String TABLE_NAME = "rivendita";
}
