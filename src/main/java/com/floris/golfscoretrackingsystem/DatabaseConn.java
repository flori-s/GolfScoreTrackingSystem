package com.floris.golfscoretrackingsystem;

import java.sql.*;
import java.util.Properties;

public class DatabaseConn {

    private Connection connection;
    private final Properties properties = new Properties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException ex) {
            System.err.println("MySQL Driver not found ( More info: https://dev.mysql.com/downloads/connector/j/ )");
        }
    }

    DatabaseConn(String hostname, String username, String password, String database) {
        properties.setProperty("hostname", hostname);
        properties.setProperty("port", "8889");
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("database", database);
    }
    private void connect() {
        String url = "jdbc:mysql://%s:%s/%s?".formatted(
                this.properties.getProperty("hostname"),
                this.properties.getProperty("port"),
                this.properties.getProperty("database")
        );

        try {
            this.connection = DriverManager.getConnection(url, this.properties);
        } catch (SQLException ex) {
            this.connection = null;
        }
    }

    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            this.connection = null;
        }
        this.connection = null;
    }

    public ResultSet query(String query) throws SQLException {
        if(this.isConnected()) { this.connect(); }

        Statement statement = this.connection.createStatement();

        return statement.executeQuery(query);
    }


    public boolean isConnected() {
        try {
            return this.connection == null || this.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public Connection getConnection() {
        if(this.isConnected()) { this.connect(); }
        return this.connection;
    }
}
