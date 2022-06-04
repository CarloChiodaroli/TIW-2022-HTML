package it.polimi.tiw.tiw2022chioda.dao;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.exception.WrongUserTypeException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstimateDAO extends DAO {

    public EstimateDAO(Connection connection) {
        super(connection);
    }

    public List<Estimate> getByUser(User user) throws WrongUserTypeException, SQLException {
        Map<UserType, String> queries = new HashMap<>();
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE ";
        queries.put(UserType.CLIENT, "WHERE CLIENT = ? ");
        queries.put(UserType.EMPLOYEE, "WHERE EMPLOYEE = ? ");
        String orderBy = "ORDER BY CODE DESC ";

        if (!queries.containsKey(user.getUserType())) throw new WrongUserTypeException(user.getUserType());
        query += queries.get(user.getUserType());
        query += orderBy;

        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, user.getID());
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        if(!result.isBeforeFirst()) return new ArrayList<>();
        return coreEstimateListSetter(result);
    }

    public Estimate getByCode(int code) throws WrongUserTypeException, SQLException {
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE " +
                "WHERE CODE = ?";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, code);
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        if(!result.isBeforeFirst()) return null;
        result.next();
        return coreEstimateSetter(result);
    }

    public List<Estimate> getNotPriced() throws SQLException {
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE " +
                "WHERE EMPLOYEE IS NULL ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        if(!result.isBeforeFirst()) return new ArrayList<>();
        return coreEstimateListSetter(result);
    }

    public Estimate createEstimate(Estimate candidate, User client)
            throws WrongUserTypeException, SQLException {
        controlClient(client);
        super.getConnection().setAutoCommit(false);
        String queryEstimate = "INSERT INTO ESTIMATE (CLIENT, PRODUCT)" +
                "VALUES (?,?)";
        PreparedStatement preparedStatement = super.prepareQuery(queryEstimate, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, client.getID());
        preparedStatement.setInt(2, candidate.getProductCode());
        preparedStatement.executeUpdate();
        ResultSet result = preparedStatement.getGeneratedKeys();
        if (result.next()) {
            candidate.setCode(result.getInt(1));
            try {
                registerOptions(candidate);
            } catch (SQLException e){
                System.err.println("Option addition failed, asked Database Rollback");
                super.getConnection().rollback();
                System.err.println("Rollback done");
                throw e;
            }
            try {
                super.getConnection().commit();
            } catch (SQLException e) {
                System.err.println("Commit failed");
                throw e;
            }
            try {
                super.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Autocommit reset to 'true' failed");
                throw e;
            }
            return candidate;
        } else {
            throw new SQLException("Estimate registration failed, no code obtained");
        }
    }

    public void priceEstimate(Estimate estimate, User employee, double price)
            throws WrongUserTypeException, SQLException {
        controlEmployee(employee);
        String query = "UPDATE ESTIMATE " +
                "SET EMPLOYEE = ?, PRICE = ? " +
                "WHERE CODE = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, employee.getID());
        preparedStatement.setDouble(2, price);
        preparedStatement.setInt(3, estimate.getCode());
        preparedStatement.executeUpdate();
    }

    private Estimate coreEstimateSetter(ResultSet result) throws SQLException {
        Estimate estimate = new Estimate();
        estimate.setClientId(result.getInt("CLIENT"));
        estimate.setEmployeeId(result.getInt("EMPLOYEE"));
        estimate.setPrice(result.getDouble("PRICE"));
        estimate.setProductCode(result.getInt("PRODUCT"));
        estimate.setCode(result.getInt("CODE"));
        return estimate;
    }

    private List<Estimate> coreEstimateListSetter(ResultSet result) throws SQLException {
        List<Estimate> estimateList = new ArrayList<>();
        while (result.next()) {
            estimateList.add(coreEstimateSetter(result));
        }
        return estimateList;
    }

    private void controlEmployee(User user)
            throws WrongUserTypeException {
        if (!user.getUserType().equals(UserType.EMPLOYEE))
            throw new WrongUserTypeException(user.getUserType(), UserType.EMPLOYEE);
    }

    private void controlClient(User user)
            throws WrongUserTypeException {
        if (!user.getUserType().equals(UserType.CLIENT))
            throw new WrongUserTypeException(user.getUserType(), UserType.CLIENT);
    }

    private void registerOptions(Estimate estimate) throws SQLException {
        DecorDAO decorDAO = new DecorDAO(super.getConnection());
        decorDAO.setDecorForEstimate(estimate.getCode(), estimate.getOptionCodes());
    }
}
