package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDAO;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia UtenteDao.
 */
public class UtenteDaoImpl implements UtenteDAO {

    public UtenteDaoImpl() {
        this.ds = Retriever.getIstance(DataSource.class);
    }

    /**
     * Inserisce un item nel database.
     *
     * @param item l'oggetto da inserire nel database
     */
    @Override
    public void doCreate(Utente item) {
        String insertSQL =
                "INSERT INTO " + TABLE_NAME +
                        "(id_sequito, email, pwd, username, nome, cognome, foto, saldo) "
                        + " VALUES(?, ? , ?, ?, ?, ?, ?, ?) ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,
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
                utente.setSeguito(rs.getObject("id_seguito", Utente.class));
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
            utenti = new ArrayList<>();

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getObject("id", Integer.class));
                utente.setSeguito(rs.getObject("id_seguito", Utente.class));
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

    /**
     * Aggiorna l'item nel database.
     *
     * @param item l'item da aggiornare
     */
    @Override
    public void doUpdate(Utente item) {
        String sql =
                "UPDATE " + TABLE_NAME +
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

    /**
     * Restituisce l'utente in base all'username
     *
     * @param username l'username dell'utente
     * @return l'utente con quell'username
     */
    @Override
    public Utente doRetrieveByUsername(String username) {
        String sql =
                "select * from " + TABLE_NAME +
                        " where username = ? ";
        Utente utente = null;


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            utente = new Utente();

            while (rs.next()) {
                utente.setId(rs.getObject("id", Integer.class));
                utente.setSeguito(rs.getObject("id_seguito", Utente.class));
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

    /**
     * Restituisce tutti gli utenti che seguono utente
     *
     * @param utente l'utente di cui vogliamo conoscere i follower
     * @return i follower di quell'utente
     */
    @Override
    public List<Utente> getFollowers(Utente utente) {

        String sql = "SELECT U2.id, U2.id_seguito, U2.email, U2.pwd, U2.username, "+
                            "U2.nome, U2.cognome, U2.foto, U2.saldo " +
                     "FROM "+ TABLE_NAME +" as U1, "+ TABLE_NAME +" as U2 " +
                     "WHERE U1.id = U2.id_seguito ";

        List<Utente> utenti = null;

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            utenti = new ArrayList<>();

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Utente utente1 = new Utente();
                utente1.setId(rs.getObject("id", Integer.class));
                utente1.setSeguito(rs.getObject("id_seguito", Utente.class));
                utente1.setEmail(rs.getObject("email", String.class));
                utente1.setPassword(rs.getObject("pwd", String.class));
                utente1.setUsername(rs.getObject("username", String.class));
                utente1.setNome(rs.getObject("nome", String.class));
                utente1.setCognome(rs.getObject("cognome", String.class));
                utente1.setFotoProfilo(rs.getObject("foto", Blob.class));
                utente1.setSaldo(rs.getObject("saldo", Float.class));
                utenti.add(utente1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    private  DataSource ds;

    private static final String TABLE_NAME = "utente";
}
