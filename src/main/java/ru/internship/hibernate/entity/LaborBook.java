package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "labor_book", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LaborBook {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_lb")
    private Integer idLb;

    @Column(name = "id_employee")
    private Integer idEmployee;

    @Column(name = "id_organization")
    private Integer idOrganization;

    @Column(name = "id_profession")
    private Integer idProfession;

    @Column(name = "not_edu_organization")
    private String notEduOrganization;

    @Column(name = "not_edu_profession")
    private String notEduProfession;

    @Column(name = "work_mark")
    private String workMark;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee", updatable = false, insertable = false)
    private Person personByIdEmployee;

    @ManyToOne
    @JoinColumn(name = "id_organization", referencedColumnName = "id_organization", updatable = false, insertable = false)
    private Organization organizationByIdOrganization;

    @ManyToOne
    @JoinColumn(name = "id_profession", referencedColumnName = "id_profession", updatable = false, insertable = false)
    private Professions professionsByIdProfession;
}
