module ParafarmIS {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;
    opens Parafarm to javafx.graphics;
}