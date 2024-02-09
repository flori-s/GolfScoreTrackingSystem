package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die de weersomstandigheden voor een ronde vertegenwoordigt.
 */
public class WeatherCondition {

    private String conditionName;
    private int windSpeed;
    private int temperature;

    /**
     * Initialiseert een nieuwe WeatherCondition instantie met de opgegeven naam van de omstandigheden, windsnelheid en temperatuur.
     *
     * @param conditionName De naam van de weersomstandigheden.
     * @param windSpeed     De windsnelheid.
     * @param temperature   De temperatuur.
     */
    public WeatherCondition(String conditionName, int windSpeed, int temperature) {
        this.conditionName = conditionName;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
    }

    /**
     * Initialiseert een nieuwe WeatherCondition instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rs De resultset van de databasequery.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public WeatherCondition(ResultSet rs) throws SQLException {
        this.conditionName = rs.getString("condition");
        this.windSpeed = rs.getInt("windspeed");
        this.temperature = rs.getInt("temperature");
    }

    /**
     * Geeft de naam van de weersomstandigheden terug.
     *
     * @return De naam van de weersomstandigheden.
     */
    public String getConditionName() {
        return conditionName;
    }

    /**
     * Stelt de naam van de weersomstandigheden in.
     *
     * @param conditionName De naam van de weersomstandigheden.
     */
    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    /**
     * Geeft de windsnelheid terug.
     *
     * @return De windsnelheid.
     */
    public int getWindSpeed() {
        return windSpeed;
    }

    /**
     * Stelt de windsnelheid in.
     *
     * @param windSpeed De windsnelheid.
     */
    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Geeft de temperatuur terug.
     *
     * @return De temperatuur.
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Stelt de temperatuur in.
     *
     * @param temperature De temperatuur.
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
