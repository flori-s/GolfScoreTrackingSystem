package com.floris.golfscoretrackingsystem.classes;

import com.floris.golfscoretrackingsystem.Applicaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getGolferId(User u) {
        int i = 0;
        try {
            ResultSet golfer = Applicaction.connection.query("SELECT * FROM Golfer");
            while (golfer.next()) {
                // Assuming getUsername() and getPassword() are methods in the User class
                if (golfer.getString("username").equals(u.getUsername()) && golfer.getString("password").equals(u.getPassword())) {
                    i = golfer.getInt("id");
                    return i;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
