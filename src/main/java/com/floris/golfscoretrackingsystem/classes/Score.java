package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Score {
    private final IntegerProperty strokes = new SimpleIntegerProperty();
    private final StringProperty notes = new SimpleStringProperty();

    public Score(int strokes, String notes) {
        setStrokes(strokes);
        setNotes(notes);
    }

    public Score(ResultSet rs) throws SQLException {
        setStrokes(rs.getInt("strokes"));
        setNotes(rs.getString("notes"));
    }

    public int getStrokes() {
        return strokes.get();
    }

    public IntegerProperty strokesProperty() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes.set(strokes);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
