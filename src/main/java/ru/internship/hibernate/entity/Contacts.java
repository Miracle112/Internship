package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contacts", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Contacts {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_contacts")
    private Integer idContacts;

    @Column(name = "id_employee")
    private Integer idEmployee;

    @Column(name = "id_contact")
    private Integer idContact;

    private String contact;

    @OneToOne
    @JoinColumn(name = "id_contacts", referencedColumnName = "id_contact")
    private ContactsDecoding contactsDecodingByIdContacts;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee", updatable = false, insertable = false)
    private Person personByIdEmployee;
}
