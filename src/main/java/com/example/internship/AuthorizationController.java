package com.example.internship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internship.hibernate.HibernateUtil;
import ru.internship.hibernate.entity.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Users users = new Users();
        users.setEmail(emailText);
        users.setPassword(passwordText);
        Query query=session.createQuery("from Users where email=:email and password=:password");
        query.setParameter("email", emailText);
        query.setParameter("password", passwordText);
        Users user =(Users)query.uniqueResult();
        if(user != null){
            id_employee = user.getIdEmployee(); // записывает айди авторизованного пользователя
            if(user.getRole().contains("Нач. отдела кадров")){
                id_chief = Integer.valueOf(user.getRole().substring(user.getRole().indexOf('_') + 1)); // id начальника отдела кадров
                System.out.println(id_chief);
                open("/com/example/internship/accounting.fxml", login, "Отдел кадров");
            }
            else if(user.getRole().equals("Администратор")) open("/com/example/internship/admin.fxml", login, "Администратор");
            else{
                System.out.println(id_employee);
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