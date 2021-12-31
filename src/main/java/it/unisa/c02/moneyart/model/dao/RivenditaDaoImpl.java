package it.unisa.c02.moneyart.model.dao;

import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class RivenditaDaoImpl implements RivenditaDao {
    @Override
    public void doCreate(Rivendita item) {

        String insertSQL =
                "INSERT INTO " + TABLE_NAME +
                        "(id_opera, prezzo, stato) "
                        + " VALUES(?, ? , ?) ";


        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, item.getId(), Types.INTEGER);
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

    @Override
    public Rivendita doRetrieveById(int id) {
        return null;
    }

    @Override
    public List<Rivendita> doRetrieveAll(String filter) {
        return null;
    }

    @Override
    public void doUpdate(Rivendita item) {

    }

    @Override
    public void doDelete(Rivendita item) {

    }

    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            ds = (DataSource) envCtx.lookup("jdbc/storage");

        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    private static final String TABLE_NAME = "rivendita";
}
