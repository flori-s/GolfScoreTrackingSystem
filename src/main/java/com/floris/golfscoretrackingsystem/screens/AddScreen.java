package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.utils.AddScreenUtils;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Het scherm waarop een nieuwe ronde aan het systeem kan worden toegevoegd.
 */
public class AddScreen {
    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane container;
    private FlowPane form;
    public User currentUser;
    public Golfer currentGolfer;

    /**
     * Initialiseert een nieuw AddScreen.
     *
     * @param homeScreen Het startscherm van de toepassing.
     * @param user       De huidige gebruiker van de toepassing.
     * @param golfer     De huidige golfer van de toepassing.
     */
    public AddScreen(HomeScreen homeScreen, User user, Golfer golfer) {
        this.currentGolfer = golfer;
        this.currentUser = user;
        this.homeScreen = homeScreen;

        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.container = new FlowPane();
        container.getStyleClass().add("container");
        container.getChildren().addAll(getAddForm(), getAddButton());

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(container, 1, 1);

        container.setPadding(new Insets(10, 10, 10, 10));
        container.setPrefSize(Applicaction.applicationSize[0] - 300, Applicaction.applicationSize[1] - 40);
        container.setOrientation(Orientation.VERTICAL);
        container.setAlignment(Pos.CENTER);
        container.setVgap(10);

        root.getChildren().add(gridPane);

        scene = new Scene(root, Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/addscreen.css")).toString());
        Applicaction.scenes.put("Addscreen", scene);
    }

    /**
     * Geeft de navigatiebalk van het scherm terug.
     *
     * @return De navigatiebalk.
     */
    public FlowPane getNavBar() {
        FlowPane navBar = AddScreenUtils.getNavBar("Add", this, homeScreen, currentGolfer, currentUser);
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
     * Genereert het formulier voor het toevoegen van een nieuwe ronde.
     *
     * @return Het formulier voor het toevoegen van een nieuwe ronde.
     */
    private FlowPane getAddForm() {
        form = new FlowPane();
        form.getStyleClass().add("add-form");
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setPrefHeight(320);
        form.setOrientation(Orientation.VERTICAL);

        form.getChildren().addAll(
                Controller.createPromptFieldBox("Course: ", "Name"),
                Controller.createPromptFieldBox("Location: ", "Location"),
                Controller.createPromptFieldBox("Strokes: ", "Strokes"),
                Controller.createPromptFieldBox("Notes: ", "Notes"),
                Controller.createPromptFieldBox("Condition: ", "Condition (Wind, Rain, etc)"),
                Controller.createPromptFieldBox("Temperature: ", "temperature in °C"),
                Controller.createPromptFieldBox("Wind Speed: ", "Wind Speed in KM/H")
        );

        return form;
    }

    /**
     * Genereert de knop voor het toevoegen van een nieuwe ronde.
     *
     * @return De knop voor het toevoegen van een nieuwe ronde.
     */
    private Button getAddButton() {
        Button addButton = new Button("Add");
        addButton.getStyleClass().add("button");

        addButton.setOnAction(e -> {
            String course = ((TextField) ((HBox) form.getChildren().get(0)).getChildren().get(1)).getText();
            String location = ((TextField) ((HBox) form.getChildren().get(1)).getChildren().get(1)).getText();
            String strokes = ((TextField) ((HBox) form.getChildren().get(2)).getChildren().get(1)).getText();
            String notes = ((TextField) ((HBox) form.getChildren().get(3)).getChildren().get(1)).getText();
            String condition = ((TextField) ((HBox) form.getChildren().get(4)).getChildren().get(1)).getText();
            String temperature = ((TextField) ((HBox) form.getChildren().get(5)).getChildren().get(1)).getText();
            String windSpeed = ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(1)).getText();

            String query1 = "INSERT INTO Course (name, location) VALUES (?, ?)";
            String query2 = "INSERT INTO Score (strokes, notes) VALUES (?, ?)";
            String query3 = "INSERT INTO WeatherCondition (`condition`, temperature, windspeed) VALUES (?, ?, ?)";
            String query4 = "INSERT INTO Round (dateplayed, conditionID, courseID, golferID, scoreID) VALUES (CURRENT_TIMESTAMP(), (SELECT id FROM WeatherCondition ORDER BY id DESC LIMIT 1), (SELECT id FROM Course ORDER BY id DESC LIMIT 1), ?, (SELECT id FROM Score ORDER BY id DESC LIMIT 1))";

            try {
                PreparedStatement stmt1 = Applicaction.connection.prepareStatement(query1);
                stmt1.setString(1, course);
                stmt1.setString(2, location);
                stmt1.executeUpdate();

                PreparedStatement stmt2 = Applicaction.connection.prepareStatement(query2);
                stmt2.setString(1, strokes);
                stmt2.setString(2, notes);
                stmt2.executeUpdate();

                PreparedStatement stmt3 = Applicaction.connection.prepareStatement(query3);
                stmt3.setString(1, condition);
                stmt3.setString(2, temperature);
                stmt3.setString(3, windSpeed);
                stmt3.executeUpdate();

                PreparedStatement stmt4 = Applicaction.connection.prepareStatement(query4);
                stmt4.setInt(1, currentUser.getGolferId(currentUser));
                stmt4.executeUpdate();

            } catch (SQLException ex) {
                // Handelt exception af
                ex.printStackTrace();
            }
        });
        return addButton;
    }

    /**
     * Herlaadt het scherm.
     */
    public void reload() {
        container.getChildren().clear();
        Controller.run(() -> {
            container.getChildren().add(getAddForm());
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
