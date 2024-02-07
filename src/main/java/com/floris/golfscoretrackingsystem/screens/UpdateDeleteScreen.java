package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.utils.UpdateDeleteScreenUtils;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
    private final FlowPane form;
    private final int currentRound;
    public Golfer currentGolfer;
    public User currentUser;

    public UpdateDeleteScreen(int id, HomeScreen homeScreen, Golfer golfer, User user) {
        this.currentGolfer = golfer;
        this.currentRound = id;
        this.currentUser = user;
        this.homeScreen = homeScreen;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.form = new FlowPane();
        try {
            form.getChildren().addAll(getUpdateForm(currentRound), getUpdateButton());
            form.getStyleClass().add("form");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(form, 1, 1);

        form.setPadding(new Insets(10, 10, 10, 10));
        form.setPrefSize(Applicaction.applicationSize[0] - 300, Applicaction.applicationSize[1] - 40);
        form.setOrientation(Orientation.VERTICAL);
        form.setAlignment(Pos.CENTER);
        form.setVgap(10);

        root.getChildren().add(gridPane);
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/updatedeletescreen.css")).toString());

        Applicaction.scenes.put("UpdateDelete", scene);
    }

    private FlowPane getNavBar() {
        return UpdateDeleteScreenUtils.getNavBar("Rounds", homeScreen, currentGolfer, currentUser);
    }

    private FlowPane getHeader() {
        return Utils.getHeader(currentGolfer);
    }

    private FlowPane getLogo() {
        return Utils.getLogo();
    }


    private FlowPane getUpdateForm(int id) throws SQLException {
        FlowPane form = new FlowPane();
        form.setPadding(new Insets(10, 10, 10, 10));
        form.getStyleClass().add("update-delete-screen");
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
                        Controller.createDateFieldBox("Date: ", datePlayed),
                        Controller.createFieldBox("Course: ", name),
                        Controller.createFieldBox("Location: ", location),
                        Controller.createFieldBox("Strokes: ", strokes),
                        Controller.createFieldBox("Notes: ", notes),
                        Controller.createFieldBox("Condition: ", condition),
                        Controller.createFieldBox("Temperature: ", temperature),
                        Controller.createFieldBox("Wind Speed: ", windSpeed)
                );
            }
        } catch (SQLException e) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }

        return form;
    }

    private Button getUpdateButton() {
        Button updateButton = new Button("Update");
        updateButton.getStyleClass().add("button");

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
            alert.setContentText("Are you sure you want to update this round?" + "\n" + "Date: " + newDate + "\n" + " Course: " + newName + "\n" + " Location: " + newLocation + "\n" + "Strokes: " + newStrokes + "\n" + "Notes: " + newNotes + "\n" + "Temperature: " + newTemperature + "\n" + "Wind Speed: " + newWindSpeed);

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    Applicaction.connection.updateQuery(updateQuery);
                    homeScreen.reload();
                    Applicaction.mainStage.setScene(homeScreen.getScene());
                } catch (SQLException exception) {
                    Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
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


    public Scene getScene() {
        return scene;
    }
}
