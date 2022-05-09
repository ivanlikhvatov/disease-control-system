package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.disease.DiseaseInformation;

public interface DiseaseInformationRepository extends JpaRepository<DiseaseInformation, String> {
    DiseaseInformation findByUserIdAndIsClosed(String userId, boolean isClosed);
}
