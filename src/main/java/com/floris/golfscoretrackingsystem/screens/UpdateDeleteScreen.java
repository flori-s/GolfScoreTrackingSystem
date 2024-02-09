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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Het UpdateDeleteScreen klasse biedt functionaliteit voor het bijwerken en verwijderen van rondes.
 */
public class UpdateDeleteScreen {

    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane form;
    private final int currentRound;
    public Golfer currentGolfer;
    public User currentUser;

    /**
     * Constructor voor UpdateDeleteScreen.
     * @param id Het ID van de huidige ronde.
     * @param homeScreen Het startscherm van de applicatie.
     * @param golfer De huidige golfer.
     * @param user De huidige gebruiker.
     */
    public UpdateDeleteScreen(int id, HomeScreen homeScreen, Golfer golfer, User user) {
        this.currentGolfer = golfer;
        this.currentRound = id;
        this.currentUser = user;
        this.homeScreen = homeScreen;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(getUpdateButton(), getDeleteButton());

        this.form = new FlowPane();
        try {
            form.getChildren().addAll(getUpdateForm(), buttons);
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

    /**
     * Geeft de navigatiebalk van het UpdateDelete-scherm terug.
     * @return De FlowPane met de navigatiebalk.
     */
    private FlowPane getNavBar() {
        return UpdateDeleteScreenUtils.getNavBar("Rounds", homeScreen, currentGolfer, currentUser);
    }

    /**
     * Geeft de header van het UpdateDelete-scherm terug.
     * @return De FlowPane met de header.
     */
    private FlowPane getHeader() {
        return Utils.getHeader(currentGolfer);
    }

    /**
     * Geeft het logo van het UpdateDelete-scherm terug.
     * @return De FlowPane met het logo.
     */
    private FlowPane getLogo() {
        return Utils.getLogo();
    }


    /**
     * Geeft het formulier voor het bijwerken van de ronde terug.
     * @return De FlowPane met het updateformulier.
     * @throws SQLException Als er een SQL-fout optreedt tijdens het ophalen van de gegevens voor de ronde.
     */
    private FlowPane getUpdateForm() throws SQLException {
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
                "WHERE r.id = ?\n" +
                "ORDER BY r.id ASC;";
        try {
            PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(query);
            preparedStatement.setInt(1, currentRound);
            ResultSet rounds = preparedStatement.executeQuery();

            while (rounds.next()) {
                String datePlayed = rounds.getString("dateplayed");
                String name = rounds.getString("name");
                String location = rounds.getString("location");
                String strokes = rounds.getString("strokes");
                String notes = rounds.getString("notes");
                String condition = rounds.getString("condition");
                String temperature = rounds.getString("temperature");
                String windSpeed = rounds.getString("windspeed");

                form.getChildren().addAll(
                        Controller.createDateFieldBox("Date: ", datePlayed),
                        Controller.createFieldBox("Course: ", name),
                        Controller.createFieldBox("Location: ", location),
                        Controller.createFieldBox("Strokes: ", strokes),
                        Controller.createFieldBox("Notes: ", notes),
                        Controller.createFieldBox("Condition: ", condition),
                        Controller.createFieldBox("Temperature °C: ", temperature),
                        Controller.createFieldBox("Wind Speed km/h: ", windSpeed)
                );
            }
        } catch (SQLException e) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }

        return form;
    }

    /**
     * Geeft de knop voor het bijwerken van de ronde terug.
     * @return De knop voor het bijwerken van de ronde.
     */
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
            String Temperature = fieldValues.get("Temperature °C: ");
            String WindSpeed = fieldValues.get("Wind Speed km/h: ");

            String updateQuery = "UPDATE Round r\n" +
                    "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                    "JOIN Course c ON r.courseID = c.id\n" +
                    "JOIN Score s ON r.scoreID = s.id\n" +
                    "JOIN Golfer g ON r.golferID = g.id\n" +
                    "SET \n" +
                    "    r.dateplayed = ?,\n" +
                    "    c.name = ?,\n" +
                    "    c.location = ?,\n" +
                    "    s.strokes = ?,\n" +
                    "    s.notes = ?,\n" +
                    "    wc.condition = ?,\n" +
                    "    wc.temperature = ?,\n" +
                    "    wc.windspeed = ?\n" +
                    "WHERE r.id = ?;";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update this round?" + "\n" + "Date: " + newDate + "\n" + " Course: " + newName + "\n" + " Location: " + newLocation + "\n" + "Strokes: " + newStrokes + "\n" + "Notes: " + newNotes + "\n" + "Temperature: " + Temperature + "\n" + "Wind Speed: " + WindSpeed);

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, newDate);
                    preparedStatement.setString(2, newName);
                    preparedStatement.setString(3, newLocation);
                    preparedStatement.setString(4, newStrokes);
                    preparedStatement.setString(5, newNotes);
                    preparedStatement.setString(6, newCondition);
                    preparedStatement.setString(7, Temperature);
                    preparedStatement.setString(8, WindSpeed);
                    preparedStatement.setInt(9, currentRound);

                    preparedStatement.executeUpdate();
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

    /**
     * Geeft de knop voor het verwijderen van de ronde terug.
     * @return De knop voor het verwijderen van de ronde.
     */
    private Button getDeleteButton() {
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("button");

        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Round");
            alert.setContentText("Are you sure you want to delete this round?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    String deleteQuery = "DELETE FROM Round WHERE id = ?";
                    PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(deleteQuery);
                    preparedStatement.setInt(1, currentRound);
                    preparedStatement.executeUpdate();
                    homeScreen.reload();
                    Applicaction.mainStage.setScene(homeScreen.getScene());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
                }
            }
        });

        return deleteButton;
    }

    /**
     * Haalt de waarden op uit de formuliervelden.
     * @return Een Map met de waarden van de formuliervelden.
     */
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

    /**
     * Geeft het Scene-object van het UpdateDelete-scherm terug.
     * @return Het Scene-object van het UpdateDelete-scherm.
     */
    public Scene getScene() {
        return scene;
    }
}
