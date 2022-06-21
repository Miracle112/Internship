package ru.internship.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {

    // создает соединения
    private static final SessionFactory sessionFactory = initSessionFactory();
    private static SessionFactory initSessionFactory(){
        try{
            return  new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml")).buildSessionFactory();
        }
        catch (Throwable ex){
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public  static SessionFactory getSessionFactory(){
        if (sessionFactory == null){
            initSessionFactory();
        }
        return sessionFactory;
    }

    // закрывает все соединения
    public  static void  close(){
        getSessionFactory().close();
    }
}