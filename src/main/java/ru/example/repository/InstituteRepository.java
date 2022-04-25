package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.institute.Institute;

public interface InstituteRepository extends JpaRepository<Institute, String> {
}
