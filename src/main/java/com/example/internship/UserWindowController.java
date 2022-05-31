package com.example.internship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWindowController {

    @FXML
    private ComboBox<String> documents_combo;
    @FXML
    private Button documents_btn;
    @FXML
    private Label documents_label;
    @FXML
    private Button documents_save_btn;
    @FXML
    private TextField document_number_field;
    @FXML
    private Label document_number_label;
    @FXML
    private TextField issue_place_field;
    @FXML
    private Label issue_place_label;
    @FXML
    private TextField doc_date_field;
    @FXML
    private Label doc_date_label;

    @FXML
    private ComboBox<String> social_network_combo;
    @FXML
    private Button social_network_btn;
    @FXML
    private Label social_network_label;
    @FXML
    private Label link_label;
    @FXML
    private TextField link_field;
    @FXML
    private Button social_network_save_btn;

    @FXML
    private Button requestBtn;
    @FXML
    private TextField profession;
    @FXML
    private TextField organization;
    @FXML
    private TextField workMark;
    @FXML
    private Button saveBtn;
    @FXML
    private Button employmentBtn;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private RadioButton noteduRBtn;
    @FXML
    private RadioButton eduRBtn;
    @FXML
    private ComboBox<String> professionBox;
    @FXML
    private ComboBox<String> organizationBox;
    @FXML
    private Button chooseBtn;
    @FXML
    private Text dateFromTxt;
    @FXML
    private Text dateToTxt;
    @FXML
    private Text workMarkTxt;
    @FXML
    private TableView<ObservableList> requestTable;
    private ObservableList<String> profs = FXCollections.observableArrayList();
    private ObservableList<String> orgs = FXCollections.observableArrayList();

    DBHandler dbHandler = new DBHandler();

    @FXML
    private Button canclebtn;

    private final String[] listDocuments = new String[]{"Паспорт", "СНИЛС", "ИНН", "Полис", "Военный билет"};
    private String selectDocument = "";

    private final String[] listSocialNetwork = new String[]{"Номер телефона", "Электронная почта", "Вконтакте",
            "Viber", "Telegram", "WhatsApp", "ICQ", "ОК"};
    private String selectSocialNetwork = "";

    java.sql.Date sqlDate;

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();

        Map<String, Integer> DocumentsType = new HashMap<>();
        DocumentsType.put("Паспорт", 1);
        DocumentsType.put("СНИЛС", 2);
        DocumentsType.put("ИНН", 3);
        DocumentsType.put("Полис", 4);
        DocumentsType.put("Военный билет", 5);

        Map<String, String> DocumentsList = new HashMap<>();
        DocumentsList.put("Паспорт", "0011222222");
        DocumentsList.put("СНИЛС", "00000000000");
        DocumentsList.put("ИНН", "000000000000");
        DocumentsList.put("Полис", "0000111122223333");
        DocumentsList.put("Военный билет", "АА0000000");

        Map<String, String> DocumentsListLabel = new HashMap<>();
        DocumentsListLabel.put("Паспорт", "Серия номер");
        DocumentsListLabel.put("СНИЛС", "Номер");
        DocumentsListLabel.put("ИНН", "Номер");
        DocumentsListLabel.put("Полис", "Номер");
        DocumentsListLabel.put("Военный билет", "Серия номер");

        documents_combo.getItems().addAll(this.listDocuments);
        documents_combo.setValue("Паспорт");
        documents_combo.setOnAction(this::getDocument);

        documents_combo.setOnAction(event -> {
            document_number_label.setText(DocumentsListLabel.get(documents_combo.getValue()));
            document_number_field.setPromptText(DocumentsList.get(documents_combo.getValue()));
        });

        documents_save_btn.setOnAction(event -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date utilDate = format.parse(doc_date_field.getText());
                sqlDate = new java.sql.Date(utilDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!(document_number_field.getText().isEmpty() || issue_place_field.getText().isEmpty()
                    || doc_date_field.getText().isEmpty())) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/practice",
                            "root", "Robbit50!")) {
                        PreparedStatement statement = conn.prepareStatement
                                ("INSERT into documents(id_employee,id_document_type,number,issue_place,doc_date) VALUES(?,?,?,?,?)");
                        statement.setInt(1, AuthorizationController.id_employee);
                        statement.setInt(2, DocumentsType.get(documents_combo.getValue()));
                        statement.setString(3, document_number_field.getText());
                        statement.setString(4, issue_place_field.getText());
                        statement.setDate(5, sqlDate);
                        statement.executeUpdate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Map<String, Integer> SocialNetworkType = new HashMap<>();
        SocialNetworkType.put("Номер телефона", 1);
        SocialNetworkType.put("Электронная почта", 2);
        SocialNetworkType.put("Вконтакте", 3);
        SocialNetworkType.put("Viber", 4);
        SocialNetworkType.put("Telegram", 5);
        SocialNetworkType.put("WhatsApp", 4);
        SocialNetworkType.put("ICQ", 5);
        SocialNetworkType.put("ОК", 6);

        Map<String, String> SocialNetworkList = new HashMap<>();
        SocialNetworkList.put("Номер телефона", "Номер");
        SocialNetworkList.put("Электронная почта", "Адресс");
        SocialNetworkList.put("Вконтакте", "Ссылка");
        SocialNetworkList.put("Viber", "Номер");
        SocialNetworkList.put("Telegram", "Номер");
        SocialNetworkList.put("WhatsApp", "Номер");
        SocialNetworkList.put("ICQ", "Номер");
        SocialNetworkList.put("ОК", "Ссылка");

        social_network_combo.getItems().addAll(this.listSocialNetwork);
        social_network_combo.setValue("Номер телефона");
        social_network_combo.setOnAction(this::getSocialNetwork);

        social_network_combo.setOnAction(event -> {
            link_label.setText(SocialNetworkList.get(social_network_combo.getValue()));
        });


        social_network_save_btn.setOnAction(event -> {
            if (!(link_field.getText().isEmpty())) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/practice",
                            "root", "Robbit50!")) {
                        PreparedStatement statement = conn.prepareStatement
                                ("INSERT into contacts(id_employee,id_contact,contact) VALUES(?,?,?)");
                        statement.setInt(1, AuthorizationController.id_employee);
                        statement.setInt(2, SocialNetworkType.get(social_network_combo.getValue()));
                        statement.setString(3, link_field.getText());
                        statement.executeUpdate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ToggleGroup group = new ToggleGroup();
        eduRBtn.setToggleGroup(group);
        noteduRBtn.setToggleGroup(group);
        eduRBtn.setSelected(true);

        initOrganizations();
        initProfessions();

        eduRBtn.setOnAction(event -> {
            organizationBox.setVisible(true);
            professionBox.setVisible(true);
            profession.setVisible(false);
            organization.setVisible(false);
        });
        noteduRBtn.setOnAction(event -> {
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            profession.setVisible(true);
            organization.setVisible(true);
        });

        saveBtn.setOnAction(event -> {
            if (eduRBtn.isSelected()) {
                if (!(organizationBox.getValue() == null || professionBox.getValue() == null || dateTo.getValue() == null
                        || dateFrom.getValue() == null || workMark.getText().isEmpty())) {
                    try {
                        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement("INSERT into labor_book(id_lb,id_employee,id_organization,id_profession,work_mark,date_from,date_to) VALUES(?,?,?,?,?,?,?)");
                        statement.setInt(1, 1);
                        statement.setInt(2, 1);
                        statement.setInt(3, 1);
                        statement.setInt(4, 5);
                        statement.setString(5, workMark.getText());
                        statement.setDate(6, Date.valueOf(dateFrom.getValue()));
                        statement.setDate(7, Date.valueOf(dateTo.getValue()));
                        statement.executeUpdate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (noteduRBtn.isSelected()) {
                if (!(dateTo.getValue() == null || dateFrom.getValue() == null || workMark.getText().isEmpty()
                        || organization.getText().isEmpty() || profession.getText().isEmpty())) {
                    try {
                        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement("INSERT into labor_book(id_lb,id_employee,not_edu_organization,not_edu_profession,work_mark,date_from,date_to) VALUES(?,?,?,?,?,?,?)");
                        statement.setInt(1, 2);
                        statement.setInt(2, 3);
                        statement.setString(3, organization.getText());
                        statement.setString(4, profession.getText());
                        statement.setString(5, workMark.getText());
                        statement.setDate(6, Date.valueOf(dateFrom.getValue()));
                        statement.setDate(7, Date.valueOf(dateTo.getValue()));
                        statement.executeUpdate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        documents_btn.setOnAction(event -> {
            documents_label.setVisible(true);
            documents_save_btn.setVisible(true);
            document_number_field.setVisible(true);
            document_number_label.setVisible(true);
            issue_place_field.setVisible(true);
            issue_place_label.setVisible(true);
            doc_date_field.setVisible(true);
            doc_date_label.setVisible(true);
            documents_combo.setVisible(true);

            social_network_label.setVisible(false);
            social_network_combo.setVisible(false);
            link_field.setVisible(false);
            link_label.setVisible(false);
            social_network_save_btn.setVisible(false);

            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            chooseBtn.setVisible(false);
            requestTable.setVisible(false);
        });

        social_network_btn.setOnAction(event -> {
            documents_label.setVisible(false);
            documents_save_btn.setVisible(false);
            document_number_field.setVisible(false);
            document_number_label.setVisible(false);
            issue_place_field.setVisible(false);
            issue_place_label.setVisible(false);
            doc_date_field.setVisible(false);
            doc_date_label.setVisible(false);
            documents_combo.setVisible(false);

            social_network_label.setVisible(true);
            social_network_combo.setVisible(true);
            link_field.setVisible(true);
            link_label.setVisible(true);
            social_network_save_btn.setVisible(true);

            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            chooseBtn.setVisible(false);
            requestTable.setVisible(false);
        });

        requestBtn.setOnAction(event -> {
            documents_label.setVisible(false);
            documents_save_btn.setVisible(false);
            document_number_field.setVisible(false);
            document_number_label.setVisible(false);
            issue_place_field.setVisible(false);
            issue_place_label.setVisible(false);
            doc_date_field.setVisible(false);
            doc_date_label.setVisible(false);
            documents_combo.setVisible(false);

            social_network_label.setVisible(false);
            social_network_combo.setVisible(false);
            link_field.setVisible(false);
            link_label.setVisible(false);
            social_network_save_btn.setVisible(false);

            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            chooseBtn.setVisible(true);
            requestTable.setVisible(true);


        });

        employmentBtn.setOnAction(event -> {
            documents_label.setVisible(false);
            documents_save_btn.setVisible(false);
            document_number_field.setVisible(false);
            document_number_label.setVisible(false);
            issue_place_field.setVisible(false);
            issue_place_label.setVisible(false);
            doc_date_field.setVisible(false);
            doc_date_label.setVisible(false);
            documents_combo.setVisible(false);

            social_network_label.setVisible(false);
            social_network_combo.setVisible(false);
            link_field.setVisible(false);
            link_label.setVisible(false);
            social_network_save_btn.setVisible(false);

            chooseBtn.setVisible(false);
            requestTable.setVisible(false);
            dateFromTxt.setVisible(true);
            dateToTxt.setVisible(true);
            workMarkTxt.setVisible(true);
            saveBtn.setVisible(true);
            organization.setVisible(true);
            profession.setVisible(true);
            workMark.setVisible(true);
            dateFrom.setVisible(true);
            dateTo.setVisible(true);
            eduRBtn.setVisible(true);
            noteduRBtn.setVisible(true);
            organizationBox.setVisible(true);
            professionBox.setVisible(true);
        });

        canclebtn.setOnAction(event -> {
            loader.setLocation(getClass().getResource("authorization.fxml"));
            canclebtn.getScene().getWindow().hide();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            assert root != null;
            stage.setScene(new Scene(root));
            stage.show();
        });

        String request = "SELECT id_personal as 'ID', (select organization.short_name from organization where personnel.id_organization = organization.id_organization) as 'Организация', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', date_from as 'Дата создания' from personnel where personnel.job_status = 0;";
        try {
            fill(request, requestTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chooseBtn.setOnAction(actionEvent -> {
            ObservableList selectId = requestTable.getSelectionModel().getSelectedItem();
            Object selectIndex = selectId.get(0);
            String upRequest = "UPDATE personnel SET job_status = 1 WHERE(id_personal = " + selectIndex + ")";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(upRequest);
                prSt.executeUpdate();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            requestTable.getItems().clear();
            String requestPersonnel = "SELECT id_personal as 'ID', (select organization.short_name from organization where personnel.id_organization = organization.id_organization) as 'Организация', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', date_from as 'Дата создания' from personnel where personnel.job_status = 0;";
            try {
                fill(requestPersonnel, requestTable);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }



    private void getDocument(ActionEvent actionEvent1) {
        selectDocument = documents_combo.getValue();
    }

    private void getSocialNetwork(ActionEvent actionEvent1) {
        selectSocialNetwork = social_network_combo.getValue();
    }
    private void initProfessions(){
        try{
            dbHandler.dbConnection = dbHandler.getDbConnection();
            ResultSet resSet = dbHandler.dbConnection.createStatement().executeQuery("SELECT name_profession FROM `professions`");
            while (resSet.next()) {
                profs.add(resSet.getString("name_profession"));
            }
            professionBox.getItems().addAll(profs);
        } catch (SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
    private void initOrganizations(){
        try{
            dbHandler.dbConnection = dbHandler.getDbConnection();
            ResultSet resSet = dbHandler.dbConnection.createStatement().executeQuery("SELECT short_name FROM `organization`");
            while (resSet.next()) {
                orgs.add(resSet.getString("short_name"));
            }
            organizationBox.getItems().addAll(orgs);
        } catch (SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
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