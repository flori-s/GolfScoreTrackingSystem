package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class HomeScreen {
    private final Scene scene;
    private final TilePane roundsPane;
    private final GridPane gridPane;
    private final Pane root;
    private FlowPane header;
    private FlowPane navBar;
    private final int fullWidth = Applicaction.applicationSize[0];
    private final int fullHeight = Applicaction.applicationSize[1];
    private double navItemWidth = 300;
    private double navItemHeight = 40;
    public User currentUser;
    public Golfer currentGolfer;

    public HomeScreen(User user, Golfer golfer) {
        this.currentGolfer = golfer;
        this.currentUser = user;

        this.root = new Pane();

        this.gridPane = new GridPane();

        this.roundsPane = new TilePane();

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(roundsPane, 1, 1);

        roundsPane.setMaxSize(fullWidth-navBar.getWidth(), fullHeight - header.getHeight());
        roundsPane.setStyle("-fx-background-color: #FFFFFF;");
        roundsPane.setPrefColumns(4);
        roundsPane.setPrefRows(3);
        roundsPane.setHgap(60);
        roundsPane.setVgap(60);
        roundsPane.setPadding(new Insets(30, 10, 10, 60));

        root.getChildren().add(gridPane);

        long startTime = System.currentTimeMillis();
        Platform.runLater(() -> {
            getRounds();
            long endTime = System.currentTimeMillis();
            long loadTimeMillis = endTime - startTime;
            double loadTimeSeconds = loadTimeMillis / 1000.0;
            System.out.println("Rounds load time: " + loadTimeSeconds + " seconds");
        });

        scene = new Scene(root, fullWidth, fullHeight);
        Applicaction.scenes.put("Home", scene);
    }

    private Pane getNavBar() {
        this.navBar = new FlowPane();
        navBar.setId("navbar");
        navBar.setOrientation(Orientation.HORIZONTAL);
        navBar.setStyle("-fx-background-color: #16A34A;");
        navBar.setPrefSize(50, fullHeight - 40);
        navBar.setPadding(new Insets(30, 0, 0, 0));
        navBar.getChildren().addAll(
                generateNavItem("Overview", true),
                generateNavItem("Rounds", false),
                generateNavItem("Scores", false),
                generateNavItem("Golfclubs", false),
                generateNavItem("Add", false));

        return navBar;
    }

    private FlowPane generateNavItem(String title, boolean active) {
        FlowPane navItem = new FlowPane();
        navItem.setStyle(active ? "-fx-background-color: #166534;" : "");
        navItem.setPadding(new Insets(0, 0, 0, 20));
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPrefSize(navItemWidth, navItemHeight);
        navItem.setHgap(40);

        Text navItemText = new Text(title);
        navItemText.setStyle("-fx-font-size: 20px; -fx-fill: white;");
        navItem.getChildren().add(navItemText);

        if (active) {
            ImageView tee = new ImageView();
            tee.setFitWidth(50);
            tee.setPreserveRatio(true);
            tee.setImage(new Image(Objects.requireNonNull(Applicaction.class.getResource("images/pointer.png")).toString()));
            navItem.getChildren().add(tee);
        }


        navItem.setOnMouseClicked(e -> {
            if (title.equals("Overview")) {
                this.reload();
            }
            if (title.equals("Rounds")) {

            }
            if (title.equals("Scores")) {

            }
            if (title.equals("Golfclubs")) {

            }
            if (title.equals("Add")) {

            }
        });

        return navItem;
    }


    private FlowPane getHeader() {
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

    private FlowPane getLogo() {
        FlowPane logo = new FlowPane();
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

    private void getRounds() {
        try {
            if (currentUser != null) {
                String query = "SELECT DISTINCT *, r.id AS round_id\n" +
                        "FROM Round r\n" +
                        "JOIN WeatherCondition wc ON r.conditionID = wc.id\n" +
                        "JOIN Course c ON r.courseID = c.id\n" +
                        "JOIN Score s ON r.scoreID = s.id\n" +
                        "JOIN Golfer g ON r.golferID = g.id\n" +
                        "WHERE r.golferID = " + currentUser.getGolferId(currentUser) + "\n" +
                        "ORDER BY r.id ASC;";

                ResultSet rounds = Applicaction.connection.query(query);

                while (rounds.next()) {
                    int roundsId = rounds.getInt("round_id");
                    Course c = new Course(rounds);
                    Golfer g = new Golfer(rounds);
                    Score s = new Score(rounds);
                    WheatherCondition wc = new WheatherCondition(rounds);
                    Round r = new Round(rounds, wc, c, g, s);

                    Node roundItem = generateRoundItem(r, c, g, s, wc);
                    roundItem.prefWidth(200);
                    roundItem.prefHeight(200);
                    roundItem.setOnMouseClicked(event -> {
                        showUpdateScreen(roundsId);
                    });

                    if (!roundsPane.getChildren().contains(roundItem)) {
                        roundsPane.getChildren().add(roundItem);
                    }
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "SQL Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Node generateRoundItem(Round r, Course c, Golfer g, Score s, WheatherCondition wc) {
        VBox roundItem = new VBox();
        roundItem.setSpacing(10);
        roundItem.setPadding(new Insets(10));
        roundItem.setAlignment(Pos.CENTER);
        roundItem.setStyle("-fx-background-color: #16A34A; -fx-background-radius: 10px;");

        Text date = new Text("Date: " + r.getDatePlayed());
        date.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text golfer = new Text("Golfer: " + g.getFirstName() + " " + g.getLastName());
        golfer.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text course = new Text("Course: " + c.getCourseName());
        course.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text score = new Text("Strokes: " + s.getStrokes());
        score.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");
        Text weather = new Text("Weather: " + wc.getConditionName() + " " + wc.getTemperature() + "°" + " " + wc.getWindSpeed() + "mph");
        weather.setStyle("-fx-font-size: 18px; -fx-fill: #ffffff;");

        roundItem.getChildren().addAll(date, golfer, course, score, weather);


        return roundItem;
    }

    public void reload() {
        roundsPane.getChildren().clear();
        long startTime = System.currentTimeMillis();
        run(() -> {
            getRounds();
            long endTime = System.currentTimeMillis();
            long loadTimeMillis = endTime - startTime;
            double loadTimeSeconds = loadTimeMillis / 1000.0;
            System.out.println("Rounds load time: " + loadTimeSeconds + " seconds");
        });
    }

    public static void run(Runnable treatment) {
        if (treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");

        if (Platform.isFxApplicationThread()) treatment.run();
        else Platform.runLater(treatment);
    }


    public Scene getScene() {
        return scene;
    }

    private void showUpdateScreen(int id) {
        UpdateDeleteScreen updateScreen = new UpdateDeleteScreen(id, this);
        Applicaction.mainStage.setScene(updateScreen.getScene());
    }
}
