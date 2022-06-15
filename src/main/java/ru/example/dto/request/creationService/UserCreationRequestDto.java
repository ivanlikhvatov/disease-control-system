package ru.example.dto.request.creationService;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import ru.example.dao.entity.user.Gender;
import ru.example.dao.entity.user.Role;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class UserCreationRequestDto {
    @NotBlank
    @Size(max = 20)
    private String login;

    @NotBlank
    @Size(max = 20)
    private String firstname;

    @NotBlank
    @Size(max = 20)
    private String lastname;

    @Size(max = 20)
    private String patronymic;

    @NotNull
    private Gender gender;

    @Valid
    private GroupRequest group;

    @Valid
    private InstituteRequest institute;

    @NotNull
    private Set<Role> roles;

    private List<String> interestedGroupsIdList;

    @AssertTrue
    public boolean isGroup() {
        if (roles == null) {
            return true;
        }

        return !roles.contains(Role.STUDENT) || group != null;
    }

    @AssertTrue
    public boolean isInterestedGroupsIdList() {
        if (roles == null) {
            return true;
        }

        if (roles.contains(Role.CURATOR) || roles.contains(Role.TEACHER)) {
            return !CollectionUtils.isEmpty(interestedGroupsIdList);
        }

        return true;
    }

    @AssertTrue
    public boolean isInstitute() {
        if (roles == null) {
            return true;
        }

        return !roles.contains(Role.DECANAT) || institute != null;
    }
}
