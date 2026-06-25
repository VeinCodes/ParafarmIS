package Parafarm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;

public class StockScreen {

    private VBox root;
    private Stage stage;
    private VBox stockListBox;
    
    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String YELLOW      = "#f9a825";
    private static final String YELLOW_SOFT = "#fffde7";
    private static final String RED         = "#c62828";
    private static final String RED_SOFT    = "#ffebee";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";

    public Scene getScene(Stage stage) {
        this.stage = stage;
        
        stockListBox = new VBox(6);
        stockListBox.setPadding(new Insets(8, 12, 8, 12));
        stockListBox.setStyle("-fx-background-color: " + BG + ";");

        ArrayList<Stock> stockData = getStockData();

        long okCount       = stockData.stream().filter(s -> s.getCurrentAmount() >= s.getLimit()).count();
        long lowCount      = stockData.stream().filter(s -> s.getCurrentAmount() > 0 && s.getCurrentAmount() < s.getLimit() && s.getCurrentAmount() >= s.getLimit() * 0.25).count();
        long criticalCount = stockData.stream().filter(s -> s.getCurrentAmount() == 0 || s.getCurrentAmount() < s.getLimit() * 0.25).count();

        VBox okCard       = buildStatusCard(String.valueOf(okCount),       "ΟΚ Απόθεμα",              GREEN,  GREEN_SOFT);
        VBox lowCard      = buildStatusCard(String.valueOf(lowCount),      "Χαμηλό Απόθεμα",          YELLOW, YELLOW_SOFT);
        VBox criticalCard = buildStatusCard(String.valueOf(criticalCount), "Κρίσιμα Χαμηλό Απόθεμα", RED,    RED_SOFT);

        HBox.setHgrow(okCard,       Priority.ALWAYS);
        HBox.setHgrow(lowCard,      Priority.ALWAYS);
        HBox.setHgrow(criticalCard, Priority.ALWAYS);

        HBox statusRow = new HBox(16, okCard, lowCard, criticalCard);
        statusRow.setPadding(new Insets(16, 16, 8, 16));

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("Όλες", "ΟΚ", "Χαμηλό", "Κρίσιμο");
        filterBox.setValue("Όλες");
        filterBox.setPrefHeight(36);
        filterBox.setMaxWidth(Double.MAX_VALUE);
        filterBox.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 13px;"
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση Προϊόντος...");
        searchField.setPrefHeight(36);
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;" +
            "-fx-font-size: 13px;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Label filterLabel = new Label("Κατάσταση");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        filterLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        VBox filterLabelBox = new VBox(2, filterLabel, filterBox);

        Button backBtn = new Button("← Πίσω");
        backBtn.setPrefHeight(36);
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

        HBox filterRow = new HBox(12, filterLabelBox, searchField);
        filterRow.setAlignment(Pos.BOTTOM_LEFT);
        filterRow.setPadding(new Insets(8, 16, 8, 16));

        String[] headers = {"Όριο", "Προϊόν", "Απόθεμα", "Κωδικός", "Κατάσταση", ""};
        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(10, 16, 10, 16));
        tableHeader.setStyle("-fx-background-color: " + GREEN_SOFT + ";");
        for (int i = 0; i < headers.length; i++) {
            Label lbl = new Label(headers[i].toUpperCase());
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            lbl.setStyle("-fx-text-fill: " + GREEN + ";");
            lbl.setAlignment(i == 0 ? Pos.CENTER_LEFT : Pos.CENTER);
            lbl.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            tableHeader.getChildren().add(lbl);
        }

        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshList(newVal, filterBox.getValue()));
        filterBox.valueProperty().addListener((obs, oldVal, newVal) -> refreshList(searchField.getText(), newVal));

        ScrollPane scroll = new ScrollPane(stockListBox);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
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

        HBox backRow = new HBox(backBtn);
        backRow.setPadding(new Insets(12, 16, 0, 16));
        root = new VBox(0, backRow, statusRow, filterRow, tableSection);
        root.setPadding(new Insets(0, 16, 16, 16));
        root.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 600);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    private void refreshList(String query, String filter) {
        stockListBox.getChildren().clear();
        for (Stock stock : new ArrayList<>(Main.stocks)) {
            boolean matchesSearch = query.trim().isEmpty() ||
                stock.getProductName().toLowerCase().contains(query.toLowerCase());
            boolean isOk       = stock.getCurrentAmount() >= stock.getLimit();
            boolean isCritical = stock.getCurrentAmount() == 0 || stock.getCurrentAmount() < stock.getLimit() * 0.25;
            boolean isLow      = !isOk && !isCritical;
            boolean matchesFilter = filter.equals("Όλες") ||
                (filter.equals("ΟΚ")      && isOk) ||
                (filter.equals("Χαμηλό")  && isLow) ||
                (filter.equals("Κρίσιμο") && isCritical);
            if (matchesSearch && matchesFilter) buildStockRow(stock);
        }
    }

    private HBox buildStockRow(Stock stock) {
        HBox row = new HBox();
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

        boolean isOk       = stock.getCurrentAmount() >= stock.getLimit();
        boolean isCritical = stock.getCurrentAmount() == 0 || stock.getCurrentAmount() < stock.getLimit() * 0.25;

        String statusText  = isOk ? "OK" : (isCritical ? "Κρίσιμο" : "Χαμηλό");
        String statusColor = isOk ? GREEN : (isCritical ? RED : YELLOW);
        String statusBg    = isOk ? GREEN_SOFT : (isCritical ? RED_SOFT : YELLOW_SOFT);

        Label limitLbl = new Label(String.valueOf(stock.getLimit()));
        limitLbl.setFont(Font.font("Arial", 13));
        limitLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        limitLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(limitLbl, Priority.ALWAYS);

        Label nameLbl = new Label(stock.getProductName());
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        nameLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLbl, Priority.ALWAYS);

        Label amountLbl = new Label(String.valueOf(stock.getCurrentAmount()));
        amountLbl.setAlignment(Pos.CENTER);
        amountLbl.setFont(Font.font("Arial", 13));
        amountLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        amountLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(amountLbl, Priority.ALWAYS);

        Label codeLbl = new Label("");
        codeLbl.setAlignment(Pos.CENTER);
        codeLbl.setFont(Font.font("Arial", 13));
        codeLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        codeLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(codeLbl, Priority.ALWAYS);

        Label statusLbl = new Label(statusText);
        statusLbl.setAlignment(Pos.CENTER);
        statusLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLbl.setMaxWidth(Double.MAX_VALUE);
        statusLbl.setStyle(
            "-fx-text-fill: " + statusColor + ";" +
            "-fx-background-color: " + statusBg + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 4 12;"
        );
        HBox.setHgrow(statusLbl, Priority.ALWAYS);

        Button orderBtn = new Button("Παραγγελία");
        orderBtn.setPrefHeight(32);
        orderBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";-fx-text-fill: " + GREEN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        );
        orderBtn.setOnMouseEntered(e -> orderBtn.setStyle(
            "-fx-background-color: " + GREEN + ";-fx-text-fill: white;-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        ));
        orderBtn.setOnMouseExited(e -> orderBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";-fx-text-fill: " + GREEN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 12px;-fx-background-radius: 16;-fx-cursor: hand;-fx-padding: 0 14;"
        ));
        orderBtn.setOnAction(e -> showOrderPopup(stock));

        row.getChildren().addAll(limitLbl, nameLbl, amountLbl, codeLbl, statusLbl, orderBtn);
        stockListBox.getChildren().add(row);
        return row;
    }

    private void showOrderPopup(Stock stock) {
        Label title = new Label("Παραγγελία για: " + stock.getProductName());
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        title.setPadding(new Insets(0, 0, 12, 0));

        Label qtyLbl = new Label("Ποσότητα");
        qtyLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        qtyLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        TextField qtyField = new TextField(String.valueOf(stock.getUsualOrderAmount()));
        qtyField.setPrefHeight(36);
        qtyField.setMaxWidth(Double.MAX_VALUE);
        qtyField.setStyle(fieldStyle());

        Label providerLbl = new Label("Προμηθευτής");
        providerLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        providerLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        TextField providerField = new TextField();
        providerField.setPromptText("Όνομα προμηθευτή...");
        providerField.setPrefHeight(36);
        providerField.setMaxWidth(Double.MAX_VALUE);
        providerField.setStyle(fieldStyle());

        Label emailLbl = new Label("Email");
        emailLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        emailLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        TextField emailField = new TextField();
        emailField.setPromptText("email@...");
        emailField.setPrefHeight(36);
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailField.setStyle(fieldStyle());

        Label addressLbl = new Label("Διεύθυνση");
        addressLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        addressLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        TextField addressField = new TextField();
        addressField.setPromptText("Διεύθυνση...");
        addressField.setPrefHeight(36);
        addressField.setMaxWidth(Double.MAX_VALUE);
        addressField.setStyle(fieldStyle());

        VBox qtyBox      = new VBox(4, qtyLbl, qtyField);
        VBox providerBox = new VBox(4, providerLbl, providerField);
        VBox emailBox    = new VBox(4, emailLbl, emailField);
        VBox addressBox  = new VBox(4, addressLbl, addressField);
        for (VBox box : new VBox[]{qtyBox, providerBox, emailBox, addressBox}) {
            box.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(box, Priority.ALWAYS);
            ((TextField) box.getChildren().get(1)).setMaxWidth(Double.MAX_VALUE);
        }

        HBox fieldsRow = new HBox(12, qtyBox, providerBox, emailBox, addressBox);
        fieldsRow.setMaxWidth(Double.MAX_VALUE);

        Label errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: " + RED + "; -fx-font-size: 11px;");
        errorLbl.setPadding(new Insets(4, 0, 0, 0));

        Button cancelBtn = new Button("Ακύρωση");
        cancelBtn.setPrefHeight(36);
        cancelBtn.setStyle(
            "-fx-background-color: " + WHITE + ";-fx-text-fill: " + TEXT_MAIN + ";-fx-font-weight: bold;" +
            "-fx-font-size: 13px;-fx-background-radius: 8;-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;-fx-border-radius: 8;-fx-cursor: hand;-fx-padding: 0 20;"
        );

        Button confirmBtn = new Button("Παραγγελία");
        confirmBtn.setPrefHeight(36);
        confirmBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";-fx-text-fill: white;-fx-font-weight: bold;" +
            "-fx-font-size: 13px;-fx-background-radius: 8;-fx-cursor: hand;-fx-padding: 0 20;"
        );

        Region btnSpacer = new Region();
        HBox.setHgrow(btnSpacer, Priority.ALWAYS);
        HBox btnRow = new HBox(10, btnSpacer, cancelBtn, confirmBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        btnRow.setPadding(new Insets(14, 0, 0, 0));

        VBox card = new VBox(0, title, fieldsRow, errorLbl, btnRow);
        card.setPadding(new Insets(24));
        card.setMaxWidth(700);
        card.setStyle(
            "-fx-background-color: #fffde7;-fx-border-color: #f0d080;" +
            "-fx-border-width: 1;-fx-border-radius: 12;-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 16, 0, 0, 4);"
        );

        StackPane overlay = new StackPane(card);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.35);");

        Scene currentScene = stage.getScene();
        StackPane existingStack = (StackPane) currentScene.getRoot();
        existingStack.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> existingStack.getChildren().remove(overlay));

        confirmBtn.setOnAction(e -> {
            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                String providerName    = providerField.getText().trim();
                String providerEmail   = emailField.getText().trim();
                String providerAddress = addressField.getText().trim();

                if (providerName.isEmpty()) { errorLbl.setText("Παρακαλώ εισάγετε όνομα προμηθευτή."); return; }

                Provider provider = new Provider(providerName, providerEmail, providerAddress);
                Product p = new Product(stock.getProductName(), stock.getProductPrice());
                ProviderOrder po = new ProviderOrder(p, qty);
                po.setDate(java.time.LocalDate.now().toString());
                po.setSituation("finished");
                po.setProvider(provider);
                Main.providerOrders.add(po);

                stock.setCurrentAmount(stock.getCurrentAmount() + qty);

                existingStack.getChildren().remove(overlay);
                stage.setScene(Main.stockScreen.getScene(stage));
            } catch (NumberFormatException ex) {
                errorLbl.setText("Η ποσότητα πρέπει να είναι αριθμός.");
            }
        });
    }

    private String fieldStyle() {
        return "-fx-background-color: " + WHITE + ";-fx-border-color: " + BORDER + ";" +
               "-fx-border-width: 1;-fx-border-radius: 6;-fx-background-radius: 6;" +
               "-fx-padding: 0 10;-fx-font-size: 13px;";
    }

    private VBox buildStatusCard(String count, String label, String color, String bgColor) {
        Label countLbl = new Label(count);
        countLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        countLbl.setStyle("-fx-text-fill: " + color + ";");

        Label labelLbl = new Label(label);
        labelLbl.setFont(Font.font("Arial", 12));
        labelLbl.setStyle("-fx-text-fill: " + color + ";");

        VBox card = new VBox(4, countLbl, labelLbl);
        card.setAlignment(Pos.CENTER);
        card.setPrefHeight(80);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );
        return card;
    }

    public ArrayList<Stock> getStockData() {
        ArrayList<Stock> stocks = new ArrayList<>(Main.stocks);
        for (Stock stock : stocks) {
            buildStockRow(stock);
        }
        return stocks;
    }
}