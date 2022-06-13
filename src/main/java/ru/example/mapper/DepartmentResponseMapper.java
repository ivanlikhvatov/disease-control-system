package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.department.Department;
import ru.example.dto.response.DepartmentResponse;

import java.util.List;

@Mapper
public interface DepartmentResponseMapper {

    DepartmentResponse map(Department department);

    List<DepartmentResponse> map(List<Department> departments);
}
