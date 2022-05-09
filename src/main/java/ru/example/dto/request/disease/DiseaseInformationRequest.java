package ru.example.dto.request.disease;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DiseaseInformationRequest {

    private DiseaseRequest disease;

    private String otherDiseaseInformation;

    @NotNull
    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private Boolean isApproved;

    private Boolean isClosed;

    @AssertTrue
    public boolean isDiseaseOrOtherDiseaseInformation() {
        return disease != null || StringUtils.isNotBlank(otherDiseaseInformation);
    }
}
