package com.example.internship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Admin extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminController.class.getResource("admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 635, 333);
        stage.setTitle("Добавление организации и начальника отдела кадров");
        stage.setScene(scene);
        stage.show();
    }
}
