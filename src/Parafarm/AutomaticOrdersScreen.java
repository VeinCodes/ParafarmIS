package Parafarm;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AutomaticOrdersScreen {

    private VBox root;
    private Stage stage;
    private VBox orderListBox;
    private static Timeline autoCheckTimer;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

        VBox senderBox = buildInfoCard(
            "Αποστολέας ParaFarm",
            "ΑΦΜ: 987654321  Θεσσαλονίκη",
            null, null, WHITE, BORDER
        );
        HBox.setHgrow(senderBox, Priority.ALWAYS);

        VBox providerBox = buildInfoCard(
            "Προμηθευτής Χονδρεμπόριο ΑΕ",
            "ΑΦΜ: 111222333",
            "Τηλ: 2310676767",
            null, WHITE, BORDER
        );
        HBox.setHgrow(providerBox, Priority.ALWAYS);
        
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

        HBox backRow = new HBox(backBtn);
        backRow.setPadding(new Insets(12, 16, 0, 16));
        backRow.setStyle("-fx-background-color: " + BG + ";");

        HBox partiesRow = new HBox(12, senderBox, providerBox);
        partiesRow.setPadding(new Insets(12, 16, 8, 16));
        partiesRow.setMaxWidth(Double.MAX_VALUE);

        VBox lastExecBox = buildInfoCard(
            "Τελευταία εκτέλεση: Δευτέρα",
            "31/03/2026 08:00 — Επιτυχής",
            null, null, GREEN_SOFT, GREEN
        );
        HBox.setHgrow(lastExecBox, Priority.ALWAYS);

        VBox nextExecBox = buildInfoCard(
            "Επόμενη: Δευτέρα",
            "07/04/2026 08:00",
            null, null, YELLOW_SOFT, YELLOW
        );
        HBox.setHgrow(nextExecBox, Priority.ALWAYS);

        HBox execRow = new HBox(12, lastExecBox, nextExecBox);
        execRow.setPadding(new Insets(0, 16, 12, 16));
        execRow.setMaxWidth(Double.MAX_VALUE);

        String[] headers = {"Ημερομηνία", "Προϊόν", "Ποσότητα", "Κωδικός", "Κατάσταση"};

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

        orderListBox = new VBox(6);
        orderListBox.setPadding(new Insets(8, 12, 8, 12));
        orderListBox.setStyle("-fx-background-color: " + BG + ";");

        loadOrders();

        ScrollPane scroll = new ScrollPane(orderListBox);
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

        Button retryBtn = new Button("Επανάληψη Αποτυχιών");
        retryBtn.setPrefHeight(38);
        retryBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );
        retryBtn.setOnAction(e -> {
            checkStock();
            loadOrders();
        });

        Button manualBtn = new Button("Χειροκίνητη Εκτέλεση Τώρα");
        manualBtn.setPrefHeight(38);
        manualBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;"
        );
        manualBtn.setOnAction(e -> {
            checkStock();
            loadOrders();
        });

        Region btnSpacer = new Region();
        HBox.setHgrow(btnSpacer, Priority.ALWAYS);

       
        HBox bottomBar = new HBox(12, retryBtn, btnSpacer, manualBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(12, 16, 12, 16));
        bottomBar.setStyle("-fx-background-color: " + WHITE + ";");

        root = new VBox(0, backRow, partiesRow, execRow, tableSection, bottomBar);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(0, 0, 0, 0));
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        startAutoCheckTimer();

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    private void loadOrders() {
        orderListBox.getChildren().clear();
        ArrayList<ProviderOrder> orders = Main.providerOrders;
        if (orders == null || orders.isEmpty()) {
            Label empty = new Label("Δεν υπάρχουν αυτόματες παραγγελίες ακόμα.");
            empty.setFont(Font.font("Arial", 13));
            empty.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
            empty.setPadding(new Insets(16));
            orderListBox.getChildren().add(empty);
            return;
        }
        for (ProviderOrder po : orders) {
            buildOrderRow(po);
        }
    }

    private HBox buildOrderRow(ProviderOrder po) {
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

        Label dateLbl = new Label(po.getDate() != null ? po.getDate() : "—");
        dateLbl.setFont(Font.font("Arial", 13));
        dateLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        dateLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(dateLbl, Priority.ALWAYS);

        Label productLbl = new Label(po.getProductName() != null ? po.getProductName() : "—");
        productLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        productLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        productLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(productLbl, Priority.ALWAYS);

        Label qtyLbl = new Label(String.valueOf(po.getQuantity()));
        qtyLbl.setFont(Font.font("Arial", 13));
        qtyLbl.setAlignment(Pos.CENTER);
        qtyLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
        qtyLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(qtyLbl, Priority.ALWAYS);

        Label codeLbl = new Label(po.getOrderCode() != null ? po.getOrderCode() : "—");
        codeLbl.setFont(Font.font("Arial", 13));
        codeLbl.setAlignment(Pos.CENTER);
        codeLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        codeLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(codeLbl, Priority.ALWAYS);

        String status = po.getSituation();
        String statusColor = GREEN;
        String statusBg    = GREEN_SOFT;
        String statusText  = "Απεστάλη";
        if (status != null) {
            if (status.equalsIgnoreCase("pending")) {
                statusColor = YELLOW; statusBg = YELLOW_SOFT; statusText = "Εκκρεμεί";
            } else if (status.equalsIgnoreCase("failed")) {
                statusColor = RED; statusBg = RED_SOFT; statusText = "Αποτυχία";
            }
        }

        Label statusLbl = new Label(statusText);
        statusLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLbl.setAlignment(Pos.CENTER);
        statusLbl.setMaxWidth(Double.MAX_VALUE);
        statusLbl.setStyle(
            "-fx-text-fill: " + statusColor + ";" +
            "-fx-background-color: " + statusBg + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 4 12;"
        );
        HBox.setHgrow(statusLbl, Priority.ALWAYS);

        row.getChildren().addAll(dateLbl, productLbl, qtyLbl, codeLbl, statusLbl);
        orderListBox.getChildren().add(row);
        return row;
    }

    private VBox buildInfoCard(String line1, String line2, String line3, String line4, String bg, String border) {
        VBox box = new VBox(3);
        box.setPadding(new Insets(12));
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        for (String line : new String[]{line1, line2, line3, line4}) {
            if (line != null) {
                Label lbl = new Label(line);
                lbl.setFont(Font.font("Arial", 13));
                lbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
                lbl.setWrapText(true);
                box.getChildren().add(lbl);
            }
        }
        return box;
    }

    public void startAutoCheckTimer() {
        if (autoCheckTimer != null) autoCheckTimer.stop();
        autoCheckTimer = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            Platform.runLater(() -> {
                checkStock();
            });
        }));
        autoCheckTimer.setCycleCount(Timeline.INDEFINITE);
        autoCheckTimer.play();
    }

    public void checkStock() {
        boolean anyRestock = false;
        for (Stock stock : Main.stocks) {
            if (stock.CheckStockandLimit()) {
                Product p = new Product(stock.getProductName(), stock.getProductPrice());
                ProviderOrder po = new ProviderOrder(p, stock.getUsualOrderAmount());
                po.setDate(LocalDateTime.now().format(FMT));
                stock.restock(stock.getUsualProvider());
                po.setSituation("finished");
                Main.providerOrders.add(po);
                anyRestock = true;
            }
        }
        if (anyRestock) {
            notify_();
        }
    }

    public ProviderOrder getOrderData() {
        if (Main.providerOrders == null || Main.providerOrders.isEmpty()) return null;
        return Main.providerOrders.get(Main.providerOrders.size() - 1);
    }

    public void notify_() {
        if (Main.primaryStage == null) return;
        Platform.runLater(() -> {
            Popup popup = new Popup();

            Label msg = new Label("✔  Αυτόματη παραγγελία εκτελέστηκε!");
            msg.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            msg.setStyle(
                "-fx-text-fill: white;" +
                "-fx-background-color: #2e7d32;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 12 20;"
            );

            popup.getContent().add(msg);
            popup.setAutoHide(true);
            popup.show(
                Main.primaryStage,
                Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - 150,
                Main.primaryStage.getY() + 40
            );

            Timeline hide = new Timeline(new KeyFrame(Duration.seconds(3), ev -> popup.hide()));
            hide.play();
        });
    }
}