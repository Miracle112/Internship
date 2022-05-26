module com.example.internship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.internship to javafx.fxml;
    exports com.example.internship;
}