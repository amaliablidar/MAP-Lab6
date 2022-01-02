module com{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;



    opens com to javafx.fxml;
    exports com;
}