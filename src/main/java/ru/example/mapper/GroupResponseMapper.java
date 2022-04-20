package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.group.Group;
import ru.example.dto.response.GroupResponse;

import java.util.List;

@Mapper
public interface GroupResponseMapper {
    GroupResponse map(Group group);

    List<GroupResponse> map(List<Group> groups);
}
