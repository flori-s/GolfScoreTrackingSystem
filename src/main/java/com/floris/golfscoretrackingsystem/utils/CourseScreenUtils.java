package com.floris.golfscoretrackingsystem.utils;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.screens.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class CourseScreenUtils {
    private static FlowPane navBar;
    private static FlowPane navItem;

    public static FlowPane getNavBar(String name, CourseScreen courseScreen, HomeScreen homeScreen, FlowPane tableview, Golfer currentGolfer, User currentUser) {
        navBar = new FlowPane();
        navBar.getStyleClass().add("navbar");
        navBar.setPrefSize(50, Applicaction.applicationSize[1] - 40);
        navBar.setPadding(new Insets(30, 0, 0, 0));

        navBar.getChildren().addAll(
                generateNavItem("Rounds", isActive("Rounds", name), courseScreen, homeScreen, tableview, currentGolfer, currentUser),
                generateNavItem("Courses", isActive("Courses", name), courseScreen, homeScreen, tableview, currentGolfer, currentUser),
                generateNavItem("Scores", isActive("Scores", name), courseScreen, homeScreen, tableview, currentGolfer, currentUser),
                generateNavItem("Golfclubs", isActive("Golfclubs", name), courseScreen, homeScreen, tableview, currentGolfer, currentUser),
                generateNavItem("Add", isActive("Add", name), courseScreen, homeScreen, tableview, currentGolfer, currentUser)
        );
        return navBar;
    }

    public static boolean isActive(String title, String name) {
        return title.equals(name);
    }

    public static FlowPane generateNavItem(String title, boolean active, CourseScreen courseScreen, HomeScreen homeScreen, FlowPane tableview, Golfer currentGolfer, User currentUser) {
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

        navItem.setOnMouseClicked(e -> handleNavItemClick(title, courseScreen, homeScreen, tableview, currentGolfer, currentUser));

        return navItem;
    }

    private static void handleNavItemClick(String title, CourseScreen courseScreen, HomeScreen homeScreen, FlowPane tableview, Golfer currentGolfer, User currentUser) {
        switch (title) {
            case "Rounds":
                homeScreen.reload();
                Applicaction.mainStage.setScene(homeScreen.getScene());
                break;
            case "Courses":
                courseScreen.reload(tableview);
                break;
            case "Scores":
                Applicaction.mainStage.setScene(new ScoreScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            case "Golfclubs":
                Applicaction.mainStage.setScene(new GolfClubScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            case "Add":
                Applicaction.mainStage.setScene(new AddScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            default:
                break;
        }
    }

}
