package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contacts_decoding", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDecoding {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_contact")
    private Integer idContact;

    private String decoding;

    @OneToOne(mappedBy = "contactsDecodingByIdContacts")
    private Contacts contactsByIdContact;
}
