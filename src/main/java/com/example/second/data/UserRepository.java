package com.example.second.data;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class UserRepository {
    private Connection connection = null;

    public UserRepository() {
        try {
            this.connection = connect();
            connection.close();
            smt();
        } catch (Exception ex) {
            System.out.println("Database exception" + ex.getMessage());
        }
    }

    private Connection connect() throws Exception {
        InitialContext cxt = new InitialContext();

        if (cxt == null)
            throw new Exception("No context");

        DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");

        if(ds == null)
            throw  new Exception("Data source not found.");



        return ds.getConnection();
    }

    public void smt() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");
        System.out.println("Success");
    }
}
