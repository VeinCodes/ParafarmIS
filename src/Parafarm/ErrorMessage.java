package Parafarm;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ErrorMessage {
    
    private VBox root;
    private Stage stage;
    private TextArea errorArea = new TextArea();
    
    public Scene getScene(Stage stage) {
        this.stage = stage;
        root = new VBox();
        return new Scene(root, 400, 200);
    }
   
    
    public void printError(String message, VBox container) {
        Label errorLabel = new Label("⚠ " + message);
        errorLabel.setStyle(
            "-fx-text-fill: #c62828;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 12;"
        );
        container.getChildren().add(errorLabel);
    }
}