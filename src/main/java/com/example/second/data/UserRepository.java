package com.example.second.data;

import lombok.Cleanup;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class UserRepository implements AutoCloseable {
    private final Connection connection;

    private static volatile UserRepository instance = null;
    private static final Object mutex = new Object();

    private UserRepository() throws Exception {
        this.connection = connect();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    try {
                        instance = new UserRepository();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return instance;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }

    private Connection connect() throws Exception {
        InitialContext cxt = new InitialContext();

        if (cxt == null)
            throw new Exception("No context");

        // Naming service (JNDI) to locate resource
        DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");

        if (ds == null)
            throw new Exception("Data source not found.");

        return ds.getConnection();
    }

    public User findUserById(long id) throws SQLException {
        @Cleanup Statement statement = connection.createStatement();
        @Cleanup ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE id = " + id);
        User user = null;

        return getUserFromResult(resultSet);
    }

    public User findUserByName(String name) throws SQLException {
        @Cleanup PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE name = ?"
        );
        statement.setString(1, name);

        @Cleanup ResultSet resultSet = statement.executeQuery();

        return getUserFromResult(resultSet);
    }

    private User getUserFromResult(ResultSet resultSet) throws SQLException {
        User user = null;

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");

            if (name != null) {
                user = new User(name, password);
            }
        }

        return user;
    }

    public int addUser(User user) throws SQLException {
        @Cleanup PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users(name, password) VALUES (?, ?)"
        );

        statement.setString(1, user.getName());
        statement.setString(2, user.getPassword());

        return statement.executeUpdate();
    }
}
