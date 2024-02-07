package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.utils.GolfclubScreenUtils;
import com.floris.golfscoretrackingsystem.classes.Golfclub;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import com.floris.golfscoretrackingsystem.utils.Utils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class GolfClubScreen {
    private final Scene scene;
    private final HomeScreen homeScreen;
    private final FlowPane tableView;
    public User currentUser;

    public Golfer currentGolfer;

    public GolfClubScreen(HomeScreen homeScreen, User user, Golfer golfer) {
        this.homeScreen = homeScreen;
        this.currentGolfer = golfer;
        this.currentUser = user;
        Pane root = new Pane();
        GridPane gridPane = new GridPane();

        this.tableView = new FlowPane();
        try {
            tableView.getChildren().addAll(getGolfClubs());
            tableView.getStyleClass().add("tableview");
            tableView.setMaxSize(Applicaction.applicationSize[0] - 300, Applicaction.applicationSize[1] - 40);
            tableView.setAlignment(Pos.CENTER);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gridPane.add(getLogo(), 0, 0);
        gridPane.add(getHeader(), 1, 0);
        gridPane.add(getNavBar(), 0, 1);
        gridPane.add(tableView, 1, 1);

        root.getChildren().add(gridPane);

        scene = new Scene(root, Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/tableview.css")).toString());
        Applicaction.scenes.put("Golfclubs", scene);
    }

    public FlowPane getNavBar() {
        FlowPane navBar = GolfclubScreenUtils.getNavBar("Golfclubs", this, homeScreen, tableView, currentGolfer, currentUser);
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

    private FlowPane getGolfClubs() throws SQLException {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        TableView<Golfclub> tv = new TableView<>();

        String query = "SELECT * \n" +
                "FROM Golfclub gc\n" +
                "JOIN Golfer g ON gc.golfer_id = g.id\n" +
                "WHERE username = '" + currentUser.getUsername() + "';";

        ResultSet golfclubs = Applicaction.connection.query(query);

        ArrayList<Golfclub> golfclubList = new ArrayList<>();

        while (golfclubs.next()) {
            Golfclub g = new Golfclub(golfclubs);
            golfclubList.add(g);
        }

        if (!golfclubList.isEmpty()) {
            TableColumn<Golfclub, String> col0 = new TableColumn<>("Name");
            col0.setCellValueFactory(new PropertyValueFactory<>("clubName"));

            TableColumn<Golfclub, String> col1 = new TableColumn<>("Type");
            col1.setCellValueFactory(new PropertyValueFactory<>("clubType"));

            tv.getColumns().addAll(col0, col1);

            tv.getItems().addAll(golfclubList);

            flowPane.getChildren().add(tv);
            return flowPane;
        }
        return null;
    }

    public void reload(FlowPane tableView) {
        tableView.getChildren().clear();
        Controller.run(() -> {
            try {
                tableView.getChildren().addAll(getGolfClubs());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Scene getScene() {
        return scene;
    }

}
