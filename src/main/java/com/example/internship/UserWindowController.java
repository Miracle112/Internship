package com.example.internship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internship.hibernate.HibernateUtil;
import ru.internship.hibernate.entity.Contacts;
import ru.internship.hibernate.entity.Documents;
import ru.internship.hibernate.entity.LaborBook;
import ru.internship.hibernate.entity.Personnel;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserWindowController {

    @FXML private ComboBox<String> documents_combo;
    @FXML private Button documents_save_btn;
    @FXML private TextField document_number_field;
    @FXML private Label document_number_label;
    @FXML private TextField issue_place_field;
    @FXML private ComboBox<String> social_network_combo;
    @FXML private Label link_label;
    @FXML private TextField link_field;
    @FXML private Button social_network_save_btn;
    @FXML private TextField profession;
    @FXML private DatePicker datePicker;
    @FXML private TextField organization;
    @FXML private TextField workMark;
    @FXML private Button saveBtn;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private RadioButton noteduRBtn;
    @FXML private RadioButton eduRBtn;
    @FXML private ComboBox<String> professionBox;
    @FXML private ComboBox<String> organizationBox;
    @FXML private Button chooseBtn;
    @FXML private TableView<ObservableList> requestTable;
    @FXML
    private Button canclebtn;
    private final String[] listDocuments = new String[]{"Паспорт", "СНИЛС", "ИНН", "Полис", "Военный билет"};
    private String selectDocument = "";
    private final String[] listSocialNetwork = new String[]{"Номер телефона", "Электронная почта", "Вконтакте",
            "Viber", "Telegram", "WhatsApp", "ICQ", "ОК"};

    private final String[] listProfessions = new String[]{"Директор", "Завуч", "Библиотекарь",
            "Преподователь начальных классов", "Преподаватель русского", "Преподаватель математики", "Преподаватель истории", "Преподаватель физкультуры",
            "Преподаватель биологии", "Преподаватель химии","Преподаватель физики","Преподаватель географии","Преподаватель музыки","Преподаватель ОБЖ",
            "Преподаватель литературы", "Преподаватель английского", "Преподаватель ИЗО","Преподаватель технологии"};

    private final String[] listOrganization = new String[]{"ПГУ", "Лицей №55", "Школа №56"};

    private String selectProfession = "";

    private String selectOrganization = "";

    LocalDate dateLD;
    Date date;
    String formattedDate;


    private void fillVacancy(Query request) {

        List<Personnel> personnel = request.getResultList();
        ArrayList<String[]> listmas = new ArrayList<>();
        personnel.stream().forEach(p -> {
            String[] element = {p.getOrganizationByIdOrganization().getShortName(), p.getProfessionsByIdProfession().getNameProfession(),
                    p.getProfessionsByIdProfession().getSubjectsByIdSubject().getNameSubject(), p.getDateFrom().toString()};
            listmas.add(element);
        });
        List<String> list = List.of("Организация", "Профессия", "Предмет", "Дата создания");
        try {
            fillHql(list, requestTable, listmas);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void initialize() {


        FXMLLoader loader = new FXMLLoader();
        Session session = HibernateUtil.getSessionFactory().openSession();

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
            dateLD = datePicker.getValue();

            if (dateLD != null) {
                formattedDate = dateLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                date = Date.valueOf(formattedDate);
            }
            if (!(document_number_field.getText().isEmpty() || issue_place_field.getText().isEmpty())) {
                session.getTransaction().begin();
                Documents documents = new Documents();
                documents.setIdEmployee(AuthorizationController.id_employee);
                documents.setIdDocumentType(DocumentsType.get(documents_combo.getValue()));
                documents.setNumber(document_number_field.getText());
                documents.setIssuePlace(issue_place_field.getText());
                documents.setDocDate(date);
                session.save(documents);
                session.getTransaction().commit();
            }
        });

        Map<String, Integer> SocialNetworkType = new HashMap<>();
        SocialNetworkType.put("Номер телефона", 1);SocialNetworkType.put("Электронная почта", 2);
        SocialNetworkType.put("Вконтакте", 3);SocialNetworkType.put("Viber", 4);
        SocialNetworkType.put("Telegram", 5);SocialNetworkType.put("WhatsApp", 4);
        SocialNetworkType.put("ICQ", 5);SocialNetworkType.put("ОК", 6);

        Map<String, String> SocialNetworkList = new HashMap<>();
        SocialNetworkList.put("Номер телефона", "Номер");SocialNetworkList.put("Электронная почта", "Адресс");
        SocialNetworkList.put("Вконтакте", "Ссылка");SocialNetworkList.put("Viber", "Номер");
        SocialNetworkList.put("Telegram", "Номер");SocialNetworkList.put("WhatsApp", "Номер");
        SocialNetworkList.put("ICQ", "Номер");SocialNetworkList.put("ОК", "Ссылка");

        Map<String, Integer> ProfessionsList = new HashMap<>();
        ProfessionsList.put("Директор", 0);ProfessionsList.put("Завуч", 0);ProfessionsList.put("Библиотекарь", 0);
        ProfessionsList.put("Преподаватель начальных классов", 0);ProfessionsList.put("Преподаватель русского", 1);
        ProfessionsList.put("Преподаватель математики", 2);ProfessionsList.put("Преподаватель истории", 3);
        ProfessionsList.put("Преподаватель физкультуры", 4);ProfessionsList.put("Преподаватель биологии", 5);
        ProfessionsList.put("Преподаватель химии", 6);ProfessionsList.put("Преподаватель физики", 7);
        ProfessionsList.put("Преподаватель географии", 8);ProfessionsList.put("Преподаватель музыки", 9);
        ProfessionsList.put("Преподаватель ОБЖ", 10);ProfessionsList.put("Преподаватель литературы", 11);
        ProfessionsList.put("Преподаватель английского", 12);ProfessionsList.put("Преподаватель ИЗО", 13);
        ProfessionsList.put("Преподаватель технологии", 14);

        Map<String, Integer> OrganizationList = new HashMap<>();
        OrganizationList.put("ПГУ", 1);OrganizationList.put("Лицей №55", 2);OrganizationList.put("Школа №56", 3);

        social_network_combo.getItems().addAll(this.listSocialNetwork);
        social_network_combo.setValue("Номер телефона");
        social_network_combo.setOnAction(event -> {
            link_label.setText(SocialNetworkList.get(social_network_combo.getValue()));
        });


        professionBox.getItems().addAll(this.listProfessions);
        professionBox.setValue("Директор");
        professionBox.setOnAction(this::getProfession);

        organizationBox.getItems().addAll(this.listOrganization);
        organizationBox.setValue("ПГУ");
        organizationBox.setOnAction(this::getOrganization);

        social_network_save_btn.setOnAction(event -> {
            if (!(link_field.getText().isEmpty())) {
                session.getTransaction().begin();
                Contacts contacts = new Contacts();
                contacts.setIdEmployee(AuthorizationController.id_employee);
                contacts.setIdContact(SocialNetworkType.get(social_network_combo.getValue()));
                contacts.setContact(link_field.getText());
                session.save(contacts);
                session.getTransaction().commit();
            }
        });

        organization.setVisible(false);
        profession.setVisible(false);

        ToggleGroup group = new ToggleGroup();
        eduRBtn.setToggleGroup(group);
        noteduRBtn.setToggleGroup(group);
        eduRBtn.setSelected(true);

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
                if (!(organizationBox.getValue() == null && professionBox.getValue() == null && dateTo.getValue() == null
                        && dateFrom.getValue() == null && workMark.getText().isEmpty())) {
                    session.getTransaction().begin();
                    LaborBook laborBook = new LaborBook();
                    laborBook.setIdEmployee(AuthorizationController.id_employee);
                    laborBook.setIdOrganization(OrganizationList.get(selectOrganization));
                    laborBook.setIdProfession(ProfessionsList.get(selectProfession));
                    laborBook.setWorkMark(workMark.getText());
                    laborBook.setDateFrom(Date.valueOf(dateFrom.getValue()));
                    laborBook.setDateTo(Date.valueOf(dateTo.getValue()));
                    session.save(laborBook);
                    session.getTransaction().commit();

                }
            }
            else if (noteduRBtn.isSelected()) {
                if (!(dateTo.getValue() == null || dateFrom.getValue() == null || workMark.getText().isEmpty()
                        || organization.getText().isEmpty() || profession.getText().isEmpty())) {
                    session.getTransaction().begin();
                    LaborBook laborBook = new LaborBook();
                    laborBook.setIdEmployee(AuthorizationController.id_employee);
                    laborBook.setNotEduOrganization( organization.getText());
                    laborBook.setNotEduProfession(profession.getText());
                    laborBook.setWorkMark(workMark.getText());
                    laborBook.setDateFrom(Date.valueOf(dateFrom.getValue()));
                    laborBook.setDateTo(Date.valueOf(dateTo.getValue()));
                    session.save(laborBook);
                    session.getTransaction().commit();
                }
            }
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


      Query requestPersonnel = session.createQuery("from Personnel where jobStatus =: jobStatus");
      requestPersonnel.setParameter("jobStatus", "0");
      fillVacancy(requestPersonnel);


        chooseBtn.setOnAction(actionEvent -> {
            ObservableList selectId = requestTable.getSelectionModel().getSelectedItem();
            Object selectIndex = selectId.get(0);
            session.getTransaction().begin();
            Personnel personnel = new Personnel();
            personnel.setJobStatus("1");
            personnel.setIdEmployee((Integer) selectIndex);
            session.update(personnel);
            session.getTransaction().commit();

            requestTable.getItems().clear();
            fillVacancy(requestPersonnel);
        });
    }

    private void getDocument(ActionEvent actionEvent1) {selectDocument = documents_combo.getValue();}
    private void getProfession(ActionEvent actionEvent1) {
        selectProfession = professionBox.getValue();
    }
    private void getOrganization(ActionEvent actionEvent1) {
        selectOrganization = organizationBox.getValue();
    }

    public static void fillHql(List<String> labels, TableView<ObservableList> table, ArrayList<String[]> args) {
        table.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < labels.size(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(labels.get(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty("");
                    } else {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                }
            });
            table.getColumns().addAll(col);
        }
        for (int j = 0; j < args.size(); j++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 0; i < args.get(j).length; i++) {
                row.add(args.get(j)[i]);
            }
            data.add(row);
        }
        table.setItems(data);
    }

}