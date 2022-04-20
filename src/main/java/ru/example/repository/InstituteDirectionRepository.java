package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.instituteDirection.InstituteDirection;

import java.util.List;

public interface InstituteDirectionRepository extends JpaRepository<InstituteDirection, String> {
    List<InstituteDirection> findAllByInstituteId(String instituteId);
}
