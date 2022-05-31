package com.example.internship;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Name;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton male;

    @FXML
    private Button Reg_button;

    @FXML
    private DatePicker birth_date;

    @FXML
    private TextField birth_plase;

    @FXML
    private TextField mail;

    @FXML
    private Label Label;

    @FXML
    private TextField password;

    @FXML
    private Button back;

    @FXML
    private TextField residence_address;

    @FXML
    private TextField registration_address;

    Date date;
    LocalDate dateLD;
    DBHandler dbHandler = new DBHandler();

    public static int id_employee;

    @FXML
    void initialize() {

        back.setOnAction(event -> {
            open("/com/example/internship/authorization.fxml", back);

        });

        Reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dateLD = birth_date.getValue();
                String formattedDate = dateLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                date = Date.valueOf(formattedDate);
                String gender = "";
                if (male.isSelected())
                    gender = "1";
                else
                    gender = "0";

                if(!Name.getText().equals("") && !gender.equals("") && !date.equals("") && !birth_plase.equals("")
                && !registration_address.equals("") && !registration_address.equals("") && !mail.equals("") && !password.equals("")){ // Проверяет введенные даныне на пустоту
                    ResultSet resultCheckUser = dbHandler.checkUser(mail.getText());
                    try {
                        if(resultCheckUser.next()){
                            Label.setText("Пользователь существует");
                        }
                        else{
                            dbHandler.register(Name.getText(), gender, date, birth_plase.getText(),
                                    residence_address.getText(), registration_address.getText());

                            ResultSet resultGetID = dbHandler.getUserID(Name.getText(), gender, date, birth_plase.getText(),
                                    residence_address.getText(), registration_address.getText());
                            try {
                                if(resultGetID.next()){
                                    id_employee = resultGetID.getInt(1); // записывает айди авторизованного пользователя
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            dbHandler.register1(id_employee, mail.getText(), password.getText(), "Пользователь");
                            open("/com/example/internship/authorization.fxml", Reg_button);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Label.setText("Вы что-то не ввели");
                }

            }
        });
    }
    public void open(String path, Button button) {
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
        stage.setTitle("Авторизация");
        stage.show();
    }

}