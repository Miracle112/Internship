package com.example.internship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountingController {

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

        String requestPerson = "SELECT * FROM person";
        try {
            fill(requestPerson, personTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        String requestPersonnel = "SELECT personnel (SELECT name_profession FROM practice.professions WHERE id_profession = 1)";
        String requestPersonnel = "SELECT * FROM personnel";
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
                    return new SimpleStringProperty(param.getValue().get(j).toString());
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
