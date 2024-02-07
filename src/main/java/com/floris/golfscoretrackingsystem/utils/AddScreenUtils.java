package com.floris.golfscoretrackingsystem.utils;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.screens.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class AddScreenUtils {
    private static FlowPane navBar;
    private static FlowPane navItem;

    public static FlowPane getNavBar(String name, AddScreen addScreen, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
        navBar = new FlowPane();
        navBar.getStyleClass().add("navbar");
        navBar.setPrefSize(50, Applicaction.applicationSize[1] - 40);
        navBar.setPadding(new Insets(30, 0, 0, 0));

        navBar.getChildren().addAll(
                generateNavItem("Rounds", isActive("Rounds", name), addScreen, homeScreen, currentGolfer, currentUser),
                generateNavItem("Courses", isActive("Courses", name), addScreen, homeScreen, currentGolfer, currentUser),
                generateNavItem("Scores", isActive("Scores", name), addScreen, homeScreen, currentGolfer, currentUser),
                generateNavItem("Golfclubs", isActive("Golfclubs", name), addScreen, homeScreen, currentGolfer, currentUser),
                generateNavItem("Add", isActive("Add", name), addScreen, homeScreen, currentGolfer, currentUser)
        );
        return navBar;
    }

    public static boolean isActive(String title, String name) {
        return title.equals(name);
    }

    public static FlowPane generateNavItem(String title, boolean active, AddScreen addscreen, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
        navItem = new FlowPane();
        navItem.setPadding(new Insets(0, 0, 0, 20));
        navItem.setHgap(60);
        navItem.setPrefSize(300, 40);
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.getStyleClass().add("navitem");

        Text navItemText = new Text(title);
        navItemText.getStyleClass().add("navitemText");
        navItem.getChildren().add(navItemText);

        if (active) {
            navItem.getStyleClass().remove("navitem");
            navItem.getStyleClass().add("active");
            ImageView tee = new ImageView();
            tee.setId("activeTee");
            tee.setFitHeight(25);
            tee.setPreserveRatio(true);
            tee.setImage(new Image(Objects.requireNonNull(Applicaction.class.getResource("images/pointer.png")).toString()));
            navItem.getChildren().add(tee);
        }

        navItem.setOnMouseClicked(e -> handleNavItemClick(title, addscreen, homeScreen, currentGolfer, currentUser));

        return navItem;
    }

    private static void handleNavItemClick(String title, AddScreen addScreen, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
        switch (title) {
            case "Rounds":
                homeScreen.reload();
                Applicaction.mainStage.setScene(homeScreen.getScene());
                break;
            case "Courses":
                Applicaction.mainStage.setScene(new CourseScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            case "Scores":
                Applicaction.mainStage.setScene(new ScoreScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            case "Golfclubs":
                Applicaction.mainStage.setScene(new GolfClubScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            case "Add":
                addScreen.reload();
                break;
            default:
                break;
        }
    }
}
