package Parafarm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SetLimitScreen {

    private Stage stage;
    public Stock currentStock;
    private TextField newLimitField;
    private Label warningLbl;

    private static final String GREEN_LIGHT  = "#43a047";
    private static final String YELLOW_SOFT  = "#fffde7";
    private static final String YELLOW_BORDER = "#f0d080";
    private static final String RED          = "#c62828";
    private static final String BG           = "#f5f6f8";
    private static final String WHITE        = "#ffffff";
    private static final String BORDER       = "#e0e0e0";
    private static final String TEXT_MAIN    = "#1a1a1a";
    private static final String TEXT_MUTED   = "#757575";

    public Scene getScene(Stage stage) {
        this.stage = stage;

        String productName = currentStock != null ? currentStock.getProductName() : "—";
        int currentAmount  = currentStock != null ? currentStock.getCurrentAmount() : 0;
        int currentLimit   = currentStock != null ? currentStock.getLimit() : 0;

        Label titleLbl = new Label("Επιλεγμένο Προϊόν: " + productName);
        titleLbl.setMaxWidth(Double.MAX_VALUE);
        titleLbl.setAlignment(Pos.CENTER);
        titleLbl.setWrapText(true);
        titleLbl.setStyle(
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-family: Georgia;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 20 16 20 16;"
        );

        Label currentAmountLbl = new Label("Τρέχον Απόθεμα");
        currentAmountLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        currentAmountLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label currentAmountVal = new Label(currentAmount + " τεμάχια");
        currentAmountVal.setMaxWidth(Double.MAX_VALUE);
        currentAmountVal.setPrefHeight(40);
        currentAmountVal.setFont(Font.font("Arial", 13));
        currentAmountVal.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 12;" +
            "-fx-text-fill: " + TEXT_MAIN + ";"
        );

        VBox currentAmountBox = new VBox(6, currentAmountLbl, currentAmountVal);
        currentAmountBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(currentAmountBox, Priority.ALWAYS);

        Label currentLimitLbl = new Label("Τρέχον Όριο Ασφαλείας");
        currentLimitLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        currentLimitLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label currentLimitVal = new Label(currentLimit + " τεμάχια");
        currentLimitVal.setMaxWidth(Double.MAX_VALUE);
        currentLimitVal.setPrefHeight(40);
        currentLimitVal.setFont(Font.font("Arial", 13));
        currentLimitVal.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 12;" +
            "-fx-text-fill: " + TEXT_MAIN + ";"
        );

        VBox currentLimitBox = new VBox(6, currentLimitLbl, currentLimitVal);
        currentLimitBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(currentLimitBox, Priority.ALWAYS);

        HBox infoRow = new HBox(16, currentAmountBox, currentLimitBox);
        infoRow.setMaxWidth(Double.MAX_VALUE);
        infoRow.setPadding(new Insets(16, 16, 12, 16));

        Label newLimitLbl = new Label("Νέο Όριο Ασφαλείας");
        newLimitLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        newLimitLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        newLimitField = new TextField();
        newLimitField.setPromptText("Εισάγετε νέο όριο...");
        newLimitField.setPrefHeight(48);
        newLimitField.setMaxWidth(Double.MAX_VALUE);
        newLimitField.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        newLimitField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 12;" +
            "-fx-text-fill: " + TEXT_MAIN + ";"
        );

        Label hintLbl = new Label("Αυτόματη παραγγελία θα γίνει όταν το απόθεμα πέσει κάτω από αυτήν την τιμή");
        hintLbl.setFont(Font.font("Arial", 11));
        hintLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        hintLbl.setWrapText(true);
        hintLbl.setMaxWidth(Double.MAX_VALUE);
        hintLbl.setAlignment(Pos.CENTER);

        warningLbl = new Label();
        warningLbl.setFont(Font.font("Arial", 11));
        warningLbl.setStyle("-fx-text-fill: " + RED + ";");
        warningLbl.setWrapText(true);
        warningLbl.setMaxWidth(Double.MAX_VALUE);

        newLimitField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int typed = Integer.parseInt(newVal);
                int limit = getNewLimit();
                checkLimit(limit);
            } catch (NumberFormatException ex) {
                warningLbl.setText("");
            }
        });

        VBox newLimitBox = new VBox(6, newLimitLbl, newLimitField, hintLbl, warningLbl);
        newLimitBox.setMaxWidth(Double.MAX_VALUE);
        newLimitBox.setPadding(new Insets(0, 16, 12, 16));

        Button cancelBtn = new Button("Ακύρωση");
        cancelBtn.setPrefHeight(38);
        cancelBtn.setPrefWidth(120);
        cancelBtn.setStyle(
            "-fx-background-color: " + RED + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        cancelBtn.setOnAction(e -> {
            stage.setScene(Main.seelimitScreen.getScene(stage));
        });

        Button saveBtn = new Button("Αποθήκευση");
        saveBtn.setPrefHeight(38);
        saveBtn.setPrefWidth(140);
        saveBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        saveBtn.setOnAction(e -> {
            try {
                int newLimit = getNewLimit();
                setLimit(newLimit);
                stage.setScene(Main.seelimitScreen.getScene(stage));
            } catch (NumberFormatException ex) {
                warningLbl.setText("Παρακαλώ εισάγετε έναν έγκυρο αριθμό.");
            }
        });

        Region btnSpacer = new Region();
        HBox.setHgrow(btnSpacer, Priority.ALWAYS);

        HBox btnRow = new HBox(10, btnSpacer, cancelBtn, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        btnRow.setPadding(new Insets(8, 16, 16, 16));
        btnRow.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(0, infoRow, newLimitBox, btnRow);
        card.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(card, Priority.ALWAYS);
        card.setStyle(
            "-fx-background-color: " + YELLOW_SOFT + ";" +
            "-fx-border-color: " + YELLOW_BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );

        VBox cardWrapper = new VBox(card);
        cardWrapper.setAlignment(Pos.CENTER);
        cardWrapper.setPadding(new Insets(24));
        cardWrapper.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(cardWrapper, Priority.ALWAYS);

        VBox root = new VBox(0, titleLbl, cardWrapper);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(cardWrapper, Priority.ALWAYS);

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    public int getNewLimit() {
        try {
            return Integer.parseInt(newLimitField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void checkLimit(int limit) {
        int currentAmount = currentStock != null ? currentStock.getCurrentAmount() : 0;
        if (currentAmount < limit) {
            warningLbl.setText("Το τρέχον απόθεμα (" + currentAmount + ") είναι ήδη κάτω από το νέο όριο - θα ενεργοποιηθεί αυτόματη παραγγελία");
        } else {
            warningLbl.setText("");
        }
    }

    public void setLimit(int limit) {
        if (currentStock != null) {
            currentStock.setLimit(limit);
        }
    }
    
    public Stock getStock(int product_number) {
        if (product_number >= 0 && product_number < Main.stocks.size()) {
            return Main.stocks.get(product_number);
        }
        return null;
    }
}