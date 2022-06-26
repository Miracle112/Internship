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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.internship.hibernate.HibernateUtil;
import ru.internship.hibernate.entity.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    Session session = getSession();

    private void fillDocs(Query request) {
        List<Documents> documents = request.getResultList();
        ArrayList<String[]> listmas = new ArrayList<>();
        documents.stream().forEach(d -> {
            String[] element = {d.getPersonByIdEmployee().getFullName(), d.getTypeDocByIdDocumentType().getDocumentName(), d.getNumber(), d.getIssuePlace(), d.getDocDate().toString()};
            listmas.add(element);
        });
        List<String> list = List.of("ФИО", "Тип документа", "Номер", "Место выдачи", "Дата выдачи");
        try {
            fillHql(list, docTable, listmas);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private void fillPerson(Query request) {
        List<Person> person = request.getResultList();
        ArrayList<String[]> listmas = new ArrayList<>();
        person.stream().forEach(p -> {
            String[] element = {p.getIdEmployee().toString(), p.getFullName(), p.getMale().toString(), p.getBirthDate().toString(),
            p.getBirthPlase(), p.getResidenceAddress(), p.getRegistrationAddress()};
            listmas.add(element);
        });
        List<String> list = List.of("ID работника", "ФИО", "Мужчина", "Дата рождения", "Место рождения", "Адрес проживания", "Адрес регистрации");
        try {
            fillHql(list, personTable, listmas);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private void fillLab(Query request) {
        List<LaborBook> laborBook = request.getResultList();
        ArrayList<String[]> listmas = new ArrayList<>();
        laborBook.stream().forEach(l -> {
            String[] element = {l.getPersonByIdEmployee().getFullName(), l.getOrganizationByIdOrganization().getShortName(), l.getProfessionsByIdProfession().getNameProfession(), l.getProfessionsByIdProfession().getSubjectsByIdSubject().getNameSubject(), l.getNotEduProfession(), l.getNotEduOrganization(), l.getWorkMark(), l.getDateFrom().toString(), l.getDateTo().toString()};
            listmas.add(element);
        });
        List<String> list = List.of("ФИО", "Организация", "Профессия", "Предмет", "Не образовательная профессия", "Не образовательная организация", "Заметки", "Дата нанятия", "Дата увольнения");
        try {
            fillHql(list, labTable, listmas);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private void fillPersonnel(Query request) {
        List<Personnel> personnel = request.getResultList();
        ArrayList<String[]> listmas = new ArrayList<>();
        personnel.stream().forEach(p -> {
            String[] element = {p.getIdPersonal().toString(), p.getIfExist(), p.getProfessionsByIdProfession().getNameProfession(), p.getProfessionsByIdProfession().getSubjectsByIdSubject().getNameSubject(),
            p.getJobStatus(), p.getDateFrom().toString()};
            listmas.add(element);
        });
        List<String> list = List.of("Номер заявки", "ФИО", "Профессия", "Предмет", "Статус", "Дата создания");
        try {
            fillHql(list, requestTable, listmas);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        dateNow();
        DBHandler dbHandler = new DBHandler();

        addBtn.setOnAction(event-> {
            addBtn.getScene().getWindow().hide();
            open("addVacancy.fxml", addBtn);
        });

        deleteBtn.setOnAction(event -> {
            // удаление из таблицы
            ObservableList id_del = requestTable.getSelectionModel().getSelectedItem();
            Object id_del_index = id_del.get(0);
            int intValue = Integer.parseInt(id_del_index.toString());
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            Personnel personnel = new Personnel();
            personnel.setIdPersonal(intValue);
            session.delete(personnel);
            session.getTransaction().commit();
            requestTable.getItems().clear();
            Query requestPersonnel = session.createQuery("from Personnel where jobStatus =: jobStatus and idOrganization =: idOrganization");
            requestPersonnel.setParameter("jobStatus", "1");
            requestPersonnel.setParameter("idOrganization", AuthorizationController.id_chief);
            fillPersonnel(requestPersonnel);
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
            Query requestPersonnel = session.createQuery("from Personnel where jobStatus =: jobStatus and idOrganization =: idOrganization");
            requestPersonnel.setParameter("jobStatus", "1");
            requestPersonnel.setParameter("idOrganization", AuthorizationController.id_chief);
            fillPersonnel(requestPersonnel);
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
            Query requestPersonnel = session.createQuery("from Personnel where jobStatus =: jobStatus and idOrganization =: idOrganization");
            requestPersonnel.setParameter("jobStatus", "1");
            requestPersonnel.setParameter("idOrganization", AuthorizationController.id_chief);
            fillPersonnel(requestPersonnel);
        });

        search1.getParent().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String text = search1.getText();
                if (text == "") {
                    fillPerson(session.createQuery("from Person"));
                } else {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.getTransaction().begin();
                    Query query=session.createQuery("from Person where fullName like :fullName");
                    query.setParameter("fullName", "%" + text + "%"); // add search by subString
                    fillPerson(query);
                }
            }
        });

        search2.getParent().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String text = search2.getText();
                if (text == "") {
                    fillDocs(session.createQuery("from Documents"));
                } else {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.getTransaction().begin();
                    Query query=session.createQuery("from Documents where personByIdEmployee.fullName like :fullName");
                    query.setParameter("fullName", "%" + text + "%"); // add search by subString
                    fillDocs(query);
                }
            }
        });

        search3.getParent().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String text = search3.getText();
                if (text == "") {
                    fillLab(session.createQuery("from LaborBook"));
                } else {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.getTransaction().begin();
                    Query query=session.createQuery("from LaborBook where personByIdEmployee.fullName like :fullName");
                    query.setParameter("fullName", "%" + text + "%"); // add search by subString
                    fillLab(query);
                }
            }
        });

        Query requestPersonnel = session.createQuery("from Personnel where jobStatus =: jobStatus and idOrganization =: idOrganization");
        requestPersonnel.setParameter("jobStatus", "1");
        requestPersonnel.setParameter("idOrganization", AuthorizationController.id_chief);
        fillPersonnel(requestPersonnel);

        fillPerson(session.createQuery("from Person"));

        fillDocs(session.createQuery("from Documents"));

        fillLab(session.createQuery("from LaborBook"));
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
        stage.show();
    }

    public Date dateNow() {
        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        date = Date.valueOf(formattedDate);
        return date;
    }
}