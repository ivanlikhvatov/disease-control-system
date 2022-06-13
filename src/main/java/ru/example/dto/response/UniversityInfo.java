package ru.example.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UniversityInfo {
    private List<GroupResponse> groups;

    private List<InstituteResponse> institutes;

    private List<DepartmentResponse> departments;

}
