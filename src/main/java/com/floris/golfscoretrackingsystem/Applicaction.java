package com.floris.golfscoretrackingsystem;

import com.floris.golfscoretrackingsystem.screens.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Applicaction extends Application {
    public static Stage mainStage;
    public static DatabaseConn connection;
    public static HashMap<String, Scene> scenes = new HashMap<>();
    public static int[] applicationSize = {3024, 1964};
    @Override
    public void start(Stage stage) throws IOException {
        connection = new DatabaseConn("adainforma.tk", "golfscoretrackingsystem", "57Yg$6cq7", "bp2_golfscoretrackingsystem");

        scenes.put("login", new LoginScreen().getScene());

        mainStage = stage;

        mainStage.setWidth(applicationSize[0]);
        mainStage.setHeight(applicationSize[1]);
        mainStage.setTitle("Golf Score Tracking System");
        mainStage.setResizable(false);

        mainStage.setScene(scenes.get("login"));
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}