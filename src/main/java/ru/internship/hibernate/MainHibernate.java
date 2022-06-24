package ru.internship.hibernate;

import org.hibernate.Session;
import ru.internship.hibernate.entity.Subjects;
import ru.internship.hibernate.entity.Users;

import javax.persistence.TypedQuery;

public class MainHibernate {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();

        Users users = new Users();
        users.setIdEmployee(7);
        users.setEmail("pimkin");
        users.setPassword("andrey");
        users.setRole("admin");
        session.save(users);
        final TypedQuery<Subjects> query = session.createQuery("from Subjects where idSubject = 0", Subjects.class);
        for (Subjects o : query.getResultList()) {
            o.getProfessionsByIdSubject().stream().forEach(professions -> {
                System.out.println(professions.getNameProfession()
                        + " " + o.getNameSubject());
            });
        }

        session.getTransaction().commit();

        session.close();
        HibernateUtil.close();
    }


}
