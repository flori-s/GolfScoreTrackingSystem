package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasse die een golfbaan vertegenwoordigt.
 */
public class Course {

    private final StringProperty courseName = new SimpleStringProperty();
    private final StringProperty courseLocation = new SimpleStringProperty();

    /**
     * Initialiseert een nieuwe Course instantie met de opgegeven naam en locatie.
     *
     * @param courseName     De naam van de golfbaan.
     * @param courseLocation De locatie van de golfbaan.
     */
    public Course(String courseName, String courseLocation) {
        setCourseName(courseName);
        setCourseLocation(courseLocation);
    }

    /**
     * Initialiseert een nieuwe Course instantie gebaseerd op de resultset van een databasequery.
     *
     * @param rs De resultset van de databasequery.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het verwerken van de resultset.
     */
    public Course(ResultSet rs) throws SQLException {
        setCourseName(rs.getString("name"));
        setCourseLocation(rs.getString("location"));
    }

    /**
     * Geeft de eigenschap voor de naam van de golfbaan terug.
     *
     * @return De eigenschap voor de naam van de golfbaan.
     */
    public StringProperty courseNameProperty() {
        return courseName;
    }

    /**
     * Geeft de eigenschap voor de locatie van de golfbaan terug.
     *
     * @return De eigenschap voor de locatie van de golfbaan.
     */
    public StringProperty courseLocationProperty() {
        return courseLocation;
    }

    /**
     * Geeft de naam van de golfbaan terug.
     *
     * @return De naam van de golfbaan.
     */
    public String getCourseName() {
        return courseName.get();
    }

    /**
     * Stelt de naam van de golfbaan in.
     *
     * @param courseName De naam van de golfbaan.
     */
    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    /**
     * Geeft de locatie van de golfbaan terug.
     *
     * @return De locatie van de golfbaan.
     */
    public String getCourseLocation() {
        return courseLocation.get();
    }

    /**
     * Stelt de locatie van de golfbaan in.
     *
     * @param courseLocation De locatie van de golfbaan.
     */
    public void setCourseLocation(String courseLocation) {
        this.courseLocation.set(courseLocation);
    }
}
