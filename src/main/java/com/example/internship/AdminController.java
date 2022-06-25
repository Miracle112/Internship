package com.example.internship;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import ru.internship.hibernate.HibernateUtil;
import ru.internship.hibernate.entity.Organization;
import ru.internship.hibernate.entity.Person;
import ru.internship.hibernate.entity.Users;

import javax.persistence.TypedQuery;
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
    private TextField bithplaceField; // переменная текстового поля для хранения информации о месте рождения

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
    int idOrganization = 0;
    boolean isConfirm = false;

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
                    Objects.equals(birthDatePicker.getUserData(), "") |
                    Objects.equals(residenceAddressField.getText(), "") |
                    Objects.equals(registrationAddressField.getText(), "") | Objects.equals(loginField.getText(), "") |
                    Objects.equals(passwordField.getText(), "")
                    | (!maleConfirm.isSelected() & !femaleConfirm.isSelected()) | !isConfirm){ // если что-то не так
                secondWarningLabel.setText("Имеется незаполненное поле!");

            } else { // если всё хорошо, происходит запись данных в БД:

                // данные организации
                addOrganization(fullNameField.getText(), shortName.getText(), INNField.getText(),
                            legalAddressField.getText(), actualAddressField.getText(), directorNameField.getText(),
                            phoneNumField.getText(), emailField.getText());

                // данные начальника отдела кадров
                int gender;
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

                addHeadOfHumanResourcesDepartment(FIOField.getText(), gender, birthDate, bithplaceField.getText(),
                            residenceAddressField.getText(), registrationAddressField.getText());

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

    public void addHeadOfHumanResourcesDepartment(String FIO, int gender, Date birth_Date, String birthPlace,
                                                  String residenceAddress,
                                                  String registrationAddress) {

        int idEmployee = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        final TypedQuery<Person> query = session.createQuery("from Person", Person.class);
        for (Person z: query.getResultList()){
            if(query.getResultList() != null){
                System.out.println(z.getIdEmployee());
                idEmployee++;
            }
        }
        idEmployee++;

        System.out.println(idEmployee + "ы");

        // вставка в таблицу "person"

        Person person = new Person();
        person.setFullName(FIO);
        person.setMale(gender);
        person.setBirthDate(birth_Date);
        person.setBirthPlace(birthPlace);
        person.setResidenceAddress(residenceAddress);
        person.setRegistrationAddress(registrationAddress);
        //session.getTransaction().commit();
        session.save(person);

        // вставка в таблицу "users"

        Users users = new Users();
        users.setIdEmployee(person.getIdEmployee());
        users.setEmail(emailField.getText());
        users.setPassword(passwordField.getText());
        users.setRole("Нач. отдела кадров" + "_" + idOrganization);
        session.save(users);
        session.getTransaction().commit();

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

    // метод добавления данных в таблицу "organization"
    public void addOrganization(String fullName, String shortName, String inn, String legalAddress,
                                String actualAddress, String directorName, String phoneNum, String email) {

        idOrganization = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();

        Organization organization = new Organization();
        organization.setFullName(fullName);
        organization.setShortName(shortName);
        organization.setInn(inn);
        organization.setLegalAddress(legalAddress);
        organization.setActualAddress(actualAddress);
        organization.setDirector(directorName);
        organization.setNumber(phoneNum);
        organization.setEmail(email);
        session.save(organization);
        session.getTransaction().commit();
        idOrganization = organization.getIdOrganization();
        System.out.println(idOrganization);
    }
}