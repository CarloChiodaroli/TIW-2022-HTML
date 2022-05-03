package it.polimi.tiw.tiw2022chioda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO {

    private final Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }

    protected PreparedStatement prepareQuery(String sql) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("Couldn't precompile SQL statement");
            throw e;
        }
        return preparedStatement;
    }

    protected PreparedStatement prepareQuery(String sql, int statement) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql, statement);
        } catch (SQLException e) {
            System.err.println("Couldn't precompile SQL statement");
            throw e;
        }
        return preparedStatement;
    }

    protected ResultSet coreQueryExecutor(PreparedStatement preparedStatement) throws SQLException {
        ResultSet result;
        try {
            result = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Couldn't execute Query");
            throw e;
        }
        return result;
    }
}
