package com.floris.golfscoretrackingsystem.utils;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class Utils {
    private static FlowPane header;
    private static FlowPane logo;
    private static final int navItemWidth = 300;
    private static final int navItemHeight = 40;
    private static final int fullWidth = Applicaction.applicationSize[0];
    private static final int fullHeight = Applicaction.applicationSize[1];
    public static FlowPane getHeader(Golfer currentGolfer) {
        header = new FlowPane();
        header.setPrefSize(fullWidth - navItemWidth, navItemHeight);
        header.setStyle("-fx-background-color: #166534;");
        header.setOrientation(Orientation.VERTICAL);
        header.setAlignment(Pos.CENTER);

        FlowPane titlePane = new FlowPane();
        titlePane.setHgap(20);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        titlePane.setPadding(new Insets(0, 0, 0, 20));
        titlePane.setPrefSize(fullWidth - navItemWidth, navItemHeight);
        titlePane.setOrientation(Orientation.HORIZONTAL);
        Text welcome = new Text("Welcome " + currentGolfer.getFirstName() + " " + currentGolfer.getLastName() + " !");
        welcome.setStyle("-fx-font-size: 20px; -fx-fill: #ffffff;");

        titlePane.getChildren().add(welcome);

        header.getChildren().addAll(titlePane);

        return header;
    }

    public static FlowPane getLogo() {
        logo = new FlowPane();
        logo.setStyle("-fx-background-color: #166534;");
        logo.setAlignment(Pos.CENTER);
        logo.setPrefSize(navItemWidth, navItemHeight);

        ImageView golfer = new ImageView();
        golfer.setFitWidth(25);
        golfer.setPreserveRatio(true);
        golfer.setImage(new Image(Objects.requireNonNull(Applicaction.class.getResource("images/logo.png")).toString()));

        Text title = new Text("Golf Score Tracking System");
        title.setStyle("-fx-font-size: 20px; -fx-fill: #ffffff;");

        logo.getChildren().addAll(golfer, title);
        return logo;
    }
}
