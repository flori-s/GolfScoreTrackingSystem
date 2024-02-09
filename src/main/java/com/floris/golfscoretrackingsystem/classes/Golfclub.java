package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die een golfclub vertegenwoordigt.
 */
public class Golfclub {
    private final StringProperty clubName = new SimpleStringProperty();
    private final StringProperty clubType = new SimpleStringProperty();

    /**
     * Initialiseert een nieuwe Golfclub instantie met de opgegeven naam en type.
     *
     * @param clubName De naam van de golfclub.
     * @param clubType Het type van de golfclub.
     */
    public Golfclub(String clubName, String clubType) {
        setClubName(clubName);
        setClubType(clubType);
    }

    /**
     * Initialiseert een nieuwe Golfclub instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rs De resultset van de databasequery.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public Golfclub(ResultSet rs) throws SQLException {
        setClubName(rs.getString("name"));
        setClubType(rs.getString("type"));
    }

    /**
     * Geeft de naam van de golfclub terug.
     *
     * @return De naam van de golfclub.
     */
    public String getClubName() {
        return clubName.get();
    }

    /**
     * Geeft de eigenschap voor de naam van de golfclub terug.
     *
     * @return De eigenschap voor de naam van de golfclub.
     */
    public StringProperty clubNameProperty() {
        return clubName;
    }

    /**
     * Stelt de naam van de golfclub in.
     *
     * @param clubName De naam van de golfclub.
     */
    public void setClubName(String clubName) {
        this.clubName.set(clubName);
    }

    /**
     * Geeft het type van de golfclub terug.
     *
     * @return Het type van de golfclub.
     */
    public String getClubType() {
        return clubType.get();
    }

    /**
     * Geeft de eigenschap voor het type van de golfclub terug.
     *
     * @return De eigenschap voor het type van de golfclub.
     */
    public StringProperty clubTypeProperty() {
        return clubType;
    }

    /**
     * Stelt het type van de golfclub in.
     *
     * @param clubType Het type van de golfclub.
     */
    public void setClubType(String clubType) {
        this.clubType.set(clubType);
    }
}
