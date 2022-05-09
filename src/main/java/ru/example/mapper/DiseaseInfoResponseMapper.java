package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.response.DiseaseInfoResponse;

@Mapper
public interface DiseaseInfoResponseMapper {
    DiseaseInfoResponse map(DiseaseInformation diseaseInformation);
}
