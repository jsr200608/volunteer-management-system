
package com.example.volunteer.controller;

import com.example.volunteer.dto.response.ApiResponse;
import com.example.volunteer.dto.response.ServiceTeamResponse;
import com.example.volunteer.service.ServiceTeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class ServiceTeamController {

    private final ServiceTeamService serviceTeamService;

    public ServiceTeamController(ServiceTeamService serviceTeamService) {
        this.serviceTeamService = serviceTeamService;
    }

    @GetMapping
    public ApiResponse<List<ServiceTeamResponse>> getAllTeams() {
        List<ServiceTeamResponse> response = serviceTeamService.getAllTeams();
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceTeamResponse> getTeamById(@PathVariable Long id) {
        ServiceTeamResponse response = serviceTeamService.getTeamById(id);
        return ApiResponse.success(response);
    }
}
