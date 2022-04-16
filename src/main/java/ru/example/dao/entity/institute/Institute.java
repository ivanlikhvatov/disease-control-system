package ru.example.dao.entity.institute;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Institute {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String shortName;
}
