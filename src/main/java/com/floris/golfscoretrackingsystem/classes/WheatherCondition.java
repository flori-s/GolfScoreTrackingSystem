package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WheatherCondition {

    private String conditionName;
    private int windSpeed;
    private int temperature;

    public WheatherCondition(String conditionName, int windSpeed, int temperature) {
        this.conditionName = conditionName;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
    }

    public WheatherCondition(ResultSet rs) throws SQLException {
        this.conditionName = rs.getString("condition");
        this.windSpeed = rs.getInt("windspeed");
        this.temperature = rs.getInt("temperature");
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}


