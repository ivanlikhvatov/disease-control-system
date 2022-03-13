package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByStudentNumber(String studentNumber);
}
