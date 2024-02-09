package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Klasse die een ronde vertegenwoordigt.
 */
public class Round {

    private int id;
    private Date datePlayed;
    private WeatherCondition weatherCondition;
    private Course course;
    private Golfer golfer;
    private Score score;

    /**
     * Initialiseert een nieuwe Round instantie met de opgegeven datum, weersomstandigheden, golfbaan, golfer en score.
     *
     * @param datePlayed         De datum waarop de ronde is gespeeld.
     * @param weatherCondition  De weersomstandigheden tijdens de ronde.
     * @param course             De golfbaan waarop de ronde is gespeeld.
     * @param golfer             De golfer die de ronde heeft gespeeld.
     * @param score              De score van de ronde.
     */
    public Round(Date datePlayed, WeatherCondition weatherCondition, Course course, Golfer golfer, Score score) {
        this.datePlayed = datePlayed;
        this.weatherCondition = weatherCondition;
        this.course = course;
        this.golfer = golfer;
        this.score = score;
    }

    /**
     * Initialiseert een nieuwe Round instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rounds De resultset van de databasequery.
     * @param wc     De weersomstandigheden van de ronde.
     * @param c      De golfbaan van de ronde.
     * @param g      De golfer van de ronde.
     * @param s      De score van de ronde.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public Round(ResultSet rounds, WeatherCondition wc, Course c, Golfer g, Score s) throws SQLException {
        this.id = rounds.getInt("id");
        this.datePlayed = rounds.getDate("dateplayed");
        this.weatherCondition = wc;
        this.course = c;
        this.golfer = g;
        this.score = s;
    }

    /**
     * Geeft de ID van de ronde terug.
     *
     * @return De ID van de ronde.
     */
    public int getId() {
        return id;
    }

    /**
     * Geeft de datum waarop de ronde is gespeeld terug.
     *
     * @return De datum waarop de ronde is gespeeld.
     */
    public Date getDatePlayed() {
        return datePlayed;
    }

    /**
     * Stelt de datum waarop de ronde is gespeeld in.
     *
     * @param datePlayed De datum waarop de ronde is gespeeld.
     */
    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    /**
     * Geeft de weersomstandigheden van de ronde terug.
     *
     * @return De weersomstandigheden van de ronde.
     */
    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    /**
     * Stelt de weersomstandigheden van de ronde in.
     *
     * @param weatherCondition De weersomstandigheden van de ronde.
     */
    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    /**
     * Geeft de golfbaan van de ronde terug.
     *
     * @return De golfbaan van de ronde.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Stelt de golfbaan van de ronde in.
     *
     * @param course De golfbaan van de ronde.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Geeft de golfer van de ronde terug.
     *
     * @return De golfer van de ronde.
     */
    public Golfer getGolfer() {
        return golfer;
    }

    /**
     * Stelt de golfer van de ronde in.
     *
     * @param golfer De golfer van de ronde.
     */
    public void setGolfer(Golfer golfer) {
        this.golfer = golfer;
    }

    /**
     * Geeft de score van de ronde terug.
     *
     * @return De score van de ronde.
     */
    public Score getScore() {
        return score;
    }

    /**
     * Stelt de score van de ronde in.
     *
     * @param score De score van de ronde.
     */
    public void setScore(Score score) {
        this.score = score;
    }
}
