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

/**
 * Hulpprogramma-klasse die gemeenschappelijke functionaliteit biedt voor het Golf Score Tracking System.
 */
public class Controller {

    /**
     * Voert de uit te voeren behandeling uit.
     *
     * @param behandeling De behandeling die moet worden uitgevoerd.
     * @throws IllegalArgumentException als de behandeling null is.
     */
    public static void run(Runnable behandeling) {
        if (behandeling == null) throw new IllegalArgumentException("De uit te voeren behandeling kan niet null zijn");

        if (Platform.isFxApplicationThread()) behandeling.run();
        else Platform.runLater(behandeling);
    }

    /**
     * Toont een waarschuwingsvenster met het gespecificeerde waarschuwingstype, titel, kop en inhoud.
     *
     * @param alertType Het type waarschuwing.
     * @param titel     De titel van het waarschuwingsvenster.
     * @param kop    De kop tekst van het waarschuwingsvenster.
     * @param inhoud   De inhoudstekst van het waarschuwingsvenster.
     */
    public static void showAlert(Alert.AlertType alertType, String titel, String kop, String inhoud) {
        Alert waarschuwing = new Alert(alertType);
        waarschuwing.setTitle(titel);
        waarschuwing.setHeaderText(kop);
        waarschuwing.setContentText(inhoud);
        waarschuwing.showAndWait();
    }

    /**
     * Maakt een HBox aan met een label en een tekstveld.
     *
     * @param labelText   De tekst om weer te geven in het label.
     * @param promptText  De tekst om weer te geven als prompt in het tekstveld.
     * @return De aangemaakte HBox.
     */
    public static HBox createFieldBox(String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setMinWidth(100);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.getStyleClass().add("label");

        TextField textField = new TextField(promptText);
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.getStyleClass().add("text-field");

        HBox hbox = new HBox(5, label, textField);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    /**
     * Maakt een HBox aan met een label en een tekstveld met prompttekst.
     *
     * @param labelText De tekst om weer te geven in het label.
     * @param promptText De prompttekst om weer te geven in het tekstveld.
     * @return De aangemaakte HBox.
     */
    public static HBox createPromptFieldBox(String labelText, String promptText) {
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

    /**
     * Maakt een HBox aan met een label en een datumkiezer met de opgegeven begindatum.
     *
     * @param labelText      De tekst om weer te geven in het label.
     * @param textFieldValue De initiële waarde om weer te geven in de datumkiezer.
     * @return De aangemaakte HBox.
     */
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
