package com.floris.golfscoretrackingsystem.classes;

import com.floris.golfscoretrackingsystem.Applicaction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die een gebruiker van het systeem vertegenwoordigt.
 */
public class User {

    private String username;
    private String password;

    /**
     * Initialiseert een nieuwe User instantie met de opgegeven gebruikersnaam en wachtwoord.
     *
     * @param username De gebruikersnaam van de gebruiker.
     * @param password Het wachtwoord van de gebruiker.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Haalt de ID op van de bijbehorende golfer voor de gebruiker.
     *
     * @param u De gebruiker waarvoor de golfer-ID wordt opgehaald.
     * @return De ID van de bijbehorende golfer.
     */
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

    /**
     * Geeft de gebruikersnaam van de gebruiker terug.
     *
     * @return De gebruikersnaam van de gebruiker.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Geeft het wachtwoord van de gebruiker terug.
     *
     * @return Het wachtwoord van de gebruiker.
     */
    public String getPassword() {
        return password;
    }
}
