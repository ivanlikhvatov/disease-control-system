package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;

@Mapper
public interface DiseaseRequestMapper {
    DiseaseInformation map(AddDiseaseInformationRequest addDiseaseInformationRequest);

    DiseaseInformation map(EditDiseaseInformationRequest editDiseaseInformationRequest);
}
