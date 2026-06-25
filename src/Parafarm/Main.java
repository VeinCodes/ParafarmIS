package Parafarm;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {
	public static ArrayList<User> users = new ArrayList<>();
	public static ArrayList<Product> products = new ArrayList<>();
	public static ArrayList<Stock> stocks = new ArrayList<>();
	public static ArrayList<Order> orders = new ArrayList<>();
	public static ArrayList<ProviderOrder> providerOrders = new ArrayList<>();
	public static ArrayList<Customer> customers = new ArrayList<>();
	public static ArrayList<Invoice> invoices = new ArrayList<>();
	
	public static OrdersScreen ordersScreen = new OrdersScreen();
	public static NewOrderScreen newOrderScreen = new NewOrderScreen();
    public static InvoiceScreen invoiceScreen = new InvoiceScreen();
    public static StockScreen stockScreen = new StockScreen();
    public static SeeLimitScreen seelimitScreen = new SeeLimitScreen();
    public static SetLimitScreen setLimitScreen = new SetLimitScreen();
    public static AutomaticOrdersScreen automaticOrdersScreen = new AutomaticOrdersScreen();
    public static ManageRightsScreen manageRightsScreen = new ManageRightsScreen();
    public static LogoutScreen logoutScreen = new LogoutScreen();
    public static MainScreen mainScreen = new MainScreen();
    
    public static Stage primaryStage;
    
    public static User currentUser = null;

    @Override
    public void start(Stage stage) {
    	primaryStage = stage;
    	
    	 loadData();
    	 
    	/*User Eleutheriou = new User("eleutheriou", "ele123");
    	User Evaggelou = new User("evaggelou", "eva123");
    	User Pwl1 = new User("pwlhthsh1", "pwlhthsh123");
    	User Andwniou = new User("andwniou", "andwniou123");
    	User Anagnwstou = new User("Anagnwsto", "Anagnwsto123");
    	User Dhmhtriou = new User("Dhmhtriou", "Dhmhtriou123");
    	
    	Eleutheriou.role = "Εξηπηρέτηση";
        users.add(Eleutheriou);
        
        Evaggelou.role = "Εξηπηρέτηση";
        users.add(Evaggelou);
        
        Pwl1.role = "Πωλητής";
        users.add(Pwl1);
        
        Andwniou.role = "Αποθήκη";
        users.add(Andwniou);
        
        Anagnwstou.role = "Αποθήκη";
        users.add(Anagnwstou);
        
        Dhmhtriou.role = "Διεύθυνση";
        users.add(Dhmhtriou);
    	

        
        Product p1 = new Product("Γάζες αποστειρωμένες", 1.20);
        Product p2 = new Product("Επίδεσμος ελαστικός", 2.50);
        Product p3 = new Product("Αιμοστατικό βαμβάκι", 1.80);
        Product p4 = new Product("Αντισηπτικό Betadine", 4.30);
        Product p5 = new Product("Οινόπνευμα 70%", 2.10);
        Product p6 = new Product("Παρακεταμόλη 500mg", 2.50);
        Product p7 = new Product("Ιβουπροφένη 400mg", 3.80);
        Product p8 = new Product("Σετιριζίνη 10mg", 4.50);
        Product p9 = new Product("Βιταμίνη C 500mg", 4.10);
        Product p10 = new Product("Αντηλιακό SPF50", 8.90);
        products.add(p1);
        products.add(p2); 
        products.add(p3); 
        products.add(p4); 
        products.add(p5);
        products.add(p6); 
        products.add(p7); 
        products.add(p8); 
        products.add(p9); 
        products.add(p10);

        stocks.add(new Stock("Γάζες αποστειρωμένες",  30, 120, 1.20, 50));
        stocks.add(new Stock("Επίδεσμος ελαστικός",   20,  75, 2.50, 40));
        stocks.add(new Stock("Αιμοστατικό βαμβάκι",   25,  90, 1.80, 50));
        stocks.add(new Stock("Αντισηπτικό Betadine",   15,  48, 4.30, 30));
        stocks.add(new Stock("Οινόπνευμα 70%",         20,  83, 2.10, 40));
        stocks.add(new Stock("Παρακεταμόλη 500mg",     20,  95, 2.50, 50));
        stocks.add(new Stock("Ιβουπροφένη 400mg",      15,  60, 3.80, 40));
        stocks.add(new Stock("Σετιριζίνη 10mg",        15,  55, 4.50, 40));
        stocks.add(new Stock("Βιταμίνη C 500mg",       25, 104, 4.10, 60));
        stocks.add(new Stock("Αντηλιακό SPF50",        10,  33, 8.90, 25));*/
        
        Main.automaticOrdersScreen.startAutoCheckTimer();
    	
        stage.setTitle("Parafarm");
        stage.setScene(new LoginScreen().getScene(stage));
        
        
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;
            if (newScene.getRoot() instanceof StackPane) return;
            if ("no-logout".equals(newScene.getRoot().getUserData())) return;

            Button logoutBtn = new Button("Αποσύνδεση");
            logoutBtn.setStyle(
                "-fx-background-color: #c62828;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 6 16;"
            );
            logoutBtn.setOnAction(e -> {
                Main.logoutScreen.showPopup(stage);
            });

            javafx.scene.Parent root = newScene.getRoot();

            StackPane stack = new StackPane();
            stack.setAlignment(Pos.BOTTOM_LEFT);
            
            StackPane.setAlignment(root, Pos.TOP_LEFT);
            StackPane.setAlignment(logoutBtn, Pos.BOTTOM_LEFT);
            StackPane.setMargin(logoutBtn, new Insets(0, 0, 16, 16));

            stack.getChildren().addAll(root, logoutBtn);
            
            logoutBtn.setMouseTransparent(false);
            
            newScene.setRoot(stack);
        });
        
        
        
        
        
        
        
        
        
        stage.setOnCloseRequest(e -> saveData());
        stage.show();
    }
    
    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("parafarm_data.ser"))) {
            oos.writeObject(users);
            oos.writeObject(products);
            oos.writeObject(stocks);
            oos.writeObject(orders);
            oos.writeObject(providerOrders);
            oos.writeObject(customers);
            oos.writeObject(invoices);
        } catch (Exception e) {}
    }

    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("parafarm_data.ser"))) {
            users    = (ArrayList<User>)          ois.readObject();
            products = (ArrayList<Product>)       ois.readObject();
            stocks   = (ArrayList<Stock>)         ois.readObject();
            orders   = (ArrayList<Order>)         ois.readObject();
            providerOrders = (ArrayList<ProviderOrder>) ois.readObject();
            customers = (ArrayList<Customer>)     ois.readObject();
            invoices  = (ArrayList<Invoice>)      ois.readObject();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    

    public static void main(String[] args) {
        launch(args);  
        
    }
}