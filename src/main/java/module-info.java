module com.example.internship {
    requires java.sql;
    requires mysql.connector.java;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.internship to javafx.fxml;
    exports com.example.internship;
}