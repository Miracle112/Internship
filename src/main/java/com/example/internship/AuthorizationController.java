package com.example.internship;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorizationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button close;

    @FXML
    private TextField email;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private Button registration;

    @FXML
    private Label text;

    public static Integer id_employee;
    public static Integer id_chief;

    @FXML
    void initialize() {
        login.setOnAction(actionEvent -> {
            String emailText = email.getText().trim();
            String passwordText = password.getText().trim();
            if (!emailText.equals("") && !passwordText.equals("")) {
                try {
                    loginUser(emailText, passwordText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(!emailText.equals("")) {
                text.setText("Пароль пустой");
            }
            else if(!passwordText.equals("")) {
                text.setText("Логин пустой");
            }
            else{
                text.setText("Даныне пусты");
            }
        });
        close.setOnAction(actionEvent -> close.getScene().getWindow().hide());
        registration.setOnAction(actionEvent -> open("/com/example/internship/registration.fxml", registration, "Регистрация"));
    }

    private void loginUser(String emailText, String passwordText) throws SQLException {
        DBHandler dbHandler = new DBHandler();
        String checkUser = "SELECT * FROM users WHERE email= '" + emailText + "'" + " AND password='" + passwordText + "'";
        String getID = "SELECT id_employee FROM users WHERE email= '" + emailText + "'" + " AND password='" + passwordText + "'";
        String getRole = "SELECT role FROM users WHERE email= '" + emailText + "'" + " AND password='" + passwordText + "'";
        ResultSet resultCheckUser = dbHandler.querry(checkUser);
        ResultSet resultGetID = dbHandler.querry(getID);
        ResultSet resultRole = dbHandler.querry(getRole);
        if(resultCheckUser.next() && resultGetID.next() && resultRole.next()){
            id_employee = resultGetID.getInt(1); // записывает айди авторизованного пользователя
            if(resultRole.getString(1).contains("Бухгалтер")){

                id_chief = Integer.valueOf(resultRole.getString(1).substring(resultRole.getString
                        (1).indexOf('_') + 1));// айди бухгалтера
                open("/com/example/internship/accounting.fxml", login, "Отдел кадров");
            }
            else if(resultRole.getString(1).equals("Администратор")) open("/com/example/internship/admin.fxml", login, "Администратор");
            else{
                open("/com/example/internship/userWindow.fxml", login, "Поиск ваканский");
            }
        }
        else text.setText("Не найден");
    }

     private void open(String path, Button button, String title) {
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene((new Scene(root)));
        //stage.getIcons().add(new Image("file:src/main/resources/picture/icon.ico")); Если нужна иконка
        stage.setTitle(title);
        stage.show();
    }

}