package com.example.internship;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Objects;

public class AdminController {

    // вкладка "Добавление организации"

    @FXML
    private TextField fullNameField; // переменная текстового поля для хранения полного названия организации

    @FXML
    private TextField shortName; // переменная текстового поля для хранения короткого названия организации

    @FXML
    private TextField INNField; // переменная текстового поля для хранения ИНН организации

    @FXML
    private TextField legalAddressField; // переменная текстового поля для хранения юр. адреса организации

    @FXML
    private TextField actualAddressField; // переменная текстового поля для хранения фактического адреса организации

    @FXML
    private TextField directorNameField; // переменная текстового поля для хранения ФИО директора организации

    @FXML
    private TextField phoneNumField; // переменная текстового поля для хранения номера телефона организации

    @FXML
    private TextField emailField; // переменная текстового поля для хранения эл. почты организации

    @FXML
    private Button checkDataButton; // подтверждение ввода данных организации

    @FXML
    private Label organizationDataAddWarningLabel; // поле предупреждений текушей вкладки


    // вкладка "Добавление данных начальника отдела кадров"

    @FXML
    private TextField FIOField;

    @FXML
    private RadioButton maleConfirm;

    @FXML
    private RadioButton femaleConfirm;

    @FXML
    private TextField bithplaceField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private TextField residenceAddressField;

    @FXML
    private TextField registrationAddressField;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button addButton;

    @FXML
    private Label secondWarningLabel;

    @FXML
    private ToggleGroup sex;

    boolean isConfirm;

    public void initialize() {

        checkDataButton.setOnAction(actionEvent -> {
            isConfirm = false;
            organizationDataAddWarningLabel.setText("");
            if (Objects.equals(fullNameField.getText(), "") | Objects.equals(shortName.getText(), "") |
                    Objects.equals(INNField.getText(), "") |
                    Objects.equals(legalAddressField.getText(), "") |
                    Objects.equals(actualAddressField.getText(), "") |
                    Objects.equals(directorNameField.getText(), "") |
                    Objects.equals(phoneNumField.getText(), "") |
                    Objects.equals(emailField.getText(), "") ){
                organizationDataAddWarningLabel.setText("Одно или несколько текстовых полей не заполнено!");
            } else {
                isConfirm = true;
            }
        });

        addButton.setOnAction(actionEvent -> {
            secondWarningLabel.setText("");
            if(Objects.equals(FIOField.getText(), "") | Objects.equals(bithplaceField.getText(), "") |
                    Objects.equals(birthDatePicker.getUserData(), "") | Objects.equals(bithplaceField.getText(), "") |
                    Objects.equals(residenceAddressField.getText(), "") |
                    Objects.equals(registrationAddressField.getText(), "") | Objects.equals(loginField.getText(), "") |
                    Objects.equals(passwordField.getText(), "")
                    | (!maleConfirm.isSelected() & !femaleConfirm.isSelected() | !isConfirm)){
                secondWarningLabel.setText("Имеется незаполненное поле!");

            }

        });

    }




















}