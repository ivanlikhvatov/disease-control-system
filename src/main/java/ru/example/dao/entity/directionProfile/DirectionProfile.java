package ru.example.dao.entity.directionProfile;

import lombok.Getter;
import lombok.Setter;
import ru.example.dao.entity.instituteDirection.InstituteDirection;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "direction_profile")
public class DirectionProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_direction_id")
    private InstituteDirection instituteDirection;

    private String name;

    private String index;
}
