
package com.example.volunteer.controller;

import com.example.volunteer.dto.request.ChangePasswordRequest;
import com.example.volunteer.dto.request.LoginRequest;
import com.example.volunteer.dto.response.ApiResponse;
import com.example.volunteer.dto.response.LoginResponse;
import com.example.volunteer.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("登出成功", null);
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ApiResponse.success("密码修改成功", null);
    }

    @GetMapping("/me")
    public ApiResponse<LoginResponse.UserInfo> getCurrentUser() {
        LoginResponse.UserInfo userInfo = authService.getCurrentUserInfo();
        return ApiResponse.success(userInfo);
    }
}
