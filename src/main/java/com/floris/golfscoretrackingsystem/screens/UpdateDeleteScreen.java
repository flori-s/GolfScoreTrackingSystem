package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UpdateDeleteScreen {

    private final Scene scene;
    private final HomeScreen homeScreen;
    private final Pane root;
    private final FlowPane form;
    private final int fullWidth = Applicaction.applicationSize[0];
    private final int fullHeight = Applicaction.applicationSize[1];
    int currentRound;

    public UpdateDeleteScreen(int id, HomeScreen homeScreen) {
        this.currentRound = id;
        this.homeScreen = homeScreen;
        this.root = new Pane();

        Image backgroundImage = new Image(Objects.requireNonNull(Applicaction.class.getResource("images/updateDeletebackground.jpeg")).toString());

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background backgroundWithImage = new Background(background);

        this.form = new FlowPane();
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setPrefSize(fullWidth, fullHeight);
        form.setOrientation(Orientation.VERTICAL);
        form.setAlignment(Pos.CENTER);
        form.setBackground(backgroundWithImage);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            homeScreen.reload();
            Applicaction.mainStage.setScene(homeScreen.getScene());
        });

        try {
            form.getChildren().addAll(getUpdateForm(currentRound),updateButton);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        root.getChildren().add(form);
        scene = new Scene(root);
        Applicaction.scenes.put("UpdateDelete", scene);
    }

    private FlowPane getUpdateForm(int id) throws SQLException {
        FlowPane form = new FlowPane();
        form.setPrefHeight(215);
        form.setOrientation(Orientation.VERTICAL);

        String query = "SELECT DISTINCT *, r.id AS round_id\n" +
                "FROM Round r\n" +
                "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                "JOIN Course c ON r.courseID = c.id\n" +
                "JOIN Score s ON r.scoreID = s.id\n" +
                "JOIN Golfer g ON r.golferID = g.id\n" +
                "WHERE r.id = " + id + "\n" +
                "ORDER BY r.id ASC;";

        ResultSet rounds = Applicaction.connection.query(query);
        while (rounds.next()) {
            String datePlayed = rounds.getString("dateplayed");
            String name = rounds.getString("name");
            String location = rounds.getString("location");
            String strokes = rounds.getString("strokes");
            String notes = rounds.getString("notes");
            String condition = rounds.getString("condition");
            String temperature = rounds.getString("temperature") + " °C";
            String windSpeed = rounds.getString("windspeed") + " km/h";

            form.getChildren().addAll(
                    createFieldBox("Date: ", datePlayed),
                    createFieldBox("Course: ", name),
                    createFieldBox("Location: ", location),
                    createFieldBox("Strokes: ", strokes),
                    createFieldBox("Notes: ", notes),
                    createFieldBox("Condition: ", condition),
                    createFieldBox("Temperature: ", temperature),
                    createFieldBox("Wind Speed: ", windSpeed)
            );
        }
        return form;
    }

    private HBox createFieldBox(String labelText, String textFieldValue) {
        Label label = new Label(labelText);
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);

        TextField textField = new TextField(textFieldValue);
        textField.setAlignment(Pos.BASELINE_LEFT);

        HBox hbox = new HBox(5, label, textField);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public Scene getScene() {
        return scene;
    }
}

