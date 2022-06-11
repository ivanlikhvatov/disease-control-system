package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.department.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {
}
