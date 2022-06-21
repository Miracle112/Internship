package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "organization", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_organization")
    private Integer idOrganization;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "INN")
    private String inn;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "actual_address")
    private String actualAddress;

    private String director;

    private String number;

    private String email;

    @OneToMany(mappedBy = "organizationByIdOrganization")
    private Collection<LaborBook> laborBooksByIdOrganization;

    @OneToMany(mappedBy = "organizationByIdOrganization")
    private Collection<Personnel> personnelsByIdOrganization;
}
