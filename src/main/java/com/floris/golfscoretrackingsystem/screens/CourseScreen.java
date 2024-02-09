package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.classes.Course;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.utils.CourseScreenUtils;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Het scherm dat de lijst met cursussen weergeeft.
 */
public class CourseScreen {
    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane tableView;
    public User currentUser;
    public Golfer currentGolfer;

    /**
     * Initialiseert een nieuw CourseScreen.
     *
     * @param homeScreen Het startscherm van de toepassing.
     * @param user       De huidige gebruiker van de toepassing.
     * @param golfer     De huidige golfer van de toepassing.
     */
    public CourseScreen(HomeScreen homeScreen, User user, Golfer golfer) {
        this.homeScreen = homeScreen;
        this.currentGolfer = golfer;
        this.currentUser = user;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.tableView = new FlowPane();
        try {
            tableView.getChildren().addAll(getCourses());
            tableView.getStyleClass().add("tableview");
            tableView.setMaxSize(Applicaction.applicationSize[0] - 300, Applicaction.applicationSize[1] - 40);
            tableView.setAlignment(Pos.CENTER);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(tableView, 1, 1);

        root.getChildren().add(gridPane);

        scene = new Scene(root, Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/tableview.css")).toString());
        Applicaction.scenes.put("Courses", scene);
    }

    /**
     * Geeft de navigatiebalk van het scherm terug.
     *
     * @return De navigatiebalk.
     */
    public FlowPane getNavBar() {
        FlowPane navBar = CourseScreenUtils.getNavBar("Courses", this, homeScreen, tableView, currentGolfer, currentUser);
        return navBar;
    }

    /**
     * Geeft de header van het scherm terug.
     *
     * @return De header.
     */
    private FlowPane getHeader() {
        FlowPane header = Utils.getHeader(currentGolfer);
        return header;
    }

    /**
     * Geeft het logo van het scherm terug.
     *
     * @return Het logo.
     */
    private FlowPane getLogo() {
        FlowPane logo = Utils.getLogo();
        return logo;
    }

    /**
     * Haalt de lijst met cursussen op uit de database.
     *
     * @return Een FlowPane met de tabelweergave van de cursussen.
     * @throws SQLException Als er een SQL-fout optreedt bij het ophalen van de cursusgegevens.
     */
    private FlowPane getCourses() throws SQLException {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        TableView<Course> tv = new TableView<>();

        String query = "SELECT DISTINCT Course.name, Course.location\n" +
                "FROM Round\n" +
                "JOIN Course ON Round.courseID = Course.id\n" +
                "JOIN Golfer ON Round.golferID = Golfer.id\n" +
                "WHERE Golfer.username = ?;";

        PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(query);
        preparedStatement.setString(1, currentUser.getUsername());

        ResultSet courses = preparedStatement.executeQuery();

        ArrayList<Course> courseList = new ArrayList<>();

        while (courses.next()) {
            Course c = new Course(courses);
            courseList.add(c);
        }

        if (!courseList.isEmpty()) {
            TableColumn<Course, String> col0 = new TableColumn<>("Name");
            col0.setCellValueFactory(new PropertyValueFactory<>("courseName"));

            TableColumn<Course, String> col1 = new TableColumn<>("Location");
            col1.setCellValueFactory(new PropertyValueFactory<>("courseLocation"));

            tv.getColumns().addAll(col0, col1);

            tv.getItems().addAll(courseList);

            flowPane.getChildren().add(tv);
            return flowPane;
        }
        return null;
    }

    /**
     * Herlaadt het scherm.
     *
     * @param tableView De FlowPane die de TableView bevat.
     */
    public void reload(FlowPane tableView) {
        tableView.getChildren().clear();
        Controller.run(() -> {
            try {
                tableView.getChildren().addAll(getCourses());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Geeft het scherm terug.
     *
     * @return Het scherm.
     */
    public Scene getScene() {
        return scene;
    }
}
