package ru.example.dao.entity.group;

import lombok.Getter;
import lombok.Setter;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.instituteDirection.InstituteDirection;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "institute_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    private String name;

    private Integer course;

    @Enumerated(EnumType.STRING)
    private EducationType educationType;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_profile_id")
    private DirectionProfile directionProfile;

    private String serialNumber;
}
