package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "type_doc", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeDoc {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_document_type")
    private Integer idDocumentType;

    @Column(name = "document_name")
    private String documentName;

    @OneToMany(mappedBy = "typeDocByIdDocumentType")
    private Collection<Documents> documentsByIdDocumentType;
}
