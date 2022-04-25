package it.polimi.tiw.tiw2022chioda.dao;

import it.polimi.tiw.tiw2022chioda.bean.Estimate;
import it.polimi.tiw.tiw2022chioda.bean.User;
import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.exception.WrongUserTypeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstimateDAO extends DAO {

    public EstimateDAO(Connection connection) {
        super(connection);
    }

    public Estimate getByCode(int code) throws WrongUserTypeException, SQLException {
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE " +
                "WHERE CODE = ?";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, code);
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        result.next();
        return coreEstimateSetter(result);
    }

    public List<Estimate> getNotPriced(User user) throws WrongUserTypeException, SQLException {
        controlEmployee(user);
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE " +
                "WHERE EMPLOYEE IS NULL ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        return coreEstimateListSetter(result);
    }

    public List<Estimate> getByUser(User user) throws WrongUserTypeException, SQLException {
        String queryClient = "WHERE CLIENT = ? ";
        String queryEmployee = "WHERE EMPLOYEE = ? ";
        String query = "SELECT CLIENT, EMPLOYEE, PRICE, PRODUCT, CODE " +
                "FROM ESTIMATE ";
        switch (user.getUserType()) {
            case CLIENT -> query = query + queryClient;
            case EMPLOYEE -> query = query + queryEmployee;
            default -> throw new WrongUserTypeException(user.getUserType());
        }
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, user.getID());
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        return coreEstimateListSetter(result);
    }

    private Estimate coreEstimateSetter(ResultSet result) throws SQLException {
        Estimate estimate = new Estimate();
        estimate.setClientId(result.getInt("CLIENT"));
        estimate.setEmployeeId(result.getInt("EMPLOYEE"));
        estimate.setPrice(result.getDouble("PRICE"));
        estimate.setProductCode(result.getInt("PRODUCT"));
        estimate.setCode(result.getInt("CODE"));
        addOptions(estimate);
        return estimate;
    }

    private List<Estimate> coreEstimateListSetter(ResultSet result) throws SQLException {
        List<Estimate> estimateList = new ArrayList<>();
        do {
            estimateList.add(coreEstimateSetter(result));
            result.next();
        } while (result.isAfterLast());
        return estimateList;
    }

    private void addOptions(Estimate estimate) {

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

    public void priceEstimate(Estimate estimate, User employee, int price)
            throws WrongUserTypeException, SQLException {
        controlEmployee(employee);
        String query = "UPDATE ESTIMATE " +
                "SET EMPLOYEE = ?, PRICE = ? " +
                "WHERE CODE = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, employee.getID());
        preparedStatement.setInt(2, price);
        preparedStatement.setInt(3, estimate.getCode());
        preparedStatement.executeUpdate();
    }

    public Estimate createEstimate(Estimate candidate, User client)
            throws WrongUserTypeException, SQLException {
        controlClient(client);
        super.getConnection().setAutoCommit(false);
        String queryEstimate = "INSERT INTO ESTIMATE (CLIENT, PRODUCT)" +
                "VALUES (?,?)";
        PreparedStatement preparedStatement = super.prepareQuery(queryEstimate);
        preparedStatement.setInt(1, client.getID());
        preparedStatement.setInt(2, candidate.getProductCode());
        preparedStatement.executeUpdate();
        ResultSet result = preparedStatement.getGeneratedKeys();
        if (result.next()) {
            candidate.setCode(result.getInt(1));
            registerOptions(candidate);
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

    private void registerOptions(Estimate estimate) throws SQLException {
        DecorDAO decorDAO = new DecorDAO(super.getConnection());
        decorDAO.setDecorForEstimate(estimate.getCode(), estimate.getOptionCodes());
    }
}
