package ru.example.dto.response;

import lombok.Data;
import ru.example.dto.response.additionalInfo.DecanatAdditionalInfo;
import ru.example.dto.response.additionalInfo.RectoratAdditionalInfo;

@Data
public class UserAdditionalInfo {

    private UniversityInfo universityInfo;

    private DecanatAdditionalInfo decanatAdditionalInfo;

    private RectoratAdditionalInfo rectoratAdditionalInfo;
}
