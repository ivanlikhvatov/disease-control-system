package ru.example.dao.entity.disease;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class DiseaseInformation {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private Boolean isApproved;

    private Boolean isClosed;

    //TODO как хранить больничный

}
