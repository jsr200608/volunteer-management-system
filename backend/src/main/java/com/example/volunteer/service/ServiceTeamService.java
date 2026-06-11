
package com.example.volunteer.service;

import com.example.volunteer.dto.response.ServiceTeamResponse;
import com.example.volunteer.entity.ServiceTeam;
import com.example.volunteer.repository.ServiceTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTeamService {

    private final ServiceTeamRepository serviceTeamRepository;

    public ServiceTeamService(ServiceTeamRepository serviceTeamRepository) {
        this.serviceTeamRepository = serviceTeamRepository;
    }

    public List<ServiceTeamResponse> getAllTeams() {
        return serviceTeamRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ServiceTeamResponse getTeamById(Long id) {
        ServiceTeam team = serviceTeamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服务队不存在"));
        return convertToResponse(team);
    }

    private ServiceTeamResponse convertToResponse(ServiceTeam team) {
        ServiceTeamResponse response = new ServiceTeamResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setDescription(team.getDescription());
        response.setIcon(team.getIcon());
        response.setStatus(team.getStatus());
        return response;
    }
}
