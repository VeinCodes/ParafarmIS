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

public class SeeLimitScreen {

    private VBox root;
    private Stage stage;
    private VBox stockListBox;

    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";
    private static final String RED         = "#c62828";

    public Scene getScene(Stage stage) {
        this.stage = stage;

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση Προϊόντος...");
        searchField.setPrefHeight(38);
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 0 16;" +
            "-fx-font-size: 13px;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Label titleLbl = new Label("Ορισμός Ορίων Αποθέματος");
        titleLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        titleLbl.setStyle("-fx-text-fill: " + GREEN + ";");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        Button backBtn = new Button("← Πίσω");
        backBtn.setPrefHeight(38);
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

        HBox topBar = new HBox(16, backBtn, titleLbl, topSpacer, searchField);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(16, 16, 12, 16));
        topBar.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        String[] headers = {"Προϊόν", "Τρέχον Απόθεμα", "Τρέχον Όριο", ""};

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

        stockListBox = new VBox(6);
        stockListBox.setPadding(new Insets(8, 12, 8, 12));
        stockListBox.setStyle("-fx-background-color: " + BG + ";");

        getProductsData();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            stockListBox.getChildren().clear();
            for (Stock stock : Main.stocks) {
                if (newVal.trim().isEmpty() || stock.getProductName().toLowerCase().contains(newVal.toLowerCase())) {
                    buildStockRow(stock);
                }
            }
        });

        ScrollPane scroll = new ScrollPane(stockListBox);
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

        Scene scene = new Scene(root, 800, 600);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    private HBox buildStockRow(Stock stock) {
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

        Label nameLbl = new Label(stock.getProductName());
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        nameLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLbl, Priority.ALWAYS);

        Label amountLbl = new Label(stock.getCurrentAmount() + " τεμάχια");
        amountLbl.setAlignment(Pos.CENTER);
        amountLbl.setFont(Font.font("Arial", 13));
        amountLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        amountLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(amountLbl, Priority.ALWAYS);

        Label limitLbl = new Label(stock.getLimit() + " τεμάχια");
        limitLbl.setAlignment(Pos.CENTER);
        limitLbl.setFont(Font.font("Arial", 13));
        limitLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        limitLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(limitLbl, Priority.ALWAYS);

        Button changeLimitBtn = new Button("Αλλαγή Ορίου");
        changeLimitBtn.setPrefHeight(32);
        changeLimitBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";" +
            "-fx-text-fill: " + GREEN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 16;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 14;"
        );
        changeLimitBtn.setOnMouseEntered(e -> changeLimitBtn.setStyle(
            "-fx-background-color: " + GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 16;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 14;"
        ));
        changeLimitBtn.setOnMouseExited(e -> changeLimitBtn.setStyle(
            "-fx-background-color: " + GREEN_SOFT + ";" +
            "-fx-text-fill: " + GREEN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 16;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 14;"
        ));
        changeLimitBtn.setOnAction(e -> {
            getStock(Main.stocks.indexOf(stock));
        });

        row.getChildren().addAll(nameLbl, amountLbl, limitLbl, changeLimitBtn);
        stockListBox.getChildren().add(row);
        return row;
    }

    public ArrayList<Product> getProductsData() {
        ArrayList<Product> products = new ArrayList<>();
        for (Stock stock : Main.stocks) {
            products.add(new Product(stock.getProductName(), stock.getProductPrice()));
            buildStockRow(stock);
        }
        return products;
    }

    public Stock getStock(int product_number) {
        Stock stock = Main.stocks.get(product_number);
        Main.setLimitScreen.currentStock = stock;
        stage.setScene(Main.setLimitScreen.getScene(stage));
        return stock;
    }
}