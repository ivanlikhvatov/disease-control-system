package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.dao.entity.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByStudentNumber(String studentNumber);

    User findByEmail(String email);

    User findByActivationCode(String activationCode);
}
