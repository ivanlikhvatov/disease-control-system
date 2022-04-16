package ru.example.dao.entity.instituteDirection;

import lombok.Data;
import ru.example.dao.entity.institute.Institute;

import javax.persistence.*;

@Data
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
