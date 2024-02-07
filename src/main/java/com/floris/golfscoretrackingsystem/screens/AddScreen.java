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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class AddScreen {
    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane container;
    public User currentUser;
    public Golfer currentGolfer;

    public AddScreen(HomeScreen homeScreen, User user, Golfer golfer) {
        this.currentGolfer = golfer;
        this.currentUser = user;
        this.homeScreen = homeScreen;

        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.container = new FlowPane();
        container.getStyleClass().add("container");
        container.getChildren().add(getAddForm());

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

    public FlowPane getNavBar() {
        FlowPane navBar = AddScreenUtils.getNavBar("Add", this, homeScreen, currentGolfer, currentUser);
        return navBar;
    }

    private FlowPane getHeader() {
        FlowPane header = Utils.getHeader(currentGolfer);
        return header;
    }

    private FlowPane getLogo() {
        FlowPane logo = Utils.getLogo();
        return logo;
    }

    private FlowPane getAddForm() {
        FlowPane form = new FlowPane();
        form.getStyleClass().add("add-form");
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setPrefHeight(320);
        form.setOrientation(Orientation.VERTICAL);

        form.getChildren().addAll(
                Controller.createDateFieldBox("Date: ", "2023-12-31"),
                Controller.createFieldBox("Course: ", "Name"),
                Controller.createFieldBox("Location: ", "Location"),
                Controller.createFieldBox("Strokes: ", "Strokes"),
                Controller.createFieldBox("Notes: ", "Notes"),
                Controller.createFieldBox("Condition: ", "Condition (Wind, Rain, etc)"),
                Controller.createFieldBox("Temperature: ", "temperature in °C"),
                Controller.createFieldBox("Wind Speed: ", "Wind Speed in KM/H")
        );

        return form;
    }

    public void reload() {
        container.getChildren().clear();
        Controller.run(() -> {
            container.getChildren().add(getAddForm());
        });
    }

    public Scene getScene() {
        return scene;
    }

}
