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

/**
 * De Utils klasse bevat hulpmethoden voor het maken van header en logo elementen.
 */
public class Utils {
    private static FlowPane header;
    private static FlowPane logo;
    private static final int navItemWidth = 300;
    private static final int navItemHeight = 40;
    private static final int fullWidth = Applicaction.applicationSize[0];
    private static final int fullHeight = Applicaction.applicationSize[1];

    /**
     * Geeft de header van het scherm terug.
     * @param currentGolfer De huidige golfer.
     * @return De FlowPane met de header.
     */
    public static FlowPane getHeader(Golfer currentGolfer) {
        header = new FlowPane();
        header.setPrefSize(fullWidth - navItemWidth, navItemHeight);
        header.getStyleClass().add("header");
        header.setOrientation(Orientation.VERTICAL);
        header.setAlignment(Pos.CENTER);

        FlowPane titlePane = new FlowPane();
        titlePane.setHgap(20);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        titlePane.setPadding(new Insets(0, 0, 0, 20));
        titlePane.setPrefSize(fullWidth - navItemWidth, navItemHeight);
        titlePane.setOrientation(Orientation.HORIZONTAL);

        Text welcome = new Text("Welcome " + currentGolfer.getFirstName() + " " + currentGolfer.getLastName() + " !");

        titlePane.getChildren().add(welcome);

        header.getChildren().addAll(titlePane);

        return header;
    }

    /**
     * Geeft het logo van de applicatie terug.
     * @return De FlowPane met het logo.
     */
    public static FlowPane getLogo() {
        logo = new FlowPane();
        logo.getStyleClass().add("logo");
        logo.setAlignment(Pos.CENTER);
        logo.setPrefSize(navItemWidth, navItemHeight);

        ImageView golfer = new ImageView();
        golfer.setFitWidth(25);
        golfer.setPreserveRatio(true);
        golfer.setImage(new Image(Objects.requireNonNull(Applicaction.class.getResource("images/logo.png")).toString()));

        Text title = new Text("Golf Score Tracking System");

        logo.getChildren().addAll(golfer, title);
        return logo;
    }
}
