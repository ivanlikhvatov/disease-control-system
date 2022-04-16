package ru.example.dao.entity.group;

import lombok.Data;
import ru.example.dao.entity.instituteDirection.InstituteDirection;

import javax.persistence.*;

@Data
@Entity
@Table(name = "institute_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_direction_id")
    private InstituteDirection instituteDirection;

    private Integer course;

    @Enumerated(EnumType.STRING)
    private EducationType educationType;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_direction_id")
    private GroupDirection groupDirection;




}
