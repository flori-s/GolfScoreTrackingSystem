package com.floris.golfscoretrackingsystem.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {

    private final StringProperty courseName = new SimpleStringProperty();
    private final StringProperty courseLocation = new SimpleStringProperty();

    public Course(String courseName, String courseLocation) {
        setCourseName(courseName);
        setCourseLocation(courseLocation);
    }

    public Course(ResultSet rs) throws SQLException {
        setCourseName(rs.getString("name"));
        setCourseLocation(rs.getString("location"));
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public StringProperty courseLocationProperty() {
        return courseLocation;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public String getCourseLocation() {
        return courseLocation.get();
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation.set(courseLocation);
    }
}