package com.floris.golfscoretrackingsystem.utils;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.*;
import com.floris.golfscoretrackingsystem.screens.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class HomeScreenUtils {

    private static FlowPane navBar;
    private static FlowPane navItem;

    /**
     * Geeft een navigatiebalk voor de applicatie terug.
     *
     * @param  name         de naam van het huidige scherm
     * @param  homeScreen   het startscherm
     * @param  currentGolfer   de huidige golfer
     * @param  currentUser   de huidige gebruiker
     * @return              de navigatiebalk als een FlowPane
     */
    public static FlowPane getNavBar(String name, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
        navBar = new FlowPane();
        navBar.getStyleClass().add("navbar");
        navBar.setPrefSize(50, Applicaction.applicationSize[1] - 40);
        navBar.setPadding(new Insets(30, 0, 0, 0));

        navBar.getChildren().addAll(
                generateNavItem("Rounds", isActive("Rounds", name), homeScreen, currentGolfer, currentUser),
                generateNavItem("Courses", isActive("Courses", name), homeScreen, currentGolfer, currentUser),
                generateNavItem("Scores", isActive("Scores", name), homeScreen, currentGolfer, currentUser),
                generateNavItem("Golfclubs", isActive("Golfclubs", name), homeScreen, currentGolfer, currentUser),
                generateNavItem("Add", isActive("Add", name), homeScreen, currentGolfer, currentUser)
        );
        return navBar;
    }

    /**
     * Checkt of de title gelijk is aan de name.
     *
     * @param  title   the title to compare
     * @param  name    the name to compare
     * @return        true if the title is equal to the name, false otherwise
     */
    public static boolean isActive(String title, String name) {
        return title.equals(name);
    }

    /**
     * Genereert een navigatie-item.
     *
     * @param  title        the title of the navigation item
     * @param  active       whether the navigation item is active
     * @param  homeScreen   the HomeScreen object
     * @param  currentGolfer  the current golfer
     * @param  currentUser  the current user
     * @return              the generated navigation item
     */
    public static FlowPane generateNavItem(String title, boolean active, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
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

        navItem.setOnMouseClicked(e -> handleNavItemClick(title, homeScreen, currentGolfer, currentUser));

        return navItem;
    }

    /**
     * Geeft navigation-items een on click functie
     *
     * @param  title         the title of the navigation item clicked
     * @param  homeScreen    the HomeScreen instance for navigation
     * @param  currentGolfer the current golfer for navigation
     * @param  currentUser   the current user for navigation
     */
    private static void handleNavItemClick(String title, HomeScreen homeScreen, Golfer currentGolfer, User currentUser) {
        switch (title) {
            case "Rounds":
                homeScreen.reload();
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
                Applicaction.mainStage.setScene(new AddScreen(homeScreen, currentUser, currentGolfer).getScene());
                break;
            default:
                break;
        }
    }

    /**
     * Maakt een round item node.
     *
     * @param  r   the Round object
     * @param  c   the Course object
     * @param  g   the Golfer object
     * @param  s   the Score object
     * @param  wc  the WeatherCondition object
     * @return     the generated round item node
     */
    public static Node generateRoundItem(Round r, Course c, Golfer g, Score s, WeatherCondition wc) {
        VBox roundItem = new VBox();
        roundItem.setSpacing(10);
        roundItem.setPadding(new Insets(10));
        roundItem.setAlignment(Pos.CENTER);
        roundItem.getStyleClass().add("roundItem");

        Text date = new Text("Date: " + r.getDatePlayed());
        Text golfer = new Text("Golfer: " + g.getFirstName() + " " + g.getLastName());
        Text course = new Text("Course: " + c.getCourseName());
        Text score = new Text("Strokes: " + s.getStrokes());
        Text weather = new Text("Weather: " + wc.getConditionName() + " " + wc.getTemperature() + "°" + " " + wc.getWindSpeed() + "mph");

        roundItem.getChildren().addAll(date, golfer, course, score, weather);

        return roundItem;
    }
}


