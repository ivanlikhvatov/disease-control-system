package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.disease.Disease;
import ru.example.dto.response.DiseaseResponse;

import java.util.List;

@Mapper
public interface DiseaseResponseMapper {
    DiseaseResponse map(Disease disease);

    List<DiseaseResponse> map(List<Disease> diseases);
}
