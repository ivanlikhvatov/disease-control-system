package ru.example.dao.entity.group;

import lombok.Getter;
import lombok.Setter;
import ru.example.dao.entity.instituteDirection.InstituteDirection;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "institute_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

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

    private String serialNumber;
}
