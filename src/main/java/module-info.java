module MSPaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens nl.bve.tekenapp to javafx.fxml;
    exports nl.bve.tekenapp;
}