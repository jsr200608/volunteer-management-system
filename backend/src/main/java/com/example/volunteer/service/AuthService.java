
package com.example.volunteer.service;

import com.example.volunteer.dto.request.ChangePasswordRequest;
import com.example.volunteer.dto.request.LoginRequest;
import com.example.volunteer.dto.response.LoginResponse;
import com.example.volunteer.entity.Member;
import com.example.volunteer.entity.User;
import com.example.volunteer.repository.MemberRepository;
import com.example.volunteer.repository.UserRepository;
import com.example.volunteer.util.JwtUtil;
import com.example.volunteer.util.PasswordUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, MemberRepository memberRepository,
                       PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("用户已禁用");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        Member member = memberRepository.findByUserId(user.getId()).orElse(null);
        String memberName = member != null ? member.getName() : user.getUsername();

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                memberName
        );

        return new LoginResponse(token, userInfo);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        
        if (!passwordUtil.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        user.setPassword(passwordUtil.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("未登录");
        }
        return (User) authentication.getPrincipal();
    }

    public LoginResponse.UserInfo getCurrentUserInfo() {
        User user = getCurrentUser();
        Member member = memberRepository.findByUserId(user.getId()).orElse(null);
        String memberName = member != null ? member.getName() : user.getUsername();
        
        return new LoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                memberName
        );
    }
}
