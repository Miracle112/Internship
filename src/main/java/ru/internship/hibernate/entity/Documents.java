package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "documents", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Documents {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_document")
    private Integer idDocument;

    @Column(name = "id_employee")
    private Integer idEmployee;

    @Column(name = "id_document_type")
    private Integer idDocumentType;

    private String number;

    @Column(name = "issue_place")
    private String issuePlace;

    @Column(name = "doc_date")
    private Date docDate;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee", updatable = false, insertable = false)
    private Person personByIdEmployee;

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type",updatable = false, insertable = false)
    private TypeDoc typeDocByIdDocumentType;
}
