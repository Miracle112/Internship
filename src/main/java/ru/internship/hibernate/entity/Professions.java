package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "professions", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Professions {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_profession")
    private Integer idProfession;

    @Column(name = "name_profession")
    private String nameProfession;

    @Column(name = "id_subject")
    private Integer idSubject;


    @OneToMany(mappedBy = "professionsByIdProfession")
    private Collection<LaborBook> laborBooksByIdProfession;

    @OneToMany(mappedBy = "professionsByIdProfession")
    private Collection<Personnel> personnelsByIdProfession;

    @ManyToOne
    @JoinColumn(name = "id_subject", referencedColumnName = "id_subject", updatable = false, insertable = false)
    private Subjects subjectsByIdSubject;
}
