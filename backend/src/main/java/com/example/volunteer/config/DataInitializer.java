
package com.example.volunteer.config;

import com.example.volunteer.entity.ServiceTeam;
import com.example.volunteer.entity.User;
import com.example.volunteer.repository.ServiceTeamRepository;
import com.example.volunteer.repository.UserRepository;
import com.example.volunteer.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ServiceTeamRepository serviceTeamRepository;
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public DataInitializer(ServiceTeamRepository serviceTeamRepository, 
                          UserRepository userRepository, 
                          PasswordUtil passwordUtil) {
        this.serviceTeamRepository = serviceTeamRepository;
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public void run(String... args) {
        initializeServiceTeams();
        initializeAdminUser();
    }

    private void initializeServiceTeams() {
        if (serviceTeamRepository.count() == 0) {
            List<ServiceTeam> teams = Arrays.asList(
                    new ServiceTeam(null, "爱心家园服务队", "关爱孤寡老人、残障人士，传递温暖", null, "ACTIVE", null, null),
                    new ServiceTeam(null, "绿色环保服务队", "环保宣传、校园美化、节能减排", null, "ACTIVE", null, null),
                    new ServiceTeam(null, "文化之旅服务队", "文化传承、艺术交流、知识普及", null, "ACTIVE", null, null),
                    new ServiceTeam(null, "安全使者服务队", "校园安全、消防宣传、应急协助", null, "ACTIVE", null, null)
            );
            serviceTeamRepository.saveAll(teams);
        }
    }

    private void initializeAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordUtil.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setStatus("ACTIVE");
            userRepository.save(admin);
        }
    }
}
