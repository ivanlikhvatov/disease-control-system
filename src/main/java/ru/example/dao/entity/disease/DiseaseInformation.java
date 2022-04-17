package ru.example.dao.entity.disease;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
//@EqualsAndHashCode(exclude = "nameAttributeInThisClassWithManyToOne")
@Entity
public class DiseaseInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private Boolean isApproved;

    private Boolean isClosed;

    //TODO как хранить больничный

}
