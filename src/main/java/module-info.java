module com.example.internship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.internship to javafx.fxml;
    exports com.example.internship;
}