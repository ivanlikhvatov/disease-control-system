package ru.example.dao.entity.instituteDirection;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.example.dao.entity.department.Department;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.institute.Institute;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class InstituteDirection {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String fullName;

    private String shortName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
