module com.floris.golfscoretrackingsystem {
    requires javafx.controls;
    requires java.sql;


    opens com.floris.golfscoretrackingsystem.classes to javafx.base;
    exports com.floris.golfscoretrackingsystem;
    opens com.floris.golfscoretrackingsystem.utils to javafx.base;
}