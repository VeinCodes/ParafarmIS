package Parafarm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageRightsScreen {

    private VBox root;
    private Stage stage;
    private VBox userListBox;

    private static final String GREEN        = "#2e7d32";
    private static final String GREEN_LIGHT  = "#43a047";
    private static final String GREEN_SOFT   = "#e8f5e9";
    private static final String YELLOW_SOFT  = "#fffde7";
    private static final String YELLOW_BORDER= "#f0d080";
    private static final String RED          = "#c62828";
    private static final String BG           = "#f5f6f8";
    private static final String WHITE        = "#ffffff";
    private static final String BORDER       = "#e0e0e0";
    private static final String TEXT_MAIN    = "#1a1a1a";
    private static final String TEXT_MUTED   = "#757575";

    private static final String[] ROLES     = {"Ταμίας", "Αποθήκη", "Πωλητής", "Εξυπηρέτηση", "Διεύθυνση"};
    private static final String[] FUNCTIONS = {
        "Έκδοση τιμολογίου",
        "Καταχώρηση παραγγελίας",
        "Προβολή αποθέματος",
        "Ορισμός ορίων αποθέματος",
        "Διαχείριση χρηστών"
    };
    private static final boolean[][] PERMISSIONS = {
        {true,  false, true,  false, false},
        {false, false, true,  false, false},
        {false, true,  false, false, true },
        {false, true,  false, false, true },
        {false, false, false, false, true }
    };

    public Scene getScene(Stage stage) {
        this.stage = stage;

        Label titleLbl = new Label("Διαχείριση Δικαιωμάτων Χρηστών");
        titleLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        titleLbl.setStyle("-fx-text-fill: " + GREEN + ";");

        Button backBtn = new Button("← Πίσω");
        backBtn.setPrefHeight(32);
        backBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 16;"
        );
        backBtn.setOnAction(e -> stage.setScene(Main.mainScreen.getScene(stage)));

        HBox topBar = new HBox(12, backBtn, titleLbl);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 16, 12, 16));
        topBar.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(10, 16, 10, 16));
        tableHeader.setStyle("-fx-background-color: " + GREEN_SOFT + ";");
        for (String h : new String[]{"Όνομα", "Username", "Ρόλος", ""}) {
            Label lbl = new Label(h.toUpperCase());
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            lbl.setStyle("-fx-text-fill: " + GREEN + ";");
            lbl.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            tableHeader.getChildren().add(lbl);
        }

        userListBox = new VBox(6);
        userListBox.setPadding(new Insets(8, 12, 8, 12));
        userListBox.setStyle("-fx-background-color: " + BG + ";");

        loadUsers();

        ScrollPane scroll = new ScrollPane(userListBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox tableSection = new VBox(0, tableHeader, scroll);
        tableSection.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        root = new VBox(0, topBar, tableSection);
        root.setPadding(new Insets(0, 16, 16, 16));
        root.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    private void loadUsers() {
        userListBox.getChildren().clear();
        if (Main.users.isEmpty()) {
            Label empty = new Label("Δεν υπάρχουν χρήστες.");
            empty.setFont(Font.font("Arial", 13));
            empty.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-padding: 16;");
            userListBox.getChildren().add(empty);
            return;
        }
        for (User user : Main.users) {
            showUserData(user);
        }
    }

    public User showUserData(User user) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        row.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label nameLbl = new Label(user.getName() != null ? user.getName() : "—");
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        nameLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLbl, Priority.ALWAYS);

        Label usernameLbl = new Label(user.getUsername() != null ? user.getUsername() : "—");
        usernameLbl.setFont(Font.font("Arial", 13));
        usernameLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        usernameLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(usernameLbl, Priority.ALWAYS);

        Label roleLbl = new Label(user.getRole() != null ? user.getRole() : "—");
        roleLbl.setFont(Font.font("Arial", 13));
        roleLbl.setStyle(
            "-fx-text-fill: " + GREEN + ";" +
            "-fx-background-color: " + GREEN_SOFT + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 3 10;"
        );
        roleLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(roleLbl, Priority.ALWAYS);

        Button editBtn = new Button("Επεξεργασία");
        editBtn.setPrefHeight(32);
        editBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";-fx-text-fill: " + GREEN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        );
        editBtn.setOnMouseEntered(e -> editBtn.setStyle(
            "-fx-background-color: " + GREEN + ";-fx-text-fill: white;-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        ));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";-fx-text-fill: " + GREEN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        ));
        editBtn.setOnAction(e -> showEditPopup(user, roleLbl));

        row.getChildren().addAll(nameLbl, usernameLbl, roleLbl, editBtn);
        userListBox.getChildren().add(row);
        return user;
    }

    private void showEditPopup(User user, Label roleDisplayLbl) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.35);");
        overlay.setMaxWidth(Double.MAX_VALUE);
        overlay.setMaxHeight(Double.MAX_VALUE);

        Label popupTitle = new Label("Επεξεργασία χρήστη - " + user.getName());
        popupTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        popupTitle.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        popupTitle.setPadding(new Insets(0, 0, 12, 0));

        Label nameLbl     = buildFieldLabel("Όνομα");
        Label usernameLbl = buildFieldLabel("Username");
        Label roleLbl     = buildFieldLabel("Ρόλος");
        Label passLbl     = buildFieldLabel("Νέος Κωδικός");

        TextField nameField     = buildField(user.getName());
        TextField usernameField = buildField(user.getUsername());
        TextField passField     = buildField("");
        passField.setPromptText("Πληκτρολογίστε...");

        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(ROLES);
        roleDropdown.setValue(user.getRole() != null ? user.getRole() : ROLES[0]);
        roleDropdown.setPrefHeight(36);
        roleDropdown.setMaxWidth(Double.MAX_VALUE);
        roleDropdown.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 13px;"
        );

        VBox nameBox     = new VBox(4, nameLbl, nameField);
        VBox usernameBox = new VBox(4, usernameLbl, usernameField);
        VBox roleBox     = new VBox(4, roleLbl, roleDropdown);
        VBox passBox     = new VBox(4, passLbl, passField);

        for (VBox box : new VBox[]{nameBox, usernameBox, passBox}) {
            box.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(box, Priority.ALWAYS);
            ((TextField) box.getChildren().get(1)).setMaxWidth(Double.MAX_VALUE);
        }
        roleBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(roleBox, Priority.ALWAYS);
        roleDropdown.setMaxWidth(Double.MAX_VALUE);

        HBox fieldsRow = new HBox(12, nameBox, usernameBox, roleBox, passBox);
        fieldsRow.setMaxWidth(Double.MAX_VALUE);

        Label permTitle = new Label("Πίνακας Δικαιωμάτων");
        permTitle.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        permTitle.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        permTitle.setPadding(new Insets(14, 0, 6, 0));

        GridPane permGrid = new GridPane();
        permGrid.setMaxWidth(Double.MAX_VALUE);
        permGrid.setStyle(
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-color: " + WHITE + ";" +
            "-fx-background-radius: 8;"
        );

        String[] roleColors = {GREEN, GREEN_LIGHT, "#f9a825", "#1565c0", RED};

        Label funcHeader = new Label("Λειτουργία");
        funcHeader.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        funcHeader.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-padding: 8 12; -fx-background-color: " + BG + ";");
        funcHeader.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(funcHeader, Priority.ALWAYS);
        permGrid.add(funcHeader, 0, 0);

        for (int r = 0; r < ROLES.length; r++) {
            Label rLbl = new Label(ROLES[r]);
            rLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            rLbl.setAlignment(Pos.CENTER);
            rLbl.setMaxWidth(Double.MAX_VALUE);
            rLbl.setStyle("-fx-text-fill: " + roleColors[r] + "; -fx-padding: 8 12; -fx-background-color: " + BG + "; -fx-alignment: center;");
            GridPane.setHgrow(rLbl, Priority.ALWAYS);
            permGrid.add(rLbl, r + 1, 0);
        }

        for (int f = 0; f < FUNCTIONS.length; f++) {
            String rowBg = f % 2 == 0 ? WHITE : BG;
            Label fLbl = new Label(FUNCTIONS[f]);
            fLbl.setFont(Font.font("Arial", 12));
            fLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + "; -fx-padding: 8 12; -fx-background-color: " + rowBg + ";");
            fLbl.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(fLbl, Priority.ALWAYS);
            permGrid.add(fLbl, 0, f + 1);

            for (int r = 0; r < ROLES.length; r++) {
                boolean allowed = PERMISSIONS[f][r];
                Label cell = new Label(allowed ? "✓" : "✗");
                cell.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                cell.setAlignment(Pos.CENTER);
                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setStyle(
                    "-fx-text-fill: " + (allowed ? GREEN : "#cccccc") + ";" +
                    "-fx-padding: 8 12;" +
                    "-fx-background-color: " + rowBg + ";" +
                    "-fx-alignment: center;"
                );
                GridPane.setHgrow(cell, Priority.ALWAYS);
                permGrid.add(cell, r + 1, f + 1);
            }
        }

        Button cancelBtn = new Button("Ακύρωση");
        cancelBtn.setPrefHeight(36);
        cancelBtn.setStyle(
            "-fx-background-color: " + WHITE + ";-fx-text-fill: " + TEXT_MAIN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 13px;-fx-background-radius: 8;-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;-fx-border-radius: 8;-fx-cursor: hand;-fx-padding: 0 20;"
        );

        Button saveBtn = new Button("Αποθήκευση");
        saveBtn.setPrefHeight(36);
        saveBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";-fx-text-fill: white;-fx-font-weight: bold;" +
            "-fx-font-size: 13px;-fx-background-radius: 8;-fx-cursor: hand;-fx-padding: 0 20;"
        );

        Region btnSpacer = new Region();
        HBox.setHgrow(btnSpacer, Priority.ALWAYS);

        HBox btnRow = new HBox(10, btnSpacer, cancelBtn, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        btnRow.setPadding(new Insets(14, 0, 0, 0));

        VBox popupCard = new VBox(0, popupTitle, fieldsRow, permTitle, permGrid, btnRow);
        popupCard.setPadding(new Insets(24));
        popupCard.setMaxWidth(720);
        popupCard.setStyle(
            "-fx-background-color: " + YELLOW_SOFT + ";" +
            "-fx-border-color: " + YELLOW_BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 16, 0, 0, 4);"
        );

        overlay.getChildren().add(popupCard);

        Scene currentScene = stage.getScene();
        StackPane existingStack = (StackPane) currentScene.getRoot();
        existingStack.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> {
            existingStack.getChildren().remove(overlay);
            loadUsers();
        });

        saveBtn.setOnAction(e -> {
            if (!nameField.getText().trim().isEmpty())     user.setName(nameField.getText().trim());
            if (!usernameField.getText().trim().isEmpty()) user.setUsername(usernameField.getText().trim());
            user.setRole(roleDropdown.getValue());
            if (!passField.getText().trim().isEmpty())     user.setPassword(passField.getText().trim());
            roleDisplayLbl.setText(user.getRole() != null ? user.getRole() : "—");
            existingStack.getChildren().remove(overlay);
            loadUsers();
        });
    }

    private Label buildFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        return lbl;
    }

    private TextField buildField(String value) {
        TextField field = new TextField(value != null ? value : "");
        field.setPrefHeight(36);
        field.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 0 10;" +
            "-fx-font-size: 13px;"
        );
        return field;
    }
}