package com.floris.golfscoretrackingsystem.classes;

import com.floris.golfscoretrackingsystem.Applicaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die een golfer vertegenwoordigt.
 */
public class Golfer {
    private String firstName;
    private String lastName;
    private int handicap;

    /**
     * Initialiseert een nieuwe Golfer instantie met de opgegeven voornaam, achternaam en handicap.
     *
     * @param firstName De voornaam van de golfer.
     * @param lastName  De achternaam van de golfer.
     * @param handicap  Het handicap van de golfer.
     */
    public Golfer(String firstName, String lastName, int handicap) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.handicap = handicap;
    }

    /**
     * Initialiseert een nieuwe Golfer instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rs De resultset van de databasequery.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public Golfer(ResultSet rs) throws SQLException {
        this.firstName = rs.getString("firstname");
        this.lastName = rs.getString("lastname");
        this.handicap = rs.getInt("handicap");
    }

    /**
     * Geeft de voornaam van de golfer terug.
     *
     * @return De voornaam van de golfer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Stelt de voornaam van de golfer in.
     *
     * @param firstName De voornaam van de golfer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Geeft de achternaam van de golfer terug.
     *
     * @return De achternaam van de golfer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Stelt de achternaam van de golfer in.
     *
     * @param lastName De achternaam van de golfer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Geeft het handicap van de golfer terug.
     *
     * @return Het handicap van de golfer.
     */
    public int getHandicap() {
        return handicap;
    }

    /**
     * Stelt het handicap van de golfer in.
     *
     * @param handicap Het handicap van de golfer.
     */
    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public int getTotalRounds(User currentUser) throws SQLException {
        int totalRounds = 0;
        String query =
                "SELECT COUNT(Round.id) AS total_rounds, Round.dateplayed\n" +
                        "FROM Round\n" +
                        "JOIN Golfer ON Round.golferID = Golfer.id\n" +
                        "WHERE Golfer.username = ?\n" +
                        "GROUP BY Round.dateplayed;";

        PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(query);
        preparedStatement.setString(1, currentUser.getUsername());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            totalRounds += resultSet.getInt("total_rounds");
        }

        System.out.println(totalRounds);
        return totalRounds;
    }
}
