package Parafarm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainScreen {

    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";

    public Scene getScene(Stage stage) {

        Label title = new Label("ParaFarm IS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: " + GREEN + ";");

        Label subtitle = new Label("Επιλέξτε ενέργεια");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        VBox header = new VBox(4, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(32, 0, 24, 0));

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(0, 40, 40, 40));

        int col = 0;
        int row = 0;

        if (hasAccess("Πωλητής", "Διεύθυνση")) {
            grid.add(buildCard("Παραγγελίες", "Δημιουργία νέας παραγγελίας", true, e -> {
                stage.setScene(Main.ordersScreen.getScene(stage));
            }), col++, row);
            if (col > 2) { col = 0; row++; }
        }

        if (hasAccess("Αποθήκη", "Διεύθυνση","Εξηπηρέτηση")) {
            grid.add(buildCard("Απόθεμα", "Διαχείριση αποθέματος", true, e -> {
                stage.setScene(Main.stockScreen.getScene(stage));
            }), col++, row);
            if (col > 2) { col = 0; row++; }
        }

        if (hasAccess("Αποθήκη", "Διεύθυνση")) {
            grid.add(buildCard("Αυτόματες Παραγγελίες", "Αυτόματη υποβολή παραγγελιών", true, e -> {
                stage.setScene(Main.automaticOrdersScreen.getScene(stage));
            }), col++, row);
            if (col > 2) { col = 0; row++; }
        }

        if (hasAccess("Διεύθυνση")) {
            grid.add(buildCard("Διαχείριση Δικαιωμάτων", "Ρόλοι και πρόσβαση χρηστών", true, e -> {
                stage.setScene(Main.manageRightsScreen.getScene(stage));
            }), col++, row);
            if (col > 2) { col = 0; row++; }
        }

        if (hasAccess("Αποθήκη", "Διεύθυνση")) {
            grid.add(buildCard("Θέσιμο Ορίων", "Αλλαγή ορίων αποθεμάτων", true, e -> {
                stage.setScene(Main.seelimitScreen.getScene(stage));
            }), col++, row);
            if (col > 2) { col = 0; row++; }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        VBox root = new VBox(header, scrollPane);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setAlignment(Pos.TOP_CENTER);

        return new Scene(root, 850, 550);
    }

    private VBox buildCard(String title, String subtitle, boolean active,
                           javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLbl.setStyle("-fx-text-fill: " + (active ? TEXT_MAIN : TEXT_MUTED) + ";");
        titleLbl.setWrapText(true);

        Label subLbl = new Label(subtitle);
        subLbl.setFont(Font.font("Arial", 11));
        subLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        subLbl.setWrapText(true);

        Button btn = new Button(active ? "Άνοιγμα →" : "");
        btn.setText(active ? "Άνοιγμα →" : "");
        btn.setText(active ? "Άνοιγμα →" : "Σύντομα");
        btn.setPrefHeight(30);
        btn.setStyle(
            "-fx-background-color: " + (active ? GREEN : BORDER) + ";" +
            "-fx-text-fill: " + (active ? WHITE : TEXT_MUTED) + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 16;" +
            "-fx-cursor: " + (active ? "hand" : "default") + ";" +
            "-fx-padding: 0 16;"
        );
        if (active) btn.setOnAction(action);

        VBox card = new VBox(8,titleLbl, subLbl, btn);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setPrefHeight(160);
        card.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + (active ? GREEN_LIGHT : BORDER) + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        return card;
    }
    
    private boolean hasAccess(String... allowedRoles) {
        if (Main.users.isEmpty()) return true;
        User current = Main.currentUser; 
        if (current == null) return true;
        String role = current.getRole();
        for (String r : allowedRoles) {
            if (r.equals(role)) return true;
        }
        return false;
    }    
}