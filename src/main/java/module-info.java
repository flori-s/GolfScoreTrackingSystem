module com.floris.golfscoretrackingsystem {
    requires javafx.controls;
    requires java.sql;


    opens com.floris.golfscoretrackingsystem to javafx.fxml;
    exports com.floris.golfscoretrackingsystem;
}