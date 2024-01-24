package com.floris.golfscoretrackingsystem.classes;

import com.floris.golfscoretrackingsystem.Applicaction;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Golfer {
    private String firstName;
    private String lastName;
    private int handicap;

    public Golfer(String firstName, String lastName, int handicap) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.handicap = handicap;
    }

    public Golfer(ResultSet rs) throws SQLException {
        this.firstName = rs.getString("firstname");
        this.lastName = rs.getString("lastname");
        this.handicap = rs.getInt("handicap");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }
}
