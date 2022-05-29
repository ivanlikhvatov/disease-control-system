package ru.example.dto.request.disease;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ApproveDiseaseRequest {

    @NotBlank
    private String id;

    @NotNull
    private ApproveType approveType;

    @NotNull
    private LocalDate dateOfRecovery;

    private String scannedCertificate;
    private String electronicSickId;

    @AssertTrue
    public boolean isScannedCertificateOrElectronicSickId() {
        if (approveType == null) {
            return true;
        }

        if (ApproveType.SCANNED_CERTIFICATE.equals(approveType)) {
            return StringUtils.isNotBlank(scannedCertificate);
        }

        return StringUtils.isNotBlank(electronicSickId);
    }

}
