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

public class OrdersScreen {
	

    private Stage stage;
    private VBox orderListBox;

    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String BLUE        = "#1565c0";
    private static final String BLUE_SOFT   = "#e3f2fd";
    private static final String YELLOW      = "#f9a825";
    private static final String YELLOW_SOFT = "#fffde7";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";
    
    public Customer customer = new Customer("giannis", 6987715922L, "negrou67");

    public Scene getScene(Stage stage) {
        this.stage = stage;

        long okCount       = Main.orders.stream().filter(o -> "finished".equalsIgnoreCase(o.getStatus())).count();
        long pendingCount  = Main.orders.stream().filter(o -> "pending".equalsIgnoreCase(o.getStatus())).count();
        long activeCount   = Main.orders.stream().filter(o -> "active".equalsIgnoreCase(o.getStatus())).count();

        VBox okCard      = buildStatusCard(String.valueOf(okCount),      "Ολοκληρωμένες",  GREEN,  GREEN_SOFT);
        VBox pendingCard = buildStatusCard(String.valueOf(pendingCount),  "Σε Εξέλιξη",     YELLOW, YELLOW_SOFT);
        VBox activeCard  = buildStatusCard(String.valueOf(activeCount),   "Ενεργές",        BLUE,   BLUE_SOFT);

        HBox.setHgrow(okCard,      Priority.ALWAYS);
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        HBox.setHgrow(activeCard,  Priority.ALWAYS);

        HBox statusRow = new HBox(16, okCard, pendingCard, activeCard);
        statusRow.setPadding(new Insets(16, 16, 8, 16));

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("Όλες", "Ολοκληρωμένες", "Σε Εξέλιξη", "Ενεργές");
        filterBox.setValue("Όλες");
        filterBox.setPrefHeight(36);
        filterBox.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 13px;"
        );

        Label filterLabel = new Label("Κατάσταση");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        filterLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        VBox filterBox2 = new VBox(2, filterLabel, filterBox);

        Label dateLbl = new Label("Ημερομηνία");
        dateLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dateLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        TextField dateField = new TextField("02/04/2026");
        dateField.setPrefHeight(36);
        dateField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;" +
            "-fx-font-size: 13px;"
        );
        VBox dateBox = new VBox(2, dateLbl, dateField);

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση Παραγγελίας...");
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

        Button addBtn = new Button("+ Νέα Παραγγελία");
        addBtn.setPrefHeight(36);
        addBtn.setStyle(
            "-fx-background-color: " + GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;"
        );
        addBtn.setOnAction(e -> stage.setScene(Main.newOrderScreen.getScene(stage)));

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

        HBox filterRow = new HBox(12, filterBox2, dateBox, searchField, addBtn);
        filterRow.setAlignment(Pos.BOTTOM_LEFT);
        filterRow.setPadding(new Insets(8, 16, 8, 16));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            refreshList(newVal, filterBox.getValue());
        });
        filterBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            refreshList(searchField.getText(), newVal);
        });

        int[] headerWidths = {180, 160, 120, 140, 120};
        String[] headers = {"Αριθμός Παραγγελίας", "Φαρμακείο", "Πωλητής", "Ημερομηνία", "Κατάσταση"};

        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(10, 16, 10, 16));
        tableHeader.setStyle("-fx-background-color: " + GREEN_SOFT + ";");
        for (int i = 0; i < headers.length; i++) {
            Label lbl = new Label(headers[i].toUpperCase());
            lbl.setPrefWidth(headerWidths[i]);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            lbl.setStyle("-fx-text-fill: " + GREEN + ";");
            lbl.setAlignment(i == 0 ? Pos.CENTER_LEFT : Pos.CENTER);
            tableHeader.getChildren().add(lbl);
        }

        orderListBox = new VBox(6);
        orderListBox.setPadding(new Insets(8, 12, 8, 12));
        orderListBox.setStyle("-fx-background-color: " + BG + ";");

        loadOrders("", "Όλες");

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

        VBox root = new VBox(0, backRow, statusRow, filterRow, tableSection);
        root.setPadding(new Insets(0, 16, 16, 16));
        root.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    public ArrayList<Order> getOrdersData() {
        return new ArrayList<>(Main.orders);
    }

    public Order getOrderData(String order_number) {
        for (Order order : Main.orders) {
            if (order.getOrderId().toLowerCase().contains(order_number.toLowerCase()) ||
            		order.getCustomer() != null && order.getCustomer().getName().toLowerCase().contains(order_number.toLowerCase())) {
                return order;
            }
        }
        new ErrorMessage().printError("Δεν βρέθηκε παραγγελία με: \"" + order_number + "\"", orderListBox);
        return null;
    }

    private void loadOrders(String query, String filter) {
        orderListBox.getChildren().clear();
        ArrayList<Order> orders = getOrdersData();
        if (orders.isEmpty()) {
            Label empty = new Label("Δεν υπάρχουν παραγγελίες ακόμα.");
            empty.setFont(Font.font("Arial", 13));
            empty.setStyle("-fx-text-fill: #757575; -fx-padding: 16;");
            orderListBox.getChildren().add(empty);
            return;
        }
        Order found = query.trim().isEmpty() ? null : getOrderData(query);
        for (Order order : orders) {
            boolean matchSearch = query.trim().isEmpty() || order == found;
            boolean matchFilter = filter.equals("Όλες") ||
                (filter.equals("Ολοκληρωμένες") && "finished".equalsIgnoreCase(order.getStatus())) ||
                (filter.equals("Σε Εξέλιξη")    && "pending".equalsIgnoreCase(order.getStatus())) ||
                (filter.equals("Ενεργές")        && "active".equalsIgnoreCase(order.getStatus()));
            if (matchSearch && matchFilter) buildOrderRow(order);
        }
    }

    private void refreshList(String query, String filter) {
        loadOrders(query, filter);
    }

    private HBox buildOrderRow(Order order) {
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

        Label idLbl = new Label(order.getOrderId() != null ? order.getOrderId() : "—");
        idLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        idLbl.setStyle("-fx-text-fill: " + "#1a1a1a" + ";");

        Label customerLbl = new Label(order.getCustomer() != null ? order.getCustomer().getName() : "—");
        customerLbl.setFont(Font.font("Arial", 13));
        customerLbl.setAlignment(Pos.CENTER);
        customerLbl.setStyle("-fx-text-fill: " + "#1a1a1a" + ";");

        Label sellerLbl = new Label(order.getSeller() != null && !order.getSeller().isEmpty() ? order.getSeller() : "—");
        sellerLbl.setFont(Font.font("Arial", 13));
        sellerLbl.setAlignment(Pos.CENTER);
        sellerLbl.setStyle("-fx-text-fill: " + "#757575" + ";");

        Label dateLbl = new Label(order.getDate() != null ? order.getDate() : "—");
        dateLbl.setFont(Font.font("Arial", 13));
        dateLbl.setAlignment(Pos.CENTER);
        dateLbl.setStyle("-fx-text-fill: " + "#757575" + ";");

        String status = order.getStatus();
        String statusColor = "#2e7d32";
        String statusBg    = "#e8f5e9";
        String statusText  = "Ολοκληρώθηκε";
        if ("pending".equalsIgnoreCase(status)) {
            statusColor = "#1565c0"; statusBg = "#e3f2fd"; statusText = "Σε Εξέλιξη";
        } else if ("active".equalsIgnoreCase(status)) {
            statusColor = "#f9a825"; statusBg = "#fffde7"; statusText = "Ενεργή";
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

        int[] colWidths = {180, 160, 120, 140, 120};
        idLbl.setPrefWidth(colWidths[0]);
        customerLbl.setPrefWidth(colWidths[1]);
        sellerLbl.setPrefWidth(colWidths[2]);
        dateLbl.setPrefWidth(colWidths[3]);
        statusLbl.setPrefWidth(colWidths[4]);

        row.getChildren().addAll(idLbl, customerLbl, sellerLbl, dateLbl, statusLbl);
        orderListBox.getChildren().add(row);
        return row;
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
}