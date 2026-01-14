module com.example.hexboardbitchess {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.hexboardbitchess to javafx.fxml;
    exports com.example.hexboardbitchess;
}