package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.disease.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, String> {
}
