package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "person", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_employee")
    private Integer idEmployee;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "male")
    private Integer male;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "birth_plase")
    private String birthPlace;

    @Column(name = "residence_address")
    private String residenceAddress;

    @Column(name = "registration_address")
    private String registrationAddress;


    @OneToMany(mappedBy = "personByIdEmployee")
    private Collection<Contacts> contactsByIdEmployee;

    @OneToMany(mappedBy = "personByIdEmployee")
    private Collection<Documents> documentsByIdEmployee;

    @OneToMany(mappedBy = "personByIdEmployee")
    private Collection<LaborBook> laborBooksByIdEmployee;

    @OneToMany(mappedBy = "personByIdEmployee")
    private Collection<Personnel> personnelsByIdEmployee;

    @OneToOne(mappedBy = "personByIdEmployee")
    private Users usersByIdEmployee;
}
