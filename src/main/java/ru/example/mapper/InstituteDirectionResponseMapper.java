package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dto.response.InstituteDirectionResponse;

import java.util.List;

@Mapper
public interface InstituteDirectionResponseMapper {
    InstituteDirectionResponse map(InstituteDirection instituteDirection);

    List<InstituteDirectionResponse> map(List<InstituteDirection> instituteDirection);
}
