package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {

    ObservableList<String> ticketListe = FXCollections.observableArrayList();

    // Farben als Konstanten
    private static final String DARK_BG = "#1e1e1e";
    private static final String FIELD_BG = "#2b2b2b";
    private static final String FIELD_STYLE =
        "-fx-background-color: #2b2b2b;" +
        "-fx-text-fill: white;" +
        "-fx-prompt-text-fill: #888888;" +
        "-fx-highlight-text-fill: white;" +
        "-fx-highlight-fill: #4a4a4a;" +
        "-fx-border-color: #555555;" +
        "-fx-border-radius: 8;" +
        "-fx-background-radius: 8;" +
        "-fx-padding: 8;";

    @Override
    public void start(Stage stage) {

        // ===== FELDER =====
        TextField titelFeld = new TextField();
        titelFeld.setPromptText("Titel eingeben...");
        titelFeld.setStyle(FIELD_STYLE);

        TextArea beschreibungFeld = new TextArea();
        beschreibungFeld.setPromptText("Beschreibung eingeben...");
        beschreibungFeld.setPrefHeight(80);
        beschreibungFeld.setWrapText(true);
        beschreibungFeld.setStyle(FIELD_STYLE);

        TextField suchFeld = new TextField();
        suchFeld.setPromptText("Ticket suchen...");
        suchFeld.setStyle(FIELD_STYLE);

        // ===== DROPDOWNS =====
        ComboBox<String> prioritaet = new ComboBox<>();
        prioritaet.getItems().addAll("🔴 Hoch", "🟡 Mittel", "🟢 Niedrig");
        prioritaet.setPromptText("Priorität wählen");
        prioritaet.setMaxWidth(Double.MAX_VALUE);
        prioritaet.setStyle(FIELD_STYLE);
        fixComboBoxTextColor(prioritaet);

        ComboBox<String> kategorie = new ComboBox<>();
        kategorie.getItems().addAll("🐛 Bug", "📋 Anfrage", "❓ Sonstiges");
        kategorie.setPromptText("Kategorie wählen");
        kategorie.setMaxWidth(Double.MAX_VALUE);
        kategorie.setStyle(FIELD_STYLE);
        fixComboBoxTextColor(kategorie);

        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll("📂 Offen", "⚙️ In Bearbeitung", "✅ Gelöst");
        status.setPromptText("Status wählen");
        status.setMaxWidth(Double.MAX_VALUE);
        status.setStyle(FIELD_STYLE);
        fixComboBoxTextColor(status);

        // ===== MELDUNG & ZÄHLER =====
        Label meldung = new Label();
        meldung.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Label zaehler = new Label("📊 Tickets gesamt: 0");
        zaehler.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");

        // ===== TICKET LISTE =====
        ListView<String> ticketAnzeige = new ListView<>(ticketListe);
        ticketAnzeige.setPrefHeight(180);
        ticketAnzeige.setStyle(
            "-fx-background-color: #2b2b2b;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        ticketAnzeige.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle(
                        "-fx-background-color: #3a3a3a;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: #555555;" +
                        "-fx-border-width: 0 0 1 0;"
                    );
                }
            }
        });

        // ===== BUTTONS =====
        Button speichernBtn = new Button("💾 Speichern");
        Button bearbeitenBtn = new Button("✏️ Bearbeiten");
        Button loeschenBtn = new Button("🗑️ Löschen");
        Button alleLoeschenBtn = new Button("🧹 Alle löschen");
        Button suchenBtn = new Button("🔍 Suchen");

        styleButton(speichernBtn, "#4CAF50");
        styleButton(bearbeitenBtn, "#2196F3");
        styleButton(loeschenBtn, "#f44336");
        styleButton(alleLoeschenBtn, "#FF5722");
        styleButton(suchenBtn, "#9C27B0");

        // ===== SPEICHERN =====
        speichernBtn.setOnAction(e -> {
            if (titelFeld.getText().isEmpty()) {
                zeigeFehlermeldung(meldung, "❌ Titel darf nicht leer sein!");
            } else if (beschreibungFeld.getText().isEmpty()) {
                zeigeFehlermeldung(meldung, "❌ Beschreibung darf nicht leer sein!");
            } else if (prioritaet.getValue() == null) {
                zeigeFehlermeldung(meldung, "❌ Bitte Priorität wählen!");
            } else if (kategorie.getValue() == null) {
                zeigeFehlermeldung(meldung, "❌ Bitte Kategorie wählen!");
            } else if (status.getValue() == null) {
                zeigeFehlermeldung(meldung, "❌ Bitte Status wählen!");
            } else {
                String ticket = titelFeld.getText()
                        + " | " + prioritaet.getValue()
                        + " | " + kategorie.getValue()
                        + " | " + status.getValue();
                ticketListe.add(ticket);

                titelFeld.clear();
                beschreibungFeld.clear();
                prioritaet.setValue(null);
                kategorie.setValue(null);
                status.setValue(null);

                meldung.setText("✅ Ticket gespeichert!");
                meldung.setTextFill(Color.LIGHTGREEN);
                zaehler.setText("📊 Tickets gesamt: " + ticketListe.size());
            }
        });

        // ===== BEARBEITEN =====
        bearbeitenBtn.setOnAction(e -> {
            String ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
            if (ausgewaehlt == null) {
                zeigeFehlermeldung(meldung, "❌ Bitte erst ein Ticket auswählen!");
            } else {
                ticketListe.remove(ausgewaehlt);
                String[] teile = ausgewaehlt.split(" \\| ");
                titelFeld.setText(teile[0]);
                if (teile.length > 1) prioritaet.setValue(teile[1]);
                if (teile.length > 2) kategorie.setValue(teile[2]);
                if (teile.length > 3) status.setValue(teile[3]);

                meldung.setText("✏️ Bearbeiten — danach Speichern!");
                meldung.setTextFill(Color.LIGHTBLUE);
                zaehler.setText("📊 Tickets gesamt: " + ticketListe.size());
            }
        });

        // ===== LÖSCHEN =====
        loeschenBtn.setOnAction(e -> {
            String ausgewaehlt = ticketAnzeige.getSelectionModel().getSelectedItem();
            if (ausgewaehlt == null) {
                zeigeFehlermeldung(meldung, "❌ Bitte erst ein Ticket auswählen!");
            } else {
                ticketListe.remove(ausgewaehlt);
                meldung.setText("🗑️ Ticket gelöscht!");
                meldung.setTextFill(Color.SALMON);
                zaehler.setText("📊 Tickets gesamt: " + ticketListe.size());
            }
        });

        // ===== ALLE LÖSCHEN =====
        alleLoeschenBtn.setOnAction(e -> {
            ticketListe.clear();
            meldung.setText("🧹 Alle Tickets gelöscht!");
            meldung.setTextFill(Color.SALMON);
            zaehler.setText("📊 Tickets gesamt: 0");
        });

        // ===== SUCHEN =====
        suchenBtn.setOnAction(e -> {
            String suche = suchFeld.getText().toLowerCase();
            if (suche.isEmpty()) {
                ticketAnzeige.setItems(ticketListe);
                meldung.setText("🔍 Alle Tickets angezeigt!");
                meldung.setTextFill(Color.WHITE);
            } else {
                ObservableList<String> gefunden = FXCollections.observableArrayList();
                for (String t : ticketListe) {
                    if (t.toLowerCase().contains(suche)) {
                        gefunden.add(t);
                    }
                }
                ticketAnzeige.setItems(gefunden);
                meldung.setText("🔍 " + gefunden.size() + " Ticket(s) gefunden!");
                meldung.setTextFill(Color.LIGHTBLUE);
            }
        });

        // ===== HAUPT LAYOUT =====
        Label appTitel = new Label("🎫 Ticket System");
        appTitel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        appTitel.setTextFill(Color.WHITE);

        HBox buttons1 = new HBox(10);
        buttons1.getChildren().addAll(speichernBtn, bearbeitenBtn, loeschenBtn, alleLoeschenBtn);

        HBox suchZeile = new HBox(10);
        suchZeile.getChildren().addAll(suchFeld, suchenBtn);
        HBox.setHgrow(suchFeld, Priority.ALWAYS);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: " + DARK_BG + ";");
        layout.getChildren().addAll(
            appTitel,
            new Separator(),
            styledLabel("Titel:"), titelFeld,
            styledLabel("Beschreibung:"), beschreibungFeld,
            styledLabel("Priorität:"), prioritaet,
            styledLabel("Kategorie:"), kategorie,
            styledLabel("Status:"), status,
            buttons1,
            new Separator(),
            styledLabel("🔍 Suche:"), suchZeile,
            styledLabel("📋 Alle Tickets:"),
            ticketAnzeige,
            zaehler,
            meldung
        );

        Scene scene = new Scene(layout, 500, 750);
        stage.setTitle("🎫 Ticket System");
        stage.setScene(scene);
        stage.show();
    }

    // ===== HILFSMETHODEN =====
    private void fixComboBoxTextColor(ComboBox<String> box) {
        box.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle(
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #2b2b2b;" +
                        "-fx-padding: 6;"
                    );
                }
            }
        });
        box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(box.getPromptText());
                    setStyle("-fx-text-fill: #888888;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
    }

    private void styleButton(Button btn, String farbe) {
        btn.setStyle(
            "-fx-background-color: " + farbe + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + farbe + ", -20%);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + farbe + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;"
        ));
    }

    private Label styledLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        return label;
    }

    private void zeigeFehlermeldung(Label label, String text) {
        label.setText(text);
        label.setTextFill(Color.SALMON);
    }

    public static void main(String[] args) {
        launch(args);
    }
}