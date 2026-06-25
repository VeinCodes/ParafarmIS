package Parafarm;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class NewOrderScreen {

    private Stage stage;
    public Order currentOrder;
    public Customer currentCustomer;
    private VBox orderCartBox;
    private VBox productListBox;
    private java.util.HashMap<String, HBox> cartItems = new HashMap<>();
    private java.util.HashMap<String, Integer> cartQuantities = new HashMap<>();
    

    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String RED         = "#c62828";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";

    public Scene getScene(Stage stage) {
        this.stage = stage;

        Label titleLbl = new Label("Νέα Παραγγελία");
        titleLbl.setMaxWidth(Double.MAX_VALUE);
        titleLbl.setAlignment(Pos.CENTER);
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

        Label nameLbl = new Label("Όνομα Πελάτη");
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        TextField nameField = new TextField();
        nameField.setPromptText("π.χ. Φαρμακείο Παπαδόπουλος");
        nameField.setPrefHeight(40);
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;" +
            "-fx-font-size: 13px;"
        );

        Label phoneLbl = new Label("Τηλέφωνο");
        phoneLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        phoneLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        TextField phoneField = new TextField();
        phoneField.setPromptText("π.χ. 6987654321");
        phoneField.setPrefHeight(40);
        phoneField.setMaxWidth(Double.MAX_VALUE);
        phoneField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;" +
            "-fx-font-size: 13px;"
        );

        Label addressLbl = new Label("Διεύθυνση");
        addressLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        addressLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        TextField addressField = new TextField();
        addressField.setPromptText("π.χ. Εγνατία 12, Θεσσαλονίκη");
        addressField.setPrefHeight(40);
        addressField.setMaxWidth(Double.MAX_VALUE);
        addressField.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;" +
            "-fx-font-size: 13px;"
        );

        Label errorLbl = new Label();
        errorLbl.setFont(Font.font("Arial", 12));
        errorLbl.setStyle("-fx-text-fill: " + RED + ";");
        errorLbl.setMaxWidth(Double.MAX_VALUE);

        VBox form = new VBox(12, nameLbl, nameField, phoneLbl, phoneField, addressLbl, addressField, errorLbl);
        form.setMaxWidth(Double.MAX_VALUE);
        form.setPadding(new Insets(20, 20, 12, 20));

        Button cancelBtn = new Button("Ακύρωση");
        cancelBtn.setPrefHeight(38);
        cancelBtn.setPrefWidth(120);
        cancelBtn.setStyle(
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
        cancelBtn.setOnAction(e -> stage.setScene(Main.ordersScreen.getScene(stage)));

        Button nextBtn = new Button("Επόμενο →");
        nextBtn.setPrefHeight(38);
        nextBtn.setPrefWidth(140);
        nextBtn.setStyle(
            "-fx-background-color: " + GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;"
        );
        nextBtn.setOnAction(e -> {
            String name    = nameField.getText().trim();
            String phone   = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty())  { errorLbl.setText("Παρακαλώ εισάγετε το όνομα πελάτη."); return; }
            if (phone.isEmpty()) { errorLbl.setText("Παρακαλώ εισάγετε τηλέφωνο."); return; }

            try {
                long phoneNum = Long.parseLong(phone);
                Customer existing = getCustomer(name);
                if (existing != null) {
                    selectCustomer(existing);
                } else {
                    createNewCustomer(name, phoneNum, address);
                }
                stage.setScene(getProductScene(stage));
            } catch (NumberFormatException ex) {
                errorLbl.setText("Το τηλέφωνο πρέπει να είναι αριθμός.");
            }
        });

        Region btnSpacer = new Region();
        HBox.setHgrow(btnSpacer, Priority.ALWAYS);

        HBox btnRow = new HBox(10, cancelBtn, btnSpacer, nextBtn);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(8, 20, 20, 20));
        btnRow.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(0, form, btnRow);
        card.setMaxWidth(560);
        card.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        VBox cardWrapper = new VBox(card);
        cardWrapper.setAlignment(Pos.CENTER);
        cardWrapper.setPadding(new Insets(32));
        cardWrapper.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(cardWrapper, Priority.ALWAYS);

        VBox root = new VBox(0, titleLbl, cardWrapper);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(cardWrapper, Priority.ALWAYS);
        
        root.setUserData("no-logout");
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        return scene;
    }

    public Scene getProductScene(Stage stage) {
        this.stage = stage;
        currentOrder = new Order();
        currentOrder.setCustomer(currentCustomer);
        if (Main.currentUser != null) {
            currentOrder.setSeller(Main.currentUser.getUsername());
        }
        cartItems.clear();
        cartQuantities.clear();

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση προϊόντος...");
        searchField.setStyle(
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #ccc;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 6 12;" +
            "-fx-font-size: 13px;"
        );
        searchField.setPrefHeight(36);

        Button searchBtn = new Button("Αναζήτηση");
        searchBtn.setPrefHeight(36);
        searchBtn.setStyle(
            "-fx-background-color: #43a047;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6 16;"
        );
        searchBtn.setOnAction(e -> {
            Stock found = getStock(searchField.getText());
            if (found != null) {
                productListBox.getChildren().clear();
                buildProductCard(found);
            }
        });

        HBox searchBar = new HBox(8, searchField, searchBtn);
        searchBar.setPadding(new Insets(8, 10, 8, 10));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox headerRow = new HBox();
        headerRow.setPadding(new Insets(10, 20, 10, 20));
        headerRow.setStyle("-fx-background-color: #e8e8e8;");
        for (String h : new String[]{"Προϊόν", "Τιμή", "Απόθεμα", "Κωδικός", "Προσθήκη"}) {
            Label lbl = new Label(h);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            lbl.setPrefWidth(160);
            lbl.setAlignment(Pos.CENTER);
            headerRow.getChildren().add(lbl);
        }

        productListBox = new VBox(10);
        productListBox.setPadding(new Insets(10));
        for (Stock stock : Main.stocks) buildProductCard(stock);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.trim().isEmpty()) {
                productListBox.getChildren().clear();
                for (Stock stock : Main.stocks) buildProductCard(stock);
            }
        });

        ScrollPane productScroll = new ScrollPane(productListBox);
        productScroll.setFitToWidth(true);
        productScroll.setStyle("-fx-background-color: white;");
        productScroll.setPrefHeight(300);

        VBox topSection = new VBox(0, searchBar, headerRow, productScroll);
        topSection.setStyle(
            "-fx-border-color: #ccc;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        orderCartBox = new VBox(10);
        orderCartBox.setPadding(new Insets(10));

        ScrollPane cartScroll = new ScrollPane(orderCartBox);
        cartScroll.setFitToWidth(true);
        cartScroll.setStyle("-fx-background-color: #f9f9f9;");
        cartScroll.setPrefHeight(140);

        Button submitBtn = new Button("Επόμενο");
        submitBtn.setPrefHeight(36);
        submitBtn.setStyle(
            "-fx-background-color: #43a047;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6 16;"
        );
        submitBtn.setOnAction(e -> {
            if (currentCustomer != null) {
            	currentCustomer.addOrder(currentOrder);
                Main.orders.add(currentOrder);
                stage.setScene(Main.invoiceScreen.getScene(stage));
            }
        });

        HBox submitBar = new HBox(submitBtn);
        submitBar.setPadding(new Insets(8, 10, 8, 10));
        submitBar.setAlignment(Pos.CENTER_RIGHT);

        VBox bottomSection = new VBox(cartScroll, submitBar);
        bottomSection.setStyle(
            "-fx-border-color: #ccc;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        VBox root = new VBox(10, topSection, bottomSection);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: white;");
        root.setUserData("no-logout");
        return new Scene(root, 850, 600);
    }

    public HBox buildProductCard(Stock stock) {
        HBox card = new HBox();
        card.setPadding(new Insets(12, 20, 12, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #ddd;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label nameLabel = new Label();
        nameLabel.setPrefWidth(160);
        nameLabel.setFont(Font.font("Arial", 13));

        Label priceLabel = new Label();
        priceLabel.setPrefWidth(160);
        priceLabel.setAlignment(Pos.CENTER);
        priceLabel.setFont(Font.font("Arial", 13));

        Label stockLabel = new Label();
        stockLabel.setPrefWidth(160);
        stockLabel.setAlignment(Pos.CENTER);
        stockLabel.setFont(Font.font("Arial", 13));

        Label codeLabel = new Label();
        codeLabel.setPrefWidth(160);
        codeLabel.setAlignment(Pos.CENTER);
        codeLabel.setFont(Font.font("Arial", 13));

        Button addBtn = new Button("+");
        addBtn.setPrefWidth(60);
        addBtn.setPrefHeight(30);
        addBtn.setStyle(
            "-fx-background-color: #66bb6a;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
        addBtn.setOnAction(e -> {
            Stock found = getStock(stock.getProductName());
            if (found != null) {
                String name = found.getProductName();
                int currentQty = cartQuantities.getOrDefault(name, 0);
                if (found.getCurrentAmount() < 1) return;
                if (cartItems.containsKey(name)) {
                    int newQty = currentQty + 1;
                    cartQuantities.put(name, newQty);
                    currentOrder.add_product(new Product(found.getProductName(), found.getProductPrice()));
                    found.setCurrentAmount(found.getCurrentAmount() - 1);
                    HBox existingItem = cartItems.get(name);
                    for (var node : existingItem.getChildren()) {
                        if (node instanceof Label lbl && lbl.getPrefWidth() == 50) {
                            lbl.setText("x" + newQty);
                            break;
                        }
                    }
                } else {
                    Product p = new Product(found.getProductName(), found.getProductPrice());
                    currentOrder.add_product(p);
                    found.setCurrentAmount(found.getCurrentAmount() - 1);
                    HBox cartItem = buildCartItem(p);
                    cartItems.put(name, cartItem);
                    cartQuantities.put(name, 1);
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        nameLabel.setText(stock.getProductName());
        priceLabel.setText(stock.getProductPrice() + " €");

        card.getChildren().addAll(nameLabel, priceLabel, stockLabel, codeLabel, spacer, addBtn);
        productListBox.getChildren().add(card);
        return card;
    }

    public HBox buildCartItem(Product product) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(10, 15, 10, 15));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #ddd;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Button cancelBtn = new Button("X");
        cancelBtn.setPrefWidth(35);
        cancelBtn.setPrefHeight(30);
        cancelBtn.setStyle(
            "-fx-background-color: #e53935;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );

        Label nameLabel = new Label();
        nameLabel.setFont(Font.font("Arial", 13));
        nameLabel.setPrefWidth(120);

        Label priceLabel = new Label();
        priceLabel.setFont(Font.font("Arial", 13));
        priceLabel.setPrefWidth(80);

        Label codeLabel = new Label();
        codeLabel.setFont(Font.font("Arial", 13));
        codeLabel.setPrefWidth(120);

        Label quantityLabel = new Label();
        quantityLabel.setFont(Font.font("Arial", 13));
        quantityLabel.setPrefWidth(50);
        quantityLabel.setAlignment(Pos.CENTER);
        quantityLabel.setStyle(
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 4;" +
            "-fx-padding: 2 6;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        nameLabel.setText(product.getName());
        priceLabel.setText(product.getPrice() + " €");
        quantityLabel.setText("x1");

        cancelBtn.setOnAction(e -> {
            currentOrder.remove_product(product);
            orderCartBox.getChildren().remove(item);
            Stock s = getStock(product.getName());
            if (s != null) s.setCurrentAmount(s.getCurrentAmount() + cartQuantities.getOrDefault(product.getName(), 1));
            cartItems.remove(product.getName());
            cartQuantities.remove(product.getName());
        });

        item.getChildren().addAll(cancelBtn, nameLabel, priceLabel, codeLabel, spacer, quantityLabel);
        orderCartBox.getChildren().add(item);
        return item;
    }

    public Stock getStock(String product_name) {
        for (Stock stock : Main.stocks) {
            if (stock.getProductName().toLowerCase().contains(product_name.toLowerCase())) {
                return stock;
            }
        }
        return null;
    }

    public ArrayList<Customer> getCustomersData() { return Main.customers; }

    public Customer getCustomer(String name) {
        for (Customer c : Main.customers) {
            if (c.getName().toLowerCase().contains(name.toLowerCase())) return c;
        }
        return null;
    }

    public void selectCustomer(Customer customer) {
        this.currentCustomer = customer;
        currentOrder = new Order();
        currentOrder.setCustomer(customer);
        System.out.println("currentUser: " + Main.currentUser);
        if (Main.currentUser != null) {
            System.out.println("username: " + Main.currentUser.getUsername());
            currentOrder.setSeller(Main.currentUser.getUsername());
        }
    }

    public void createNewCustomer(String name, long phone, String address) {
        Customer customer = new Customer(name, phone, address);
        Main.customers.add(customer);
        selectCustomer(customer);
    }

    public Order getOrder() { return currentOrder; }
}