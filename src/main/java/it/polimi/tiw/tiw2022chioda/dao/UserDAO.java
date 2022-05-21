package it.polimi.tiw.tiw2022chioda.dao;

import it.polimi.tiw.tiw2022chioda.bean.User;

import java.sql.*;

public class UserDAO extends DAO {

    public UserDAO(Connection connection) {
        super(connection);
    }

    public User registerCredentials(User candidate, String password)
            throws SQLException {
        String query = "INSERT INTO USER (username, email, usertype, password)" +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = super.prepareQuery(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, candidate.getUsername());
        preparedStatement.setString(2, candidate.getEmail());
        preparedStatement.setString(3, candidate.getUserTypeAsString());
        preparedStatement.setString(4, password);
        preparedStatement.executeUpdate();
        ResultSet result = preparedStatement.getGeneratedKeys();
        if (result.next()) {
            candidate.setID(result.getInt(1));
            return candidate;
        } else {
            throw new SQLException("User registration failed, no ID obtained");
        }
    }

    public boolean isUsernamePresent(String username) throws SQLException {
        String query = "SELECT USERNAME " +
                "FROM USER " +
                "WHERE USERNAME = ?";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setString(1, username);
        ResultSet result;
        try {
            result = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Couldn't execute Query");
            throw e;
        }
        return result.isBeforeFirst();
    }

    public User checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT ID, USERNAME, EMAIL, USERTYPE " +
                "FROM USER " +
                "WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        return coreUserGetter(preparedStatement);
    }

    public User getById(int id) throws SQLException {
        String query = "SELECT ID, USERNAME, EMAIL, USERTYPE " +
                "FROM USER " +
                "WHERE ID = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, id);
        return coreUserGetter(preparedStatement);
    }

    private User coreUserGetter(PreparedStatement preparedStatement) throws SQLException {
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        if(!result.isBeforeFirst()) return null;
        result.next();
        User user = new User();
        user.setID(result.getInt("ID"));
        user.setUserType(result.getString("USERTYPE"));
        user.setUsername(result.getString("USERNAME"));
        user.setEmail(result.getString("EMAIL"));
        return user;
    }
}
