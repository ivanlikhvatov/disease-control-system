package ru.example.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.example.domain.Status;
import ru.example.domain.User;
import ru.example.security.jwt.JwtUser;

@Mapper
public interface JwtUserMapper {

    @Mapping(target = "lastPasswordReset", source = "updated")
    JwtUser map(User user);

    @AfterMapping
    default void mapAfter(@MappingTarget JwtUser jwtUser, User user){
        jwtUser.setEnabled(user.getStatus().equals(Status.ACTIVE));

    }
}
