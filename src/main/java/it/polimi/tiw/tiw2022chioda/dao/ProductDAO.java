package it.polimi.tiw.tiw2022chioda.dao;

import it.polimi.tiw.tiw2022chioda.bean.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DAO {

    public ProductDAO(Connection connection) {
        super(connection);
    }

    public List<Product> getAll() throws SQLException {
        String query = "SELECT CODE, NAME, IMAGE " +
                "FROM PRODUCT";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        ResultSet resultSet = super.coreQueryExecutor(preparedStatement);
        List<Product> result = new ArrayList<>();
        if(!resultSet.isBeforeFirst()) return result;
        while (resultSet.next()) {
            Product product = new Product();
            product.setCode(resultSet.getInt("CODE"));
            product.setImage(resultSet.getString("IMAGE"));
            product.setName(resultSet.getString("NAME"));
            result.add(product);
        }
        return result;
    }

    public Product getByCode(int code) throws SQLException {
        String query = "SELECT CODE, NAME, IMAGE " +
                "FROM PRODUCT " +
                "WHERE CODE = ?";
        PreparedStatement preparedStatement = super.prepareQuery(query);
        preparedStatement.setInt(1, code);
        ResultSet resultSet = super.coreQueryExecutor(preparedStatement);
        List<Product> result = new ArrayList<>();
        if (!resultSet.isBeforeFirst()) return null;
        resultSet.next();
        Product product = new Product();
        product.setCode(resultSet.getInt("CODE"));
        product.setImage(resultSet.getString("IMAGE"));
        product.setName(resultSet.getString("NAME"));
        return product;
    }
}
