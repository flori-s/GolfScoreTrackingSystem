package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.Controller;
import com.floris.golfscoretrackingsystem.classes.Golfer;
import com.floris.golfscoretrackingsystem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Het scherm waar gebruikers zich kunnen aanmelden of registreren.
 */
public class LoginScreen {
    private final Scene scene;
    int maxAttempts = 3;

    /**
     * Initialiseert een nieuw LoginScreen.
     */
    public LoginScreen() {
        FlowPane container = new FlowPane();
        container.getStyleClass().add("container");
        container.setMinSize(Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        container.setAlignment(Pos.CENTER);

        FlowPane form = new FlowPane();
        form.getStyleClass().add("form");
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setMaxSize(350, 200);
        form.setOrientation(Orientation.HORIZONTAL);
        form.setHgap(10);
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");
        form.getChildren().addAll(loginButton, registerButton);


        loginButton.setOnAction(e -> {
            form.getChildren().clear();
            form.getChildren().addAll(getLoginForm());
        });

        registerButton.setOnAction(e -> {
            form.getChildren().clear();
            form.getChildren().addAll(getRegisterForm());
        });

        form.setAlignment(Pos.CENTER);
        form.setVgap(10);
        form.setPrefSize(500, 600);

        container.getChildren().addAll(form);
        scene = new Scene(container);
        scene.getStylesheets().add(Objects.requireNonNull(Applicaction.class.getResource("stylesheets/loginscreen.css")).toString());
    }

    /**
     * Maakt het aanmeldingsformulier.
     * @return Een VBox die het aanmeldingsformulier bevat.
     */
    private VBox getLoginForm() {
        VBox loginForm = new VBox();
        loginForm.setSpacing(10);

        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");

        loginForm.getChildren().addAll(usernameField, passwordField, loginButton);

        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField));

        return loginForm;
    }

    /**
     * Behandelt het inlogproces.
     * @param usernameField Het veld voor gebruikersnaam.
     * @param passwordField Het veld voor wachtwoord.
     */
    private void handleLogin(TextField usernameField, PasswordField passwordField) {
        int localMaxAttempts = maxAttempts;
        boolean loginSuccessful = false;
        User u = null;
        Golfer g = null;

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Incomplete Information", "Please fill in all fields");
            return;
        }

        try (ResultSet users = getUsers()) {

            while (maxAttempts > 0 && users.next()) {
                if (usernameField.getText().equals(users.getString("username")) && passwordField.getText().equals(users.getString("password"))) {
                    u = new User(users.getString("username"), users.getString("password"));
                    g = new Golfer(users.getString("firstname"), users.getString("lastname"), Integer.parseInt(users.getString("handicap")));
                    loginSuccessful = true;
                    break;
                }
            }

            users.close();

            if (loginSuccessful) {
                showHomeScreen(u, g);
            } else {
                usernameField.clear();
                passwordField.clear();

                Controller.showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong",
                        "Wrong Username or Password. Remaining attempts: " + localMaxAttempts);
                maxAttempts--;

                if (maxAttempts <= 0) {
                    Controller.showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong", "Contact customer service: customerservice@GSTS.com");
                    Applicaction.mainStage.close();
                }
            }
        } catch (SQLException ex) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(ex);
        }
    }

    /**
     * Maakt het registratieformulier.
     * @return Een VBox die het registratieformulier bevat.
     */
    private VBox getRegisterForm() {
        VBox registerForm = new VBox();
        registerForm.setSpacing(10);

        TextField firstnameField = new TextField();
        firstnameField.getStyleClass().add("text-field");
        firstnameField.setPromptText("Firstname");

        TextField lastnameField = new TextField();
        lastnameField.getStyleClass().add("text-field");
        lastnameField.setPromptText("Lastname");

        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");

        registerButton.setOnAction(e -> {
            handleRegister(usernameField, passwordField, firstnameField, lastnameField);
        });

        registerForm.getChildren().addAll(usernameField, passwordField, firstnameField, lastnameField, registerButton);

        return registerForm;
    }

    /**
     * Behandelt het registratieproces.
     *
     * @param  usernameField   Het veld voor het invoeren van de gebruikersnaam
     * @param  passwordField   Het veld voor het invoeren van het wachtwoord
     * @param  firstnameField  Het veld voor het invoeren van de voornaam
     * @param  lastnameField   Het veld voor het invoeren van de achternaam
     */
    private void handleRegister(TextField usernameField, PasswordField passwordField, TextField firstnameField, TextField lastnameField) {
        boolean usernameTaken = false;

        // Check of alle velden zijn ingevuld
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty()) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Incomplete Information", "Please fill in all fields");
            return;
        }

        try (ResultSet users = getUsers()) {
            while (users.next()) {
                if (usernameField.getText().equals(users.getString("username"))) {
                    usernameTaken = true;
                    break;
                }
            }
            // Check of de gebruikersnaam niet al in gebruik is
            if (!usernameTaken) {
                String insertQuery = "INSERT INTO Golfer(username, password, firstname, lastname) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = Applicaction.connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, usernameField.getText());
                    preparedStatement.setString(2, passwordField.getText());
                    preparedStatement.setString(3, firstnameField.getText());
                    preparedStatement.setString(4, lastnameField.getText());
                    preparedStatement.executeUpdate();

                    // Maakt een nieuw user aan
                    User newUser = new User(usernameField.getText(), passwordField.getText());
                    Golfer newGolfer = new Golfer(firstnameField.getText(), lastnameField.getText(), 54);

                    showHomeScreen(newUser, newGolfer);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                usernameField.clear();
                Controller.showAlert(Alert.AlertType.ERROR, "Error", "Username Already Taken", "The chosen username is already in use. Please choose another");
            }
        } catch (SQLException ex) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(ex);
        }
    }

    /**
     * Haalt de gebruikersinformatie op uit de database.
     * @return Een ResultSet met de gebruikersinformatie.
     */
    private ResultSet getUsers() {
        try {
            ResultSet golfer = Applicaction.connection.query("SELECT * FROM Golfer");
            return golfer;
        } catch (SQLException e) {
            Controller.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }
    }

    /**
     * Geeft het Scene-object van het LoginScreen terug.
     * @return Het Scene-object van het LoginScreen.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Laat het startscherm zien na een succesvolle aanmelding.
     * @param user De aangemelde gebruiker.
     * @param golfer De bijbehorende golferinformatie.
     */
    private void showHomeScreen(User user, Golfer golfer) {
        HomeScreen homeScreen = new HomeScreen(user, golfer);
        Applicaction.mainStage.setScene(homeScreen.getScene());
    }

}
