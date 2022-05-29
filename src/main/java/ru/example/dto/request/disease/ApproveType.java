package ru.example.dto.request.disease;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.example.dao.entity.group.EducationType;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ApproveType {
    ELECTRONIC_SICK_ID("electronicSickId"),
    SCANNED_CERTIFICATE("scannedCertificate");

    private final String valueFromClient;

    @JsonCreator
    public static ApproveType fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.valueFromClient.equals(name) || item.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
