package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.response.DirectionProfileResponse;
import ru.example.dto.response.GroupResponse;
import ru.example.dto.response.InstituteDirectionResponse;
import ru.example.dto.response.InstituteResponse;
import ru.example.service.DirectionProfileService;
import ru.example.service.GroupService;
import ru.example.service.InstituteDirectionService;
import ru.example.service.InstituteService;

import java.util.List;


//TODO не нужный сервис
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/university")
public class UniversityInformationController {

    private final GroupService groupService;

//    @GetMapping("/groups")
//    public List<GroupResponse> getGroups() {
//        return groupService.getAllGroups();
//    }


}
