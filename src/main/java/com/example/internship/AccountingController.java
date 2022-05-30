package com.example.internship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountingController {

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
        DBHandler dbHandler = new DBHandler();

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
            String request = "UPDATE personnel SET job_status = 2 WHERE(id_personal = " + id_del_index + ")";
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
            String request = "UPDATE personnel SET job_status = 0 WHERE(id_personal = " + id_del_index + ")";
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
            if (keyEvent.getCode()== KeyCode.ENTER){
                String text = search1.getText();
                requestTable.getItems().clear();
                String request = "SELECT * FROM person WHERE full_name = '" + text + "'";
                System.out.println(request);
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
        });

        String requestPerson = "SELECT * FROM person";
        try {
            fill(requestPerson, personTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String requestPersonnel = "SELECT id_personal as 'Номер заявки', (select full_name from person where personnel.id_employee = person.id_employee) as 'ФИО', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', job_status as 'Статус', date_from as 'Дата создания', date_to as 'Дата закрытия' FROM practice.personnel where job_status = 1;";
        try {
            fill(requestPersonnel, requestTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String requestDoc = "SELECT * FROM documents";
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
                    if(param.getValue().get(j) == null){
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
}
