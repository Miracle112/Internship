package com.example.internship;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddVacancyController {

    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<String> boxProfessions;

    @FXML
    private ComboBox<String> boxSubjects;

    @FXML
    private DatePicker createDateBox;
    String professions;
    String subjects;
    DBHandler dbHandler = new DBHandler();
    Date date;
    LocalDate dateLD;
    int id_sub;

    @FXML
    void initialize() {
        setBox("SELECT DISTINCT name_profession FROM professions", "name_profession", boxProfessions);
        setBox("SELECT name_subject FROM subjects", "name_subject", boxSubjects);

        boxSubjects.setDisable(true);
        boxProfessions.setOnAction(event -> {
            professions = boxProfessions.getValue();
            if (professions.equals("Преподаватель предмета")) boxSubjects.setDisable(false);
            else boxSubjects.setDisable(true);
        });
        boxSubjects.setOnAction(event -> {
            subjects = boxSubjects.getValue();
        });
        addBtn.setOnAction(event -> {
            String requestGetIdSub = "SELECT id_subject FROM practice.subjects where name_subject = '" + subjects + " and name_profession = " + professions + "';";
            try {
                ResultSet rs = dbHandler.querry(requestGetIdSub);
                if (rs.next()) {
                    id_sub = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String requestAdd = "INSERT INTO practice.personnel (id_organization, id_profession, job_status, date_from) VALUES " +
                    "("+ AuthorizationController.id_chief + ",(SELECT id_profession FROM practice.professions WHERE id_subject = "
                    + id_sub +" and name_profession = '" + professions + "'),0,'" + dateTransformer() + "');";
            PreparedStatement prSt = null;
            try {
                prSt = dbHandler.getDbConnection().prepareStatement(requestAdd);
                prSt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            addBtn.getScene().getWindow().hide();
            open("/com/example/internship/accounting.fxml", addBtn);
        });

    }

    public void setBox(String request, String nameColumn, ComboBox nameBox) {
        try {
            ObservableList<String> listacombo = FXCollections.observableArrayList();
            ResultSet resultSet = dbHandler.querry(request);
            while (resultSet.next()) {
                listacombo.add(resultSet.getString(nameColumn));
            }
            nameBox.setItems(listacombo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date dateTransformer() {
        dateLD = createDateBox.getValue();
        String formattedDate = dateLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        date = Date.valueOf(formattedDate);
        return date;
    }

    private void open(String path, Button button) {
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
        stage.setTitle("Отдел кадров");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
