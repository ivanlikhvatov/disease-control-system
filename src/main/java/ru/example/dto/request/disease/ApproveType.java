package ru.example.dto.request.disease;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApproveType {
    ELECTRONIC_SICK_ID("electronicSickId", "Электронный больничный"),
    SCANNED_CERTIFICATE("scannedCertificate", "Справка");

    private final String valueFromClient;
    private final String description;

    @JsonCreator
    public static ApproveType fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.valueFromClient.equals(name) || item.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
