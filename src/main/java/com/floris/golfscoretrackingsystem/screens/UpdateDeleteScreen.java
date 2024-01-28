package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
        form.setVgap(10);

        try {
            form.getChildren().addAll(getUpdateForm(currentRound), getButton(), getUpdateButton());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        root.getChildren().add(form);
        scene = new Scene(root);
        Applicaction.scenes.put("UpdateDelete", scene);
    }

    private FlowPane getUpdateForm(int id) throws SQLException {
        FlowPane form = new FlowPane();
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setStyle("-fx-background-radius: 5; -fx-background-color: #16A34A;");
        form.setPrefHeight(320);
        form.setOrientation(Orientation.VERTICAL);

        String query = "SELECT DISTINCT *\n" +
                "FROM Round r\n" +
                "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                "JOIN Course c ON r.courseID = c.id\n" +
                "JOIN Score s ON r.scoreID = s.id\n" +
                "JOIN Golfer g ON r.golferID = g.id\n" +
                "WHERE r.id = " + id + "\n" +
                "ORDER BY r.id ASC;";
        try {
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
                        createDateFieldBox("Date: ", datePlayed),
                        createFieldBox("Course: ", name),
                        createFieldBox("Location: ", location),
                        createFieldBox("Strokes: ", strokes),
                        createFieldBox("Notes: ", notes),
                        createFieldBox("Condition: ", condition),
                        createFieldBox("Temperature: ", temperature),
                        createFieldBox("Wind Speed: ", windSpeed)
                );
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }

        return form;
    }

    private HBox createFieldBox(String labelText, String textFieldValue) {
        Label label = new Label(labelText);
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setStyle("-fx-text-fill: #FFFFFF;");

        TextField textField = new TextField(textFieldValue);
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.setStyle("-fx-background-color: #16A34A; -fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 0);");

        HBox hbox = new HBox(5, label, textField);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox createDateFieldBox(String labelText, String textFieldValue) {
        Label label = new Label(labelText);
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setStyle("-fx-text-fill: #FFFFFF;");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate initialDate = LocalDate.parse(textFieldValue, dateFormatter);

        DatePicker datePicker = new DatePicker(initialDate);
        datePicker.setStyle("-fx-background-color: #16A34A; -fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 0);");

        HBox hbox = new HBox(5, label, datePicker);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private Button getButton() {
        Button updateButton = new Button("Back");
        updateButton.setStyle("-fx-background-color: #16A34A; -fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-padding: 10px 20px;");

        updateButton.setOnAction(e -> {
            homeScreen.reload();
            Applicaction.mainStage.setScene(homeScreen.getScene());
        });
        return updateButton;
    }

    private Button getUpdateButton() {
        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #16A34A; -fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-padding: 10px 20px;");

        updateButton.setOnAction(e -> {
            Map<String, String> fieldValues = getFormFieldValues();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate selectedDate = LocalDate.parse(fieldValues.get("Date: "), dateFormatter);

            DateTimeFormatter secondDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String newDate = selectedDate.format(secondDateFormatter);

            String newName = fieldValues.get("Course: ");
            String newLocation = fieldValues.get("Location: ");
            String newStrokes = fieldValues.get("Strokes: ");
            String newNotes = fieldValues.get("Notes: ");
            String newCondition = fieldValues.get("Condition: ");
            String Temperature = fieldValues.get("Temperature: ");
            String WindSpeed = fieldValues.get("Wind Speed: ");

            String newTemperature = Temperature != null ? Temperature.replaceAll("[^\\d.]", "") : null;
            String newWindSpeed = WindSpeed != null ? WindSpeed.replaceAll("[^\\d.]", "") : null;

            String updateQuery = "UPDATE Round r\n" +
                    "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                    "JOIN Course c ON r.courseID = c.id\n" +
                    "JOIN Score s ON r.scoreID = s.id\n" +
                    "JOIN Golfer g ON r.golferID = g.id\n" +
                    "SET \n" +
                    "    r.dateplayed = '" + newDate + "',\n" +
                    "    c.name = '" + newName + "',\n" +
                    "    c.location = '" + newLocation + "',\n" +
                    "    s.strokes = '" + newStrokes + "',\n" +
                    "    s.notes =  '" + newNotes + "',\n" +
                    "    wc.condition = '" + newCondition + "',\n" +
                    "    wc.temperature = '" + newTemperature + "',\n" +
                    "    wc.windspeed = '" + newWindSpeed + "'\n" +
                    "WHERE r.id = " + currentRound + ";";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update this round?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    Applicaction.connection.updateQuery(updateQuery);
                    homeScreen.reload();
                    Applicaction.mainStage.setScene(homeScreen.getScene());
                } catch (SQLException exception) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
                    exception.printStackTrace();
                }
            }
        });
        return updateButton;
    }

    private Map<String, String> getFormFieldValues() {
        Map<String, String> fieldValues = new HashMap<>();

        for (Node node : form.getChildren()) {
            if (node instanceof FlowPane) {
                FlowPane innerFlowPane = (FlowPane) node;
                for (Node innerNode : innerFlowPane.getChildren()) {
                    if (innerNode instanceof HBox) {
                        HBox box = (HBox) innerNode;
                        for (Node child : box.getChildren()) {
                            if (child instanceof TextField || child instanceof DatePicker) {
                                Label label = null;
                                for (Node sibling : box.getChildren()) {
                                    if (sibling instanceof Label) {
                                        if (box.getChildren().indexOf(sibling) < box.getChildren().indexOf(child)) {
                                            label = (Label) sibling;
                                        }
                                    }
                                }
                                if (label != null) {
                                    if (child instanceof TextField) {
                                        TextField textField = (TextField) child;
                                        fieldValues.put(label.getText(), textField.getText());
                                    } else if (child instanceof DatePicker) {
                                        DatePicker datePicker = (DatePicker) child;
                                        LocalDate selectedDate = datePicker.getValue();
                                        if (selectedDate != null) {
                                            fieldValues.put(label.getText(), selectedDate.toString());
                                        } else {
                                            fieldValues.put(label.getText(), null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        return fieldValues;
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
}

