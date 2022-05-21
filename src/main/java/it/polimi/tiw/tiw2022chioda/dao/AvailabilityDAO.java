package it.polimi.tiw.tiw2022chioda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDAO extends DAO {

    public AvailabilityDAO(Connection connection) {
        super(connection);
    }

    public List<Integer> getFromProduct(int productCode) throws SQLException {
        String query = "SELECT OPT " +
                "FROM AVAILABILITY " +
                "WHERE PRODUCT = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, productCode);
        ResultSet resultSet = super.coreQueryExecutor(preparedStatement);
        List<Integer> result = new ArrayList<>();
        if(!resultSet.isBeforeFirst()) return new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getInt("OPT"));
        }
        return result;
    }
}
