package ru.internship.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "subjects", schema = "practice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subjects {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_subject")
    private Integer idSubject;

    @Column(name = "name_subject")
    private String nameSubject;

    @OneToMany(mappedBy = "subjectsByIdSubject")
    private Collection<Professions> professionsByIdSubject;
}
