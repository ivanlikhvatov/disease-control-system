package ru.example.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.creationService.UserCreationRequestDto;
import ru.example.dto.response.*;
import ru.example.service.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final UserCreationService creationService;
    private final InstituteService instituteService;
    private final InstituteDirectionService instituteDirectionService;
    private final DirectionProfileService directionProfileService;
    private final GroupService groupService;

    @GetMapping("/users/{studentNumber}")
    public UserInfoDto getUserByStudentNumber(@PathVariable String studentNumber) {
        return userService.getUserInfoDtoByStudentNumber(studentNumber);
    }

    @PostMapping("/users/create")
    public StatusResult createUser(@RequestBody UserCreationRequestDto request) {
        return creationService.createUser(request);
    }

    @GetMapping("/institutes")
    public List<InstituteResponse> getInstitutesInfo() {
        return instituteService.getInstitutesInfo();
    }

    @GetMapping("/institutes/{instituteId}/directions")
    public List<InstituteDirectionResponse> getInstituteDirections(@PathVariable String instituteId) {
        return instituteDirectionService.getInstituteDirections(instituteId);
    }

    @GetMapping("/directions/{directionId}/profiles")
    public List<DirectionProfileResponse> getDirectionProfiles(@PathVariable String directionId) {
        return directionProfileService.getDirectionProfiles(directionId);
    }

    @GetMapping("/profiles/{profileId}/groups")
    public List<GroupResponse> getGroups(@PathVariable String profileId) {
        return groupService.getGroups(profileId);
    }

}
