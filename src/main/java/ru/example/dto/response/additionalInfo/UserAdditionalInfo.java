package ru.example.dto.response.additionalInfo;

import lombok.Data;
import ru.example.dto.response.UniversityInfo;
import ru.example.dto.response.additionalInfo.CuratorAndTeacherAdditionalInfo;
import ru.example.dto.response.additionalInfo.DecanatAdditionalInfo;
import ru.example.dto.response.additionalInfo.RectoratAdditionalInfo;

@Data
public class UserAdditionalInfo {

    private UniversityInfo universityInfo;

    private DecanatAdditionalInfo decanatAdditionalInfo;

    private RectoratAdditionalInfo rectoratAdditionalInfo;

    private CuratorAndTeacherAdditionalInfo curatorAndTeacherAdditionalInfo;
}
