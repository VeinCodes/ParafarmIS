package Parafarm;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Label errorLabel;
    private Stage stage;
    //public static ArrayList<User> users = new ArrayList<>();

    public Scene getScene(Stage stage) {
        this.stage = stage;

        Label title = new Label("ParaFarmIS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Σύνδεση στο Σύστημα");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#c8e6c9"));

        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30));
        header.setStyle("-fx-background-color: #2e7d32;");

        Label usernameLabel = new Label("Όνομα Χρήστη");
        usernameLabel.setFont(Font.font("Arial", 13));
        usernameLabel.setTextFill(Color.web("#555"));

        usernameField = new TextField();
        usernameField.setPromptText("Εισάγετε όνομα χρήστη");
        usernameField.setPrefHeight(40);
        usernameField.setMaxWidth(400);
        usernameField.setStyle(
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #ccc;" +
            "-fx-border-width: 1;"
        );

        Label passwordLabel = new Label("Κωδικός Πρόσβασης");
        passwordLabel.setFont(Font.font("Arial", 13));
        passwordLabel.setTextFill(Color.web("#555"));

        passwordField = new PasswordField();
        passwordField.setPromptText("Εισάγετε κωδικό");
        passwordField.setPrefHeight(40);
        passwordField.setMaxWidth(400);
        passwordField.setStyle(
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #ccc;" +
            "-fx-border-width: 1;"
        );

        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        loginBtn = new Button("Σύνδεση");
        loginBtn.setPrefHeight(42);
        loginBtn.setPrefWidth(200);
        loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginBtn.setStyle(
            "-fx-background-color: #2e7d32;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
            "-fx-background-color: #1b5e20;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
            "-fx-background-color: #2e7d32;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));
        loginBtn.setOnAction(e -> handleLogin());

        VBox form = new VBox(10,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            errorLabel, loginBtn
        );
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(40));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(form);
        root.setStyle("-fx-background-color: #f1f8e9;");
        
        root.setUserData("no-logout");
        return new Scene(root, 850, 600);
    }

    private void handleLogin() { //einai theoritika mazi me to button
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Παρακαλώ συμπληρώστε όλα τα πεδία!");
            return;
        }
        String role = userAuthRole(username, password);
        check_role(role);
    }

    public String userAuthRole(String username, String password) {
    	for(User u: Main.users) {
    		if(u.username.equals(username) && u.password.equals(password)) {
    			if(u.role != null) {
    				return u.role;
    			}
    		}
    	}
        return null;
    }


    public void check_role(String role) {
        if (role == null) {
            errorLabel.setText("Λάθος όνομα χρήστη ή κωδικός!");
            return;
        }
        
        for (User u : Main.users) {
            if (u.username.equals(usernameField.getText())) {
                Main.currentUser = u;
                break;
            }
        }
        stage.setScene(Main.mainScreen.getScene(stage));
    }
}