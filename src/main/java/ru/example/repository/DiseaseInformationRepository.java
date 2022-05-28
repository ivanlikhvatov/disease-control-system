package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;

public interface DiseaseInformationRepository extends JpaRepository<DiseaseInformation, String> {
    DiseaseInformation findByUserIdAndStatus(String userId, DiseaseStatus status);
}
