package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDAO;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDaoImpl implements UtenteDAO {

    public UtenteDaoImpl() {
        this.ds = Retriever.getIstance(DataSource.class);
    }

    @Override
    public void doCreate(Utente item) {
        String insertSQL =
                "INSERT INTO " + TABLE_NAME +
                        "(id_sequito, email, pwd, username, nome, cognome, foto, saldo) "
                        + " VALUES(?, ? , ?, ?, ?, ?, ?, ?) ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, item.getSeguito(), Types.INTEGER);
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

    @Override
    public Utente doRetrieveById(int id) {
        String sql =
                "select * from " + TABLE_NAME +
                        " where id = ? ";
       Utente utente = null;


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            utente = new Utente();

            while (rs.next()) {
                utente.setId(rs.getObject("id", Integer.class));
                utente.setSeguito(rs.getObject("id_utente", Integer.class));
                utente.setEmail(rs.getObject("email", String.class));
                utente.setPassword(rs.getObject("pwd", String.class));
                utente.setUsername(rs.getObject("username", String.class));
                utente.setNome(rs.getObject("nome", String.class));
                utente.setCognome(rs.getObject("cognome", String.class));
                utente.setFotoProfilo(rs.getObject("foto", Blob.class));
                utente.setSaldo(rs.getObject("saldo", Float.class));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utente;
    }

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
            utenti = new ArrayList<>();

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getObject("id", Integer.class));
                utente.setSeguito(rs.getObject("id_utente", Integer.class));
                utente.setEmail(rs.getObject("email", String.class));
                utente.setPassword(rs.getObject("pwd", String.class));
                utente.setUsername(rs.getObject("username", String.class));
                utente.setNome(rs.getObject("nome", String.class));
                utente.setCognome(rs.getObject("cognome", String.class));
                utente.setFotoProfilo(rs.getObject("foto", Blob.class));
                utente.setSaldo(rs.getObject("saldo", Float.class));
                utenti.add(utente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    @Override
    public void doUpdate(Utente item) {
        String sql =
                "UPDATE " + TABLE_NAME +
                        " set id_seguito = ?, email = ?, pwd = ?, username = ?, nome = ?, cognome = ?, "
                        + " foto = ?, saldo = ?"
                        + " where id = ?";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, item.getSeguito(), Types.INTEGER);
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

    @Override
    public void doDelete(Utente item) {
        String sql =
                "delete from " + TABLE_NAME +
                        " where id = ? ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, item.getId(), Types.INTEGER);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  DataSource ds;

    private static final String TABLE_NAME = "utente";
}
