package Parafarm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LogoutScreen {

    private VBox root;
    private Stage stage;

    private static final String RED        = "#c62828";
    private static final String RED_LIGHT  = "#e53935";
    private static final String WHITE      = "#ffffff";
    private static final String BORDER     = "#e0e0e0";
    private static final String TEXT_MAIN  = "#1a1a1a";
    private static final String TEXT_MUTED = "#757575";

    public Scene getScene(Stage stage) {
        this.stage = stage;
        root = new VBox();
        return new Scene(root, 800, 600);
    }

    public void showPopup(Stage stage) {
        this.stage = stage;

        Label titleLbl = new Label("Αποσύνδεση");
        titleLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        titleLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");

        Label messageLbl = new Label("Είσαστε σίγουροι ότι θέλετε να\nαποσυνδεθείτε από το σύστημα");
        messageLbl.setFont(Font.font("Arial", 14));
        messageLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        messageLbl.setAlignment(Pos.CENTER);
        messageLbl.setWrapText(true);

        String userInfo = "";
        if (Main.currentUser != null) {
            userInfo = "Χρήστης: " + (Main.currentUser.getName() != null ? Main.currentUser.getName() : "—") +
                       "  Ρόλος: " + (Main.currentUser.getRole() != null ? Main.currentUser.getRole() : "—");
        }
        Label userLbl = new Label(userInfo);
        userLbl.setFont(Font.font("Arial", 12));
        userLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        userLbl.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Ακύρωση");
        cancelBtn.setPrefHeight(42);
        cancelBtn.setPrefWidth(160);
        cancelBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );

        Button logoutBtn = new Button("Αποσύνδεση");
        logoutBtn.setPrefHeight(42);
        logoutBtn.setPrefWidth(160);
        logoutBtn.setStyle(
            "-fx-background-color: " + RED_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        HBox btnRow = new HBox(12, cancelBtn, logoutBtn);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(8, 0, 0, 0));

        VBox card = new VBox(16, titleLbl, messageLbl, userLbl, btnRow);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32));
        card.setMaxWidth(420);
        card.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 4);"
        );

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true);
        StackPane.setAlignment(card, Pos.CENTER);

        Scene currentScene = stage.getScene();
        StackPane existingStack = (StackPane) currentScene.getRoot();
        existingStack.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> {
            existingStack.getChildren().remove(overlay);
        });

        logoutBtn.setOnAction(e -> {
            existingStack.getChildren().remove(overlay);
            Main.currentUser = null;
            stage.setScene(new LoginScreen().getScene(stage));
        });
    }
}