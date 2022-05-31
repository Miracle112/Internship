package com.example.internship;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.example.internship.DBHandler.getDbConnection;

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
    private CheckBox checkDataButton; // подтверждение ввода данных организации

    @FXML
    private Label organizationDataAddWarningLabel; // поле предупреждений текушей вкладки


    // вкладка "Добавление данных начальника отдела кадров"

    @FXML
    private TextField FIOField; // переменная текстового поля для хранения ФИО

    @FXML
    private RadioButton maleConfirm; // выбор пола

    @FXML
    private RadioButton femaleConfirm; // выбор пола

    @FXML
    private TextField bithplaceField; // переменная текстового поля для хранения информации о месте рождении

    @FXML
    private DatePicker birthDatePicker; // календарь с возможностью выбора конкретной даты

    @FXML
    private TextField residenceAddressField; // // переменная текстового поля для хранения адреса проживания

    @FXML
    private TextField registrationAddressField;// переменная текстового поля для хранения адреса прописки

    @FXML
    private TextField loginField; // переменная текстового поля для хранения логина

    @FXML
    private TextField passwordField; // переменная текстового поля для хранения пароля

    @FXML
    private Button addButton; // кнопка добавления

    @FXML
    private Label secondWarningLabel; // поле для вывода ошибок

    boolean isConfirm;

    public void initialize() {

        checkDataButton.setOnAction(actionEvent -> { // кнопка проверки введённых данных 1й вкладки

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
                editable(false);
            }
            if (!checkDataButton.isSelected()){
                editable(true);
            }
        });

        addButton.setOnAction(actionEvent -> { // кнопка "Добавить" для добавления данных из текстовых полей в БД
            secondWarningLabel.setText("");
            if(Objects.equals(FIOField.getText(), "") | Objects.equals(bithplaceField.getText(), "") |
                    Objects.equals(birthDatePicker.getUserData(), "") | Objects.equals(bithplaceField.getText(), "") |
                    Objects.equals(residenceAddressField.getText(), "") |
                    Objects.equals(registrationAddressField.getText(), "") | Objects.equals(loginField.getText(), "") |
                    Objects.equals(passwordField.getText(), "")
                    | (!maleConfirm.isSelected() & !femaleConfirm.isSelected() | !isConfirm)){ // если что-то не так
                secondWarningLabel.setText("Имеется незаполненное поле!");

            } else { // если всё хорошо, происходит запись данных в БД:

                // данные организации
                try {
                    addOrganization(fullNameField.getText(), shortName.getText(), INNField.getText(),
                            legalAddressField.getText(), actualAddressField.getText(), directorNameField.getText(),
                            phoneNumField.getText(), emailField.getText());
                } catch (SQLException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            // данные начальника отдела кадров
            byte gender;
            if(maleConfirm.isSelected()){
                gender = 1;
                femaleConfirm.setSelected(false);
            } else {
                gender = 0;
            }
            LocalDate localDate;
            localDate = birthDatePicker.getValue();
            String dateFormatted = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date birthDate = Date.valueOf(dateFormatted);

            try {
                addHeadOfHumanResourcesDepartment(FIOField.getText(), gender, birthDate, bithplaceField.getText(),
                        residenceAddressField.getText(), registrationAddressField.getText());
            } catch (SQLException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });
    }


    public void editable(boolean status) { // метод блокировки и разблокировки полей 1-й вкладки
        fullNameField.setEditable(status);
        shortName.setEditable(status);
        INNField.setEditable(status);
        legalAddressField.setEditable(status);
        actualAddressField.setEditable(status);
        directorNameField.setEditable(status);
        phoneNumField.setEditable(status);
        emailField.setEditable(status);
    }

    public void addHeadOfHumanResourcesDepartment(String FIO, byte gender, Date birth_Date, String bithplace,
                                                  String residenceAddress,
                                                  String registrationAddress) throws SQLException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException {

        int idEmployee = 0;
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/practice",
                "root", "Mabe.131852")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM person");
            while (resultSet.next()) {
                idEmployee++;
            }
        }
        idEmployee++;

        // вставка в таблицу "person"

        String request = "INSERT INTO person (id_employee, full_name, male, birth_date, birth_plase, " +
                "residence_address, registration_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        DBHandler.getDbConnection().prepareStatement(request);
        PreparedStatement preparedStatement;

        try {
            preparedStatement = DBHandler.getDbConnection().prepareStatement(request);
            preparedStatement.setInt(1, idEmployee);
            preparedStatement.setString(2, FIO);
            preparedStatement.setByte(3, gender);
            preparedStatement.setDate(4, birth_Date);
            preparedStatement.setString(5, bithplace);
            preparedStatement.setString(6, residenceAddress);
            preparedStatement.setString(7, registrationAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }

        // вставка в таблицу "users"
        request = "INSERT users (id_employee, email, password) VALUES(?, ?, ?)";

        DBHandler.getDbConnection().prepareStatement(request);
        PreparedStatement preparedStatementTwo;

        try {
            preparedStatementTwo = getDbConnection().prepareStatement(request);
            preparedStatementTwo.setInt(1, idEmployee);
            preparedStatementTwo.setString(2, emailField.getText());
            preparedStatementTwo.setString(3, passwordField.getText());
            preparedStatementTwo.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }


        fullNameField.clear();
        shortName.clear();
        INNField.clear();
        legalAddressField.clear();
        actualAddressField.clear();
        directorNameField.clear();
        phoneNumField.clear();
        emailField.clear();
        checkDataButton.fire();
        FIOField.clear();
        bithplaceField.clear();
        birthDatePicker.getEditor().clear();
        residenceAddressField.clear();
        registrationAddressField.clear();
        loginField.clear();
        passwordField.clear();
        femaleConfirm.fire();
        maleConfirm.fire();
        isConfirm = false;
    }

    public void addOrganization(String fullName, String shortName, String inn, String legalAddress,
                                String actualAddress, String directorName, String phoneNum, String email)
            throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        int idOrganization = 0;
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/practice",
                "root", "Mabe.131852")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM organization");
            while (resultSet.next()) {
                idOrganization++;
            }
        }
        idOrganization++;

        String secRequest = "INSERT INTO organization (id_organization, full_name, short_name, INN, legal_address, " +
                "actual_address, director, number, email) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DBHandler.getDbConnection().prepareStatement(secRequest);
        PreparedStatement preparedStatement;

        try {
            preparedStatement = getDbConnection().prepareStatement(secRequest);
            preparedStatement.setInt(1, idOrganization);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, shortName);
            preparedStatement.setString(4, inn);
            preparedStatement.setString(5, legalAddress);
            preparedStatement.setString(6, actualAddress);
            preparedStatement.setString(7, directorName);
            preparedStatement.setString(8, phoneNum);
            preparedStatement.setString(9, email);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}