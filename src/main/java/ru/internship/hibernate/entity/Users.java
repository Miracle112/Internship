package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @Column(name = "id_employee")
    private Integer idEmployee;

    private String email;

    private String password;

    private String role;

    @OneToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Person personByIdEmployee;
}
