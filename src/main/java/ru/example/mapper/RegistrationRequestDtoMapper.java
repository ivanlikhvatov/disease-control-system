package ru.example.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.example.domain.Role;
import ru.example.domain.Status;
import ru.example.domain.User;
import ru.example.dto.request.RegistrationRequestDto;

import java.util.Set;

@Mapper
public interface RegistrationRequestDtoMapper {
    User map(RegistrationRequestDto request);

    @AfterMapping
    default void mapAfter(@MappingTarget User user, RegistrationRequestDto request) {
        user.setRoles(Set.of(Role.USER));
        //TODO далее меняем на active, когда подтвердит почту (подумать как быть когда указал не ту почту)
        user.setStatus(Status.NOT_ACTIVE);

    }

}
