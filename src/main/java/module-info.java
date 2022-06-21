module com.example.internship {
    requires java.sql;
    requires mysql.connector.java;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.persistence;
    requires static lombok;


    opens ru.internship.hibernate to org.hibernate.orm.core;
    exports ru.internship.hibernate to org.hibernate.orm.core;

    opens com.example.internship to org.hibernate.orm.core, javafx.fxml;
    exports com.example.internship to org.hibernate.orm.core, javafx.graphics;

    opens ru.internship.hibernate.entity to org.hibernate.orm.core;
    exports ru.internship.hibernate.entity to org.hibernate.orm.core;





}