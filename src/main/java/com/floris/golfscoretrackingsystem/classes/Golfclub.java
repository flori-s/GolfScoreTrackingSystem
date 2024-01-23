package com.floris.golfscoretrackingsystem.classes;

public class Golfclub {
    private String clubName;
    private String clubType;

    public Golfclub(String clubName, String clubType) {
        this.clubName = clubName;
        this.clubType = clubType;
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
