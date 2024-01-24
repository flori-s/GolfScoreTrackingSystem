package com.floris.golfscoretrackingsystem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {

    private String courseName;
    private String courseLocation;

    public Course(String courseName, String courseLocation) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
    }

    public Course(ResultSet rs) throws SQLException {
        this.courseName = rs.getString("name");
        this.courseLocation = rs.getString("location");
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }


}
