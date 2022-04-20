package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.directionProfile.DirectionProfile;

import java.util.List;

public interface DirectionProfileRepository extends JpaRepository<DirectionProfile, String> {
    List<DirectionProfile> findAllByInstituteDirectionId(String instituteDirectionId);
}
