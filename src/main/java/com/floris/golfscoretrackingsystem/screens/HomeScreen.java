package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static com.floris.golfscoretrackingsystem.Applicaction.scenes;

public class HomeScreen {
    private final Scene scene;
    private final TilePane roundsPane;
    private final int fullWidth = Applicaction.applicationSize[0];
    private final int fullHeight = Applicaction.applicationSize[1];
    public User currentUser;
    public Golfer currentGolfer;

    public HomeScreen(User user, Golfer golfer) {
        this.currentGolfer = golfer;
        this.currentUser = user;
        this.roundsPane = new TilePane();
        roundsPane.setHgap(10);
        roundsPane.setVgap(10);
        roundsPane.setPadding(new Insets(30, 0, 0, 0));

        Pane root = new Pane();


        root.getChildren().add(gridPane);
        getRounds();
        scene = new Scene(root, fullWidth, fullHeight);
        Applicaction.scenes.put("home", scene);
    }

    private void getRounds() {
        System.out.println("Current User: " + currentUser);
        try {
            if (currentUser != null) {
                String query = "SELECT DISTINCT * FROM Round r JOIN WeatherCondition wc ON r.conditionID = wc.id JOIN Course c ON r.courseID = c.id JOIN Score s ON r.scoreID = s.id JOIN Golfer g ON r.golferID = g.id WHERE r.golferID = " + currentUser.getGolferId(currentUser) + " ORDER BY r.id ASC;";
                System.out.println("SQL Query: " + query);

                ResultSet rounds = Applicaction.connection.query(query);

                ResultSetMetaData metaData = rounds.getMetaData();
                int columnCount = metaData.getColumnCount();
                System.out.println("ResultSet Metadata - Column Count: " + columnCount);

                while (rounds.next()) {
                    System.out.println("Generating roundItem...");
                    Course c = new Course(rounds);
                    Golfer g = new Golfer(rounds);
                    Score s = new Score(rounds);
                    WheatherCondition wc = new WheatherCondition(rounds);
                    Round r = new Round(rounds, wc, c, g, s);

                    Node roundItem = generateRoundItem(r, c, g, s, wc);
                    roundItem.prefWidth(200);
                    roundItem.prefHeight(200);
                    roundItem.setOnMouseClicked(event -> {
                        try {
                            int roundId = Integer.parseInt(rounds.getString("id"));
                            showUpdateScreen(roundId);
                        } catch (NumberFormatException | SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Unknown ID", "Cannot find id for this round.");
                            e.printStackTrace();
                        }
                    });

                    if (!roundsPane.getChildren().contains(roundItem)) {
                        System.out.println("Adding roundItem to roundsPane");
                        roundsPane.getChildren().add(roundItem);
                    }
                }
            }
        } catch (SQLException e) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "SQL Exception", e.getMessage()));
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    private void showUpdateScreen(int id) {
        UpdateScreen updateScreen = new UpdateScreen(id);
        scenes.put("Update", updateScreen.getScene());
        Applicaction.mainStage.setScene(scenes.get("Update"));
    }

    private Node generateRoundItem(Round r, Course c, Golfer g, Score s, WheatherCondition wc) {
        VBox roundItem = new VBox();

        Text date = new Text("Date: " + r.getDatePlayed());
        Text golfer = new Text("Golfer: " + g.getFirstName() + " " + g.getLastName());
        Text course = new Text("Course: " + c.getCourseName());
        Text score = new Text("Strokes: " + s.getStrokes());
        Text weather = new Text("Weather: " + wc.getConditionName() + " " + wc.getTemperature() + "°" + " " + wc.getWindSpeed() + "mph");

        roundItem.getChildren().addAll(date, golfer, course, score, weather);

        System.out.println("Generated roundItem for Round ID: " + r.getId());

        return roundItem;
    }
}
