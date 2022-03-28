package ru.example.dao.entity.user;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.example.dao.entity.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String studentNumber;

    private String password;

    private String email;

    private String firstname;

    private String lastname;

    private String patronymic;

    private String activationCode;

    private LocalDateTime activationCodeExpirationDate;

    private Gender gender;

    @LastModifiedDate
    private LocalDateTime updated;

    @CreatedDate
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
