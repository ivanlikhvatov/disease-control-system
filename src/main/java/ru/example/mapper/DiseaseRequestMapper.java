package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.request.disease.DiseaseInformationRequest;

@Mapper
public interface DiseaseRequestMapper {
    DiseaseInformation map(DiseaseInformationRequest diseaseInformationRequest);
}
