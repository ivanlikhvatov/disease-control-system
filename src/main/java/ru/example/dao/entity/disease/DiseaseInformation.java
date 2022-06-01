package ru.example.dao.entity.disease;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.disease.ApproveType;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    private String otherDiseaseInformation;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private DiseaseStatus status;

    private ApproveType approveType;

    private String scannedCertificateFileName;

    private String electronicSickId;

    private String rejectCause;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reject_author_id")
    private User rejectAuthor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approve_author_id")
    private User approveAuthor;

    //TODO удалить
    @Override
    public String toString() {
        return "DiseaseInformation{" +
                "id='" + id + '\'' +
                ", dateOfDisease=" + dateOfDisease +
                ", dateOfRecovery=" + dateOfRecovery +
                '}';
    }
}
