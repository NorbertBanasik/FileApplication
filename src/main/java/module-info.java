module FileApp {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports fileapp to javafx.graphics;
    exports fileapp.controller to javafx.fxml;
    opens fileapp.controller to javafx.fxml;
}