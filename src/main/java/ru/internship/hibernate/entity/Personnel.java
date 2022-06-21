package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "personnel", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Personnel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_personal")
    private Integer idPersonal;

    @Column(name = "id_organization")
    private Integer idOrganization;

    @Column(name = "id_profession")
    private Integer idProfession;

    @Column(name = "job_status")
    private String jobStatus;

    @Column(name = "id_employee")
    private Integer idEmployee;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "id_organization", referencedColumnName = "id_organization", updatable = false, insertable = false)
    private Organization organizationByIdOrganization;

    @ManyToOne
    @JoinColumn(name = "id_profession", referencedColumnName = "id_profession", updatable = false, insertable = false)
    private Professions professionsByIdProfession;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee", updatable = false, insertable = false)
    private Person personByIdEmployee;
}
