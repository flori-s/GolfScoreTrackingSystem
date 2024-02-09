package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.*;
import com.floris.golfscoretrackingsystem.utils.HomeScreenUtils;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Schermklasse voor de startpagina van het Golf Score Tracking System.
 */
public class HomeScreen {
    private final Scene scene;
    public final TilePane roundsPane;

    public User currentUser;
    public Golfer currentGolfer;

    /**
     * Initialiseert het startscherm met de opgegeven gebruiker en golfer.
     *
     * @param user   De huidige gebruiker.
     * @param golfer De huidige golfer.
     */
    public HomeScreen(User user, Golfer golfer) {
        this.currentGolfer = golfer;
        this.currentUser = user;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();
        this.roundsPane = new TilePane();

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(roundsPane, 1, 1);

        roundsPane.setMaxSize(Applicaction.applicationSize[0] - 300, Applicaction.applicationSize[1] - 40);
        roundsPane.setPrefColumns(4);
        roundsPane.setPrefRows(3);
        roundsPane.getStyleClass().add("roundsPane");

        root.getChildren().add(gridPane);

        getRounds();

        scene = new Scene(root, Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/homescreen.css")).toString());
        Applicaction.scenes.put("Home", scene);
    }

    private FlowPane getLogo() {
        return Utils.getLogo();
    }

    private FlowPane getHeader() {
        return Utils.getHeader(currentGolfer);
    }

    private FlowPane getNavBar() {
        return HomeScreenUtils.getNavBar("Rounds", this, currentGolfer, currentUser);
    }

    /**
     * Haalt de recente rondes op vanuit de database en toont ze op het scherm.
     */
    public void getRounds() {
        String query = "SELECT DISTINCT *, r.id AS round_id\n" +
                "FROM Round r\n" +
                "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                "JOIN Course c ON r.courseID = c.id\n" +
                "JOIN Score s ON r.scoreID = s.id\n" +
                "JOIN Golfer g ON r.golferID = g.id\n" +
                "WHERE r.golferID = ?\n" +
                "ORDER BY r.dateplayed DESC\n" +
                "LIMIT 12;";

        try {
            PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(query);
            preparedStatement.setInt(1, currentUser.getGolferId(currentUser));

            ResultSet rounds = preparedStatement.executeQuery();

            while (rounds.next()) {
                int roundsId = rounds.getInt("round_id");
                Course c = new Course(rounds);
                Golfer g = new Golfer(rounds);
                Score s = new Score(rounds);
                WeatherCondition wc = new WeatherCondition(rounds);
                Round r = new Round(rounds, wc, c, g, s);

                Node roundItem = HomeScreenUtils.generateRoundItem(r, c, g, s, wc);
                roundItem.prefWidth(200);
                roundItem.prefHeight(200);
                roundItem.setOnMouseClicked(event -> {
                    showUpdateScreen(roundsId);
                });

                if (!roundsPane.getChildren().contains(roundItem)) {
                    roundsPane.getChildren().add(roundItem);
                }
            }
        } catch (SQLException e) {
            Controller.showAlert(Alert.AlertType.ERROR, "Fout", "Databasefout", "Fout bij het openen van de database");
            e.printStackTrace();
        }
    }

    /**
     * Herlaadt de inhoud van het startscherm.
     */
    public void reload() {
        roundsPane.getChildren().clear();
        Controller.run(() -> {
            getRounds();
        });
    }

    /**
     * Geeft het JavaFX-sceneobject van het startscherm terug.
     *
     * @return Het sceneobject van het startscherm.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Toont het scherm voor het bijwerken van de opgegeven ronde.
     *
     * @param id De ID van de ronde die moet worden bijgewerkt.
     */
    private void showUpdateScreen(int id) {
        UpdateDeleteScreen updateScreen = new UpdateDeleteScreen(id, this, currentGolfer, currentUser);
        Applicaction.mainStage.setScene(updateScreen.getScene());
    }
}
