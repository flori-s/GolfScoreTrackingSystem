package com.floris.golfscoretrackingsystem;

import java.sql.*;
import java.util.Properties;

/**
 * Deze klasse implementeert een databaseverbinding voor de Golf Score Tracking System applicatie.
 */
public class DatabaseConn {

    private Connection connection;
    private final Properties properties = new Properties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL Driver not found ( More info: https://dev.mysql.com/downloads/connector/j/ )");
        }
    }

    /**
     * Constructor voor het initialiseren van de databaseverbinding.
     * @param hostname De hostname van de database.
     * @param username De gebruikersnaam voor toegang tot de database.
     * @param password Het wachtwoord voor toegang tot de database.
     * @param database De naam van de database.
     */
    DatabaseConn(String hostname, String username, String password, String database) {
        properties.setProperty("hostname", hostname);
        properties.setProperty("port", "3306");
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("database", database);
    }

    /**
     * Maakt verbinding met de database.
     */
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

    /**
     * Geeft de huidige databaseverbinding terug.
     * @return De huidige databaseverbinding.
     */
    public Connection getConnection() {
        if (this.isConnected()) {
            this.connect();
        }
        return this.connection;
    }

    /**
     * Controleert of er een actieve databaseverbinding is.
     * @return True als er een actieve verbinding is, anders False.
     */
    public boolean isConnected() {
        try {
            return this.connection == null || this.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Sluit de huidige databaseverbinding.
     */
    public void disconnect() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            this.connection = null;
        }
        this.connection = null;
    }

    /**
     * Voert een query uit op de database.
     * @param query De SQL-query die moet worden uitgevoerd.
     * @return Het resultaat van de query.
     * @throws SQLException Als er een fout optreedt tijdens het uitvoeren van de query.
     */
    public ResultSet query(String query) throws SQLException {
        if (this.isConnected()) {
            this.connect();
        }

        Statement statement = this.connection.createStatement();

        return statement.executeQuery(query);
    }

    /**
     * Voert een SQL-instructie uit op de database.
     * @param query De SQL-instructie die moet worden uitgevoerd.
     * @throws SQLException Als er een fout optreedt tijdens het uitvoeren van de instructie.
     */
    public void execute(String query) throws SQLException {
        if (!isConnected()) {
            connect();
        }

        Statement statement = connection.createStatement();
        statement.execute(query);
    }

    /**
     * Bereidt een SQL-instructie voor op uitvoering.
     * @param sql De SQL-instructie die moet worden voorbereid.
     * @return Het voorbereide statement.
     * @throws SQLException Als er een fout optreedt tijdens het voorbereiden van de instructie.
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return preparedStatement;
    }

    /**
     * Voert een updatequery uit op de database.
     * @param query De updatequery die moet worden uitgevoerd.
     * @return Het aantal rijen dat door de query is beïnvloed.
     * @throws SQLException Als er een fout optreedt tijdens het uitvoeren van de query.
     */
    public int updateQuery(String query) throws SQLException {
        if (this.isConnected()) {
            this.connect();
        }

        Statement statement = this.connection.createStatement();

        return statement.executeUpdate(query);
    }
}
