package ru.example.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.Status;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.RegistrationRequestDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Mapper
public abstract class RegistrationRequestDtoMapper {

    public abstract User map(RegistrationRequestDto request, Long codeDuration);

    @AfterMapping
    public void mapAfter(@MappingTarget User user, RegistrationRequestDto request, Long codeDuration) {
        LocalDateTime codeExpiration = LocalDateTime.now().plusHours(codeDuration);

        user.setRoles(Set.of(Role.USER));
        user.setStatus(Status.NOT_ACTIVE);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setActivationCodeExpirationDate(codeExpiration);
    }

}
