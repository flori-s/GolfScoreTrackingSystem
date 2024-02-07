package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Round {

    private int id;
    private Date datePlayed;
    private ArrayList<Golfclub> golfclubs = new ArrayList<>();
    private WeatherCondition weatherCondition;
    private Course course;
    private Golfer golfer;
    private Score score;

    public Round(Date datePlayed, WeatherCondition weatherCondition, Course course, Golfer golfer, Score score) {
        this.datePlayed = datePlayed;
        this.weatherCondition = weatherCondition;
        this.course = course;
        this.golfer = golfer;
        this.score = score;
    }

    public Round(ResultSet rounds, WeatherCondition wc, Course c, Golfer g, Score s) throws SQLException {
        this.id = rounds.getInt("id");
        this.datePlayed = rounds.getDate("dateplayed");
        this.weatherCondition = wc;
        this.course = c;
        this.golfer = g;
        this.score = s;
    }

    public int getId() {
        return id;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public WeatherCondition getWheatherCondition() {
        return weatherCondition;
    }

    public void setWheatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Golfer getGolfer() {
        return golfer;
    }

    public void setGolfer(Golfer golfer) {
        this.golfer = golfer;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
