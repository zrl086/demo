package com.demo.controller;

import com.demo.dto.ApiResponse;
import com.demo.dto.LoginResponse;
import com.demo.model.LoginRequest;
import com.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口 — POST /api/login
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     * 请求：{ "username": "admin", "password": "admin123" }
     * 响应：{ "code": 200, "data": { "token": "...", "role": "admin" } }
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginInfo info = authService.login(request.getUsername(), request.getPassword());
        if (info == null) {
            return ApiResponse.fail(401, "用户名或密码错误");
        }
        LoginResponse resp = new LoginResponse(info.token, info.username, info.nickname, info.role);
        return ApiResponse.ok("登录成功", resp);
    }
}
