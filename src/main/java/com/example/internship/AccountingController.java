package com.example.internship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccountingController {
    Date date;

    @FXML
    private Button acceptBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button rejectBtn;

    @FXML
    private TableView<ObservableList> docTable;

    @FXML
    private TableView<ObservableList> labTable;

    @FXML
    private TableView<ObservableList> personTable;

    @FXML
    private TableView<ObservableList> requestTable;

    @FXML
    private TextField search1;

    @FXML
    private TextField search2;

    @FXML
    private TextField search3;

    @FXML
    void initialize() {
        dateNow();
        DBHandler dbHandler = new DBHandler();

        addBtn.setOnAction(actionEvent -> open("addVacancy.fxml", addBtn));

        deleteBtn.setOnAction(event -> {
            // удаление из таблицы
            ObservableList id_del = requestTable.getSelectionModel().getSelectedItem();
            Object id_del_index = id_del.get(0);
            String request = "DELETE FROM personnel WHERE(id_personal = " + id_del_index + ")";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(request);
                prSt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            requestTable.getItems().clear();
            String requestPersonnel = "SELECT id_personal as 'Номер заявки', (select full_name from person where personnel.id_employee = person.id_employee) as 'ФИО', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', job_status as 'Статус', date_from as 'Дата создания', date_to as 'Дата закрытия' FROM practice.personnel where job_status = 1;";
            try {
                fill(requestPersonnel, requestTable);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        acceptBtn.setOnAction(event -> {
            // изменение job_status на 2
            ObservableList id_del = requestTable.getSelectionModel().getSelectedItem();
            Object id_del_index = id_del.get(0);
            String request = "UPDATE personnel SET job_status = 2, date_to = '" + date + "' WHERE(id_personal = " + id_del_index + ")";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(request);
                prSt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            requestTable.getItems().clear();
            String requestPersonnel = "SELECT id_personal as 'Номер заявки', (select full_name from person where personnel.id_employee = person.id_employee) as 'ФИО', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', job_status as 'Статус', date_from as 'Дата создания', date_to as 'Дата закрытия' FROM practice.personnel where job_status = 1;";
            try {
                fill(requestPersonnel, requestTable);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        rejectBtn.setOnAction(event -> {
            // изменение job_status на 0
            ObservableList id_del = requestTable.getSelectionModel().getSelectedItem();
            Object id_del_index = id_del.get(0);
            String request = "UPDATE personnel SET job_status = 0, id_employee = NULL WHERE(id_personal = " + id_del_index + ")";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(request);
                prSt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            requestTable.getItems().clear();
            String requestPersonnel = "SELECT id_personal as 'Номер заявки', (select full_name from person where personnel.id_employee = person.id_employee) as 'ФИО', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', job_status as 'Статус', date_from as 'Дата создания', date_to as 'Дата закрытия' FROM practice.personnel where job_status = 1;";
            try {
                fill(requestPersonnel, requestTable);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        search1.getParent().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String text = search1.getText();
                if (text == "") {
                    String requestPerson = "SELECT id_employee as 'ID работника', full_name as 'ФИО', male as 'Мужчина', birth_date as 'День рождения', birth_plase as 'Место рождения', residence_address as 'Адрес проживания', registration_address as 'Адрес регистрации' FROM practice.person;";
                    personTable.getItems().clear();
                    try {
                        PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(requestPerson);
                        prSt.executeQuery();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fill(requestPerson, personTable);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    String request = "SELECT id_employee as 'ID работника', full_name as 'ФИО', male as 'Мужчина', birth_date as 'День рождения', birth_plase as 'Место рождения', residence_address as 'Адрес проживания', registration_address as 'Адрес регистрации' FROM practice.person WHERE full_name = '" + text + "'";
                    personTable.getItems().clear();
                    try {
                        PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(request);
                        prSt.executeQuery();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fill(request, personTable);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        search2.getParent().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String text = search2.getText();
                if (text == "") {
                    String requestDoc = "SELECT (select full_name from person where documents.id_employee = person.id_employee) as 'ФИО', (select document_name from type_doc where type_doc.id_document_type = documents.id_document_type) as 'Тип документа', number as 'Номер', issue_place as 'Место выдачи', doc_date as 'Дата выдачи' FROM practice.documents;";
                    personTable.getItems().clear();
                    try {
                        PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(requestDoc);
                        prSt.executeQuery();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fill(requestDoc, docTable);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    String request = "SELECT(select full_name from person where documents.id_employee = person.id_employee) as 'ФИО', (select document_name from type_doc where type_doc.id_document_type = documents.id_document_type) as 'Тип документа', number as 'Номер', issue_place as 'Место выдачи', doc_date as 'Дата выдачи' FROM practice.documents where (select full_name from person where documents.id_employee = person.id_employee) = '" + text + "';";
                    personTable.getItems().clear();
                    try {
                        PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(request);
                        prSt.executeQuery();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fill(request, docTable);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        String requestPerson = "SELECT id_employee as 'ID работника', full_name as 'ФИО', male as 'Мужчина', birth_date as 'День рождения', birth_plase as 'Место рождения', residence_address as 'Адрес проживания', registration_address as 'Адрес регистрации' FROM practice.person;";
        try {
            fill(requestPerson, personTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String requestPersonnel = "SELECT id_personal as 'Номер заявки', (select full_name from person where personnel.id_employee = person.id_employee) as 'ФИО', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', job_status as 'Статус', date_from as " +
                "'Дата создания', date_to as 'Дата закрытия' FROM practice.personnel where job_status = 1 AND id_organization = "+ AuthorizationController.id_chief +";";
        try {
            fill(requestPersonnel, requestTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String requestDoc = "SELECT (select full_name from person where documents.id_employee = person.id_employee) as 'ФИО', (select document_name from type_doc where type_doc.id_document_type = documents.id_document_type) as 'Тип документа', number as 'Номер', issue_place as 'Место выдачи', doc_date as 'Дата выдачи' FROM practice.documents;";
        try {
            fill(requestDoc, docTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String requestLab = "SELECT * FROM labor_book";
        try {
            fill(requestLab, labTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void fill(String querry, TableView<ObservableList> personalDataTable1) throws SQLException {
        personalDataTable1.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        DBHandler dbHandler = new DBHandler();
        ResultSet resultSet = dbHandler.querry(querry);
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnLabel(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty("");
                    } else {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                }
            });
            personalDataTable1.getColumns().addAll(col);
        }
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            data.add(row);
        }
        personalDataTable1.setItems(data);
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
        stage.setTitle("Добавление");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public Date dateNow() {
        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        date = Date.valueOf(formattedDate);
        return date;
    }
}