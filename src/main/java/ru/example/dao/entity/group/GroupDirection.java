package ru.example.dao.entity.group;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "institute_group_direction")
public class GroupDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String index;
}
