package com.floris.golfscoretrackingsystem.screens;

import com.floris.golfscoretrackingsystem.Applicaction;
import com.floris.golfscoretrackingsystem.classes.User;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen {
    private final Scene scene;
    int maxAttempts = 3;


    public LoginScreen() {
        FlowPane container = new FlowPane();
        container.setMinSize(Applicaction.applicationSize[0], Applicaction.applicationSize[1]);
        container.setAlignment(Pos.CENTER);

        FlowPane form = new FlowPane();
        Text accountField = new Text("Account ?");
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Login");
        form.getChildren().addAll(accountField, registerButton, loginButton);
        form.setOrientation(Orientation.VERTICAL);

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

        container.getChildren().add(form);
        scene = new Scene(container);
    }

    private VBox getLoginForm() {
        VBox loginForm = new VBox();
        loginForm.setSpacing(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");

        loginForm.getChildren().addAll(usernameField, passwordField, loginButton);

        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField));

        return loginForm;
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField) {
        int localMaxAttempts = maxAttempts;
        boolean loginSuccessful = false;
        User u = null;

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Incomplete Information", "Please fill in all fields");
            return;
        }

        try (ResultSet users = getUsers()) {

            while (maxAttempts > 0 && users.next()) {
                if (usernameField.getText().equals(users.getString("username")) && passwordField.getText().equals(users.getString("password"))) {
                    u = new User(users.getString("username"), users.getString("password"));
                    loginSuccessful = true;
                    break;
                }
            }

            users.close();

            if (loginSuccessful) {
                showHomeScreen(u);
            } else {
                usernameField.clear();
                passwordField.clear();

                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong",
                        "Wrong Username or Password. Remaining attempts: " + localMaxAttempts);


                maxAttempts--;

                if (maxAttempts <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong", "Contact customer service: customerservice@GSTS.com");
                    Applicaction.mainStage.close();
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(ex);
        }
    }


    private VBox getRegisterForm() {
        VBox registerForm = new VBox();
        registerForm.setSpacing(10);

        TextField firstnameField = new TextField();
        firstnameField.setPromptText("Firstname");

        TextField lastnameField = new TextField();
        lastnameField.setPromptText("Lastname");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");

        registerButton.setOnAction(e -> {
            handleRegister(usernameField, passwordField, firstnameField, lastnameField);
        });

        registerForm.getChildren().addAll(usernameField, passwordField, firstnameField, lastnameField, registerButton);

        return registerForm;
    }

    private void handleRegister(TextField usernameField, PasswordField passwordField, TextField firstnameField, TextField lastnameField) {
        boolean loginSuccessful = false;

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Incomplete Information", "Please fill in all fields");
            return;
        }

        try (ResultSet users = getUsers()) {
            while (users.next()) {
                if (usernameField.getText().equals(users.getString("username"))) {
                    loginSuccessful = true;
                    break;
                }
            }

            if (!loginSuccessful) {
                String insertQuery = "INSERT INTO Golfer(username, password, firstname, lastname) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = Applicaction.connection.getConnection().prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, usernameField.getText());
                    preparedStatement.setString(2, passwordField.getText());
                    preparedStatement.setString(3, firstnameField.getText());
                    preparedStatement.setString(4, lastnameField.getText());
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                showHomeScreen(new User(usernameField.getText(), passwordField.getText()));
            } else {
                usernameField.clear();
                showAlert(Alert.AlertType.ERROR, "Error", "Username Already Taken", "The chosen username is already in use. Please choose another");

            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(ex);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private ResultSet getUsers() {
        try {
            ResultSet golfer = Applicaction.connection.query("SELECT * FROM golfer");
            return golfer;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }
    }

    public Scene getScene() {
        return scene;
    }

    private void showHomeScreen(User user) {
        Applicaction.mainStage.setScene(Applicaction.scenes.put("home", new HomeScreen(user).getScene()));
    }
}
