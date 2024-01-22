package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.User;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class HomeScreen {
    private final Scene scene;
    private int fullWidth = Applicaction.applicationSize[0];
    private int fullHeight = Applicaction.applicationSize[1];
    public User currentUser;

    public HomeScreen(User user) {
        this.currentUser = user;
        Pane root = new Pane();

        scene = new Scene(root, fullWidth, fullHeight);
    }

    public Scene getScene() {
        return scene;
    }
}
