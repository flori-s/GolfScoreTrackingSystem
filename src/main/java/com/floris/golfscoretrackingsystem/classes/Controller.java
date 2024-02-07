package com.floris.golfscoretrackingsystem.classes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Controller {
    public static void run(Runnable treatment) {
        if (treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");

        if (Platform.isFxApplicationThread()) treatment.run();
        else Platform.runLater(treatment);
    }

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static HBox createFieldBox(String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.getStyleClass().add("label");

        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.getStyleClass().add("text-field");

        HBox hbox = new HBox(5, label, textField);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public static HBox createDateFieldBox(String labelText, String textFieldValue) {
        Label label = new Label(labelText);
        label.getStyleClass().add("label");
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate initialDate = LocalDate.parse(textFieldValue, dateFormatter);

        DatePicker datePicker = new DatePicker(initialDate);
        datePicker.setEditable(false);
        datePicker.getStyleClass().add("date-field");

        HBox hbox = new HBox(5, label, datePicker);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
