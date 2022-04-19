package ru.example.dao.entity.instituteDirection;

import lombok.Getter;
import lombok.Setter;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.institute.Institute;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class InstituteDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String shortName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    private Institute institute;
}
