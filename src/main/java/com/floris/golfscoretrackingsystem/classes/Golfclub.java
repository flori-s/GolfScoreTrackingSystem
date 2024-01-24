package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Golfclub {
    private String clubName;
    private String clubType;

    public Golfclub(String clubName, String clubType) {
        this.clubName = clubName;
        this.clubType = clubType;
    }

    public Golfclub(ResultSet rs) throws SQLException {
        this.clubName = rs.getString("name");
        this.clubType = rs.getString("type");
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }
}
