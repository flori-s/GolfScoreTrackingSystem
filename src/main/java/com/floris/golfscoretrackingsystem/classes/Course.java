package com.floris.golfscoretrackingsystem.classes;

public class Course {

    private String courseName;
    private String courseLocation;

    public Course(String courseName, String courseLocation) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
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
