package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.group.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String> {
    List<Group> findAllByDirectionProfileId(String directionProfileId);
}
