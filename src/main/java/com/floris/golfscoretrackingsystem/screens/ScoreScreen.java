package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.Score;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.utils.ScoreScreenUtils;
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
 * Het ScoreScreen klasse biedt functionaliteit om de scores van een gebruiker weer te geven.
 */
public class ScoreScreen {
    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane tableView;
    public User currentUser;
    public Golfer currentGolfer;

    /**
     * Constructor voor ScoreScreen.
     * @param homeScreen Het startscherm van de applicatie.
     * @param user De huidige gebruiker.
     * @param golfer De bijbehorende golferinformatie.
     */
    public ScoreScreen(HomeScreen homeScreen, User user, Golfer golfer) {
        this.homeScreen = homeScreen;
        this.currentGolfer = golfer;
        this.currentUser = user;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.tableView = new FlowPane();
        try {
            tableView.getChildren().addAll(getScores());
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
        Applicaction.scenes.put("Scores", scene);
    }

    /**
     * Geeft de navigatiebalk van het scorescherm terug.
     * @return FlowPane met de navigatiebalk.
     */
    public FlowPane getNavBar() {
        FlowPane navBar = ScoreScreenUtils.getNavBar("Scores", this, tableView, homeScreen, currentGolfer, currentUser);
        return navBar;
    }

    /**
     * Geeft de header van het scorescherm terug.
     * @return FlowPane met de header.
     */
    private FlowPane getHeader() {
        FlowPane header = Utils.getHeader(currentGolfer);
        return header;
    }

    /**
     * Geeft het logo van het scorescherm terug.
     * @return FlowPane met het logo.
     */
    private FlowPane getLogo() {
        FlowPane logo = Utils.getLogo();
        return logo;
    }

    /**
     * Haalt de scores op vanuit de database en geeft deze weer in een TableView.
     * @return FlowPane met de TableView van scores.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het ophalen van de scores.
     */
    private FlowPane getScores() throws SQLException {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        TableView<Score> tv = new TableView<>();

        String query = "SELECT Score.strokes, Score.notes\n" +
                "FROM Round\n" +
                "JOIN Score ON Round.scoreID = Score.id\n" +
                "JOIN Golfer ON Round.golferID = Golfer.id\n" +
                "WHERE Golfer.username = ?;";

        PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(query);
        preparedStatement.setString(1, currentUser.getUsername());

        ResultSet scores = preparedStatement.executeQuery();

        ArrayList<Score> scoreList = new ArrayList<>();

        while (scores.next()) {
            Score s = new Score(scores);
            scoreList.add(s);
        }

        if (!scoreList.isEmpty()) {
            TableColumn<Score, Integer> col0 = new TableColumn<>("Strokes");
            col0.setCellValueFactory(new PropertyValueFactory<>("strokes"));

            TableColumn<Score, String> col1 = new TableColumn<>("Notes");
            col1.setCellValueFactory(new PropertyValueFactory<>("notes"));

            tv.getColumns().addAll(col0, col1);

            tv.getItems().addAll(scoreList);

            flowPane.getChildren().add(tv);
            return flowPane;
        }
        return null;
    }

    /**
     * Vernieuwt de inhoud van het TableView met scores.
     * @param tableView De FlowPane die het TableView bevat.
     */
    public void reload(FlowPane tableView) {
        tableView.getChildren().clear();
        Controller.run(() -> {
            try {
                tableView.getChildren().addAll(getScores());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Geeft het Scene-object van het scorescherm terug.
     * @return Het Scene-object van het scorescherm.
     */
    public Scene getScene() {
        return scene;
    }
}
