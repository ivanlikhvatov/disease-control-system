package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.response.DiseaseInfoResponse;

import java.util.List;

@Mapper
public interface DiseaseInfoResponseMapper {

    List<DiseaseInfoResponse> map(List<DiseaseInformation> diseaseInformationList);

    DiseaseInfoResponse map(DiseaseInformation diseaseInformation);
}
