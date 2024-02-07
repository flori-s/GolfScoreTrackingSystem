package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.*;
import com.floris.golfscoretrackingsystem.utils.HomeScreenUtils;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class HomeScreen {
    private final Scene scene;
    public final TilePane roundsPane;
    private FlowPane navBar;
    private final int fullWidth = Applicaction.applicationSize[0];
    private final int fullHeight = Applicaction.applicationSize[1];
    private final double navItemWidth = 300;
    private final double navItemHeight = 40;
    public User currentUser;
    public Golfer currentGolfer;

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

        roundsPane.setMaxSize(fullWidth - 300, fullHeight - 40);
        roundsPane.setPrefColumns(4);
        roundsPane.setPrefRows(3);
        roundsPane.getStyleClass().add("roundsPane");

        root.getChildren().add(gridPane);

        getRounds();

        scene = new Scene(root, fullWidth, fullHeight);
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

    public void getRounds() {
        try {
            if (currentUser != null) {
                String query = "SELECT DISTINCT *, r.id AS round_id\n" +
                        "FROM Round r\n" +
                        "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                        "JOIN Course c ON r.courseID = c.id\n" +
                        "JOIN Score s ON r.scoreID = s.id\n" +
                        "JOIN Golfer g ON r.golferID = g.id\n" +
                        "WHERE r.golferID = " + currentUser.getGolferId(currentUser) + "\n" +
                        "ORDER BY r.dateplayed DESC\n" +
                        "LIMIT 12;";

                ResultSet rounds = Applicaction.connection.query(query);

                while (rounds.next()) {
                    int roundsId = rounds.getInt("round_id");
                    Course c = new Course(rounds);
                    Golfer g = new Golfer(rounds);
                    Score s = new Score(rounds);
                    WeatherCondition wc = new WeatherCondition(rounds);
                    Round r = new Round(rounds, wc, c, g, s);

                    Node roundItem = generateRoundItem(r, c, g, s, wc);
                    roundItem.prefWidth(200);
                    roundItem.prefHeight(200);
                    roundItem.setOnMouseClicked(event -> {
                        showUpdateScreen(roundsId);
                    });

                    if (!roundsPane.getChildren().contains(roundItem)) {
                        roundsPane.getChildren().add(roundItem);
                    }
                }
            }
        } catch (SQLException e) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            e.printStackTrace();
        }
    }

    public Node generateRoundItem(Round r, Course c, Golfer g, Score s, WeatherCondition wc) {
        VBox roundItem = new VBox();
        roundItem.setSpacing(10);
        roundItem.setPadding(new Insets(10));
        roundItem.setAlignment(Pos.CENTER);
        roundItem.getStyleClass().add("roundItem");

        Text date = new Text("Date: " + r.getDatePlayed());
        date.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text golfer = new Text("Golfer: " + g.getFirstName() + " " + g.getLastName());
        golfer.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text course = new Text("Course: " + c.getCourseName());
        course.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text score = new Text("Strokes: " + s.getStrokes());
        score.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text weather = new Text("Weather: " + wc.getConditionName() + " " + wc.getTemperature() + "°" + " " + wc.getWindSpeed() + "mph");
        weather.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");

        roundItem.getChildren().addAll(date, golfer, course, score, weather);

        return roundItem;
    }

    public void reload() {
        roundsPane.getChildren().clear();
        Controller.run(() -> {
            getRounds();
        });
    }

    public Scene getScene() {
        return scene;
    }

    private void showUpdateScreen(int id) {
        UpdateDeleteScreen updateScreen = new UpdateDeleteScreen(id, this, currentGolfer, currentUser);
        Applicaction.mainStage.setScene(updateScreen.getScene());
    }
}
