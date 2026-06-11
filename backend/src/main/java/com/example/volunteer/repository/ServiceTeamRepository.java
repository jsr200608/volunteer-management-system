
package com.example.volunteer.repository;

import com.example.volunteer.entity.ServiceTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTeamRepository extends JpaRepository<ServiceTeam, Long> {
    Optional<ServiceTeam> findByName(String name);
    List<ServiceTeam> findByStatus(String status);
}
