package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die een score vertegenwoordigt voor een ronde.
 */
public class Score {
    private final IntegerProperty strokes = new SimpleIntegerProperty();
    private final StringProperty notes = new SimpleStringProperty();

    /**
     * Initialiseert een nieuwe Score instantie met het opgegeven aantal slagen en opmerkingen.
     *
     * @param strokes Het aantal slagen voor de score.
     * @param notes   Opmerkingen voor de score.
     */
    public Score(int strokes, String notes) {
        setStrokes(strokes);
        setNotes(notes);
    }

    /**
     * Initialiseert een nieuwe Score instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rs De resultset van de databasequery.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public Score(ResultSet rs) throws SQLException {
        setStrokes(rs.getInt("strokes"));
        setNotes(rs.getString("notes"));
    }

    /**
     * Geeft het aantal slagen voor de score terug.
     *
     * @return Het aantal slagen voor de score.
     */
    public int getStrokes() {
        return strokes.get();
    }

    /**
     * Geeft een IntegerProperty terug dat het aantal slagen voor de score vertegenwoordigt.
     *
     * @return Een IntegerProperty voor het aantal slagen.
     */
    public IntegerProperty strokesProperty() {
        return strokes;
    }

    /**
     * Stelt het aantal slagen voor de score in.
     *
     * @param strokes Het aantal slagen voor de score.
     */
    public void setStrokes(int strokes) {
        this.strokes.set(strokes);
    }

    /**
     * Geeft de opmerkingen voor de score terug.
     *
     * @return De opmerkingen voor de score.
     */
    public String getNotes() {
        return notes.get();
    }

    /**
     * Geeft een StringProperty terug dat de opmerkingen voor de score vertegenwoordigt.
     *
     * @return Een StringProperty voor de opmerkingen.
     */
    public StringProperty notesProperty() {
        return notes;
    }

    /**
     * Stelt de opmerkingen voor de score in.
     *
     * @param notes De opmerkingen voor de score.
     */
    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
