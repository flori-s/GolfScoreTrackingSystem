package com.floris.golfscoretrackingsystem.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Round {

    private Date datePlayed;
    private ArrayList<Golfclub> golfclubs = new ArrayList<>();
    private WheatherCondition wheatherCondition;
    private Course course;
    private Golfer golfer;
    private Score score;

    public Round(Date datePlayed, WheatherCondition wheatherCondition, Course course, Golfer golfer, Score score) {
        this.datePlayed = datePlayed;
        this.wheatherCondition = wheatherCondition;
        this.course = course;
        this.golfer = golfer;
        this.score = score;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public WheatherCondition getWheatherCondition() {
        return wheatherCondition;
    }

    public void setWheatherCondition(WheatherCondition wheatherCondition) {
        this.wheatherCondition = wheatherCondition;
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
