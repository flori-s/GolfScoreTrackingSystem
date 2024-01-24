package com.floris.golfscoretrackingsystem.screens;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class UpdateScreen {

    private final Scene scene;

    public UpdateScreen(int id) {
        Pane root = new Pane();

        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }
}
