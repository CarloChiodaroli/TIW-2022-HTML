package it.polimi.tiw.tiw2022chioda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DecorDAO extends DAO {

    public DecorDAO(Connection connection) {
        super(connection);
    }

    public List<Integer> getOptionCodesFromEstimateCode(int estimateCode)
            throws SQLException {
        String query = "SELECT OPT " +
                "FROM DECOR " +
                "WHERE ESTIMATE = ? ";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, estimateCode);
        ResultSet result = super.coreQueryExecutor(preparedStatement);
        List<Integer> codes = new ArrayList<>();
        do {
            codes.add(result.getInt("OPT"));
            result.next();
        } while (!result.isAfterLast());
        return codes;
    }

    public void setDecorForEstimate(int estimateCode, List<Integer> optionCodes)
            throws SQLException{
        String query = "INSERT INTO DECOR (ESTIMATE, OPT) " +
                "VALUES (?, ?) ";
        for(Integer optionCode: optionCodes){
            PreparedStatement preparedStatement = super.prepareQuery(query);
            preparedStatement.setInt(1, estimateCode);
            preparedStatement.setInt(2, optionCode);
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if(!result.next()){
                String error = "Could not register " + optionCode + " option";
                System.err.println(error);
                throw new SQLException(error);
            }
        }
    }


}
