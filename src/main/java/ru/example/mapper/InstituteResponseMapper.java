package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.institute.Institute;
import ru.example.dto.response.InstituteResponse;

import java.util.List;

@Mapper
public interface InstituteResponseMapper {
    InstituteResponse map(Institute institute);

    List<InstituteResponse> map(List<Institute> institutes);
}
