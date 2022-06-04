package it.polimi.tiw.tiw2022chioda.dao;

import it.polimi.tiw.tiw2022chioda.bean.Option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OptionDAO extends DAO {

    public OptionDAO(Connection connection) {
        super(connection);
    }

    public Option getFromCode(int optionCode) throws SQLException {
        String query = "SELECT NAME, TYPE " +
                "FROM OPT " +
                "WHERE CODE = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, optionCode);
        ResultSet resultSet = super.coreQueryExecutor(preparedStatement);
        if (!resultSet.isBeforeFirst()) return null;
        resultSet.next();
        Option option = new Option();
        option.setCode(optionCode);
        option.setType(resultSet.getString("TYPE"));
        option.setName(resultSet.getString("NAME"));
        return option;
    }

    public List<Integer> codesFromEstimate(int estimateCode) throws SQLException {
        DecorDAO decorDAO = new DecorDAO(getConnection());
        return decorDAO.getOptionCodesFromEstimateCode(estimateCode);
    }

    public List<Integer> codesFromProduct(int productCode) throws SQLException {
        AvailabilityDAO availabilityDAO = new AvailabilityDAO(getConnection());
        return availabilityDAO.getFromProduct(productCode);
    }
}
