package ru.example.dao.entity.group;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.example.dao.entity.directionProfile.DirectionProfile;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "institute_group")
public class Group {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String name;

    //TODO изменить на String
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
