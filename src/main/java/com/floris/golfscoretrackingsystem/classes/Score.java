package com.floris.golfscoretrackingsystem.classes;

public class Score {
    private int strokes;
    private String notes;

    public Score(int strokes, String notes) {
        this.strokes = strokes;
        this.notes = notes;
    }

    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes = strokes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
