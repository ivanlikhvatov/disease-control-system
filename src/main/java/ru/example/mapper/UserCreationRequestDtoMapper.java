package ru.example.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.example.dao.entity.Status;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.creationService.UserCreationRequestDto;


@Mapper
public interface UserCreationRequestDtoMapper {
    User map(UserCreationRequestDto request);

    @AfterMapping
    default void mapAfter(@MappingTarget User user, UserCreationRequestDto request){
        user.setStatus(Status.CREATED);
    }
}
