package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Golfclub {
    private final StringProperty clubName = new SimpleStringProperty();
    private final StringProperty clubType = new SimpleStringProperty();

    public Golfclub(String clubName, String clubType) {
        setClubName(clubName);
        setClubType(clubType);
    }

    public Golfclub(ResultSet rs) throws SQLException {
        setClubName(rs.getString("name"));
        setClubType(rs.getString("type"));
    }


    public String getClubName() {
        return clubName.get();
    }

    public StringProperty clubNameProperty() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName.set(clubName);
    }

    public String getClubType() {
        return clubType.get();
    }

    public StringProperty clubTypeProperty() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType.set(clubType);
    }
}
