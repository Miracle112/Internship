package com.example.internship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internship.hibernate.HibernateUtil;
import ru.internship.hibernate.entity.Person;
import ru.internship.hibernate.entity.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField name;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton male;

    @FXML
    private Button reg_button;

    @FXML
    private DatePicker birth_date;

    @FXML
    private TextField birth_place;

    @FXML
    private TextField email;

    @FXML
    private Label label;

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
    public static int id_employee;
    String formattedDate;


    @FXML
    void initialize() {

        back.setOnAction(event -> {
            open("/com/example/internship/authorization.fxml", back);
        });

        reg_button.setOnAction(event -> {
            handle();
        });

    }

    private void handle() {
        dateLD = birth_date.getValue();
        if(dateLD != null){
            formattedDate = dateLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            date = Date.valueOf(formattedDate);
        }
        else {
            label.setText("Вы что-то не ввели");
        }
        int gender = 3;
        if (male.isSelected()) gender = 1;
        else if(female.isSelected()) gender = 0;

        if (!name.getText().equals("") && !birth_place.getText().equals("")
                && !registration_address.getText().equals("") && !registration_address.getText().equals("") &&
                !email.getText().equals("") && !password.getText().equals("") && gender != 3 && dateLD != null) { // Проверяет введенные даныне на пустоту


            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();

            Query query = session.createQuery("from Users where email=:email and password=:password");
            query.setParameter("email", email.getText());
            query.setParameter("password", password.getText());
            Users user =(Users)query.uniqueResult();

            if (user != null) {
                session.close();
                label.setText("Пользователь существует");
            }
            else {

                Person person = new Person();
                person.setFullName(name.getText());
                person.setMale(gender);
                person.setBirthDate(date);
                person.setBirthPlase(birth_place.getText());
                person.setResidenceAddress(residence_address.getText());
                person.setRegistrationAddress(registration_address.getText());
                session.save(person);

                Query queryTwo = session.createQuery("from Person where fullName=:fullname and male=:male and birthPlase=:birthPlace and" +
                        " birthDate=:birthDate and residenceAddress=:residenceAddress and registrationAddress =:registrationAddress");
                queryTwo.setParameter("fullname", name.getText());
                queryTwo.setParameter("male", gender);
                queryTwo.setParameter("birthDate", date);
                queryTwo.setParameter("birthPlace", birth_place.getText());
                queryTwo.setParameter("residenceAddress", residence_address.getText());
                queryTwo.setParameter("registrationAddress", registration_address.getText());
                person = (Person) queryTwo.uniqueResult();
                id_employee = person.getIdEmployee(); // записывает айди авторизованного пользователя

                Users users = new Users();
                users.setIdEmployee(id_employee);
                users.setEmail(email.getText());
                users.setPassword(password.getText());
                users.setRole("Пользователь");
                session.save(users);
                session.getTransaction().commit();

                session.close();
                open("/com/example/internship/authorization.fxml", reg_button);
            }
        } else {
            label.setText("Вы что-то не ввели");
        }
    }

    private void open(String path, Button button) {
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