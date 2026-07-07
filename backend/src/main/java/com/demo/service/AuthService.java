package com.demo.service;

import com.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务 — 用户校验与 Token 管理
 * 当前阶段使用内存用户列表 + Mock Token
 * 后续可集成 Spring Security + JWT
 */
@Service
public class AuthService {

    // 内存用户存储（后续迁移至数据库）
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();
    // Token → 用户名映射（后续替换为 JWT 解析）
    private static final Map<String, String> TOKENS = new ConcurrentHashMap<>();

    static {
        USERS.put("admin", new User("admin", "admin123", "Admin", "admin"));
        USERS.put("user",  new User("user",  "user123",  "User",  "user"));
    }

    /**
     * 登录校验
     * @return LoginInfo（含 mock token），失败返回 null
     */
    public LoginInfo login(String username, String password) {
        User user = USERS.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        String token = "mock-jwt-" + UUID.randomUUID();
        TOKENS.put(token, username);
        return new LoginInfo(token, user.getUsername(), user.getNickname(), user.getRole());
    }

    /**
     * Token 校验
     * @return 用户名，无效返回 null
     */
    public String validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) return null;
        return TOKENS.get(token.substring(7));
    }

    /**
     * 是否为管理员
     */
    public boolean isAdmin(String username) {
        User user = USERS.get(username);
        return user != null && "admin".equals(user.getRole());
    }

    /**
     * 登录成功返回的内部记录
     */
    public static class LoginInfo {
        public final String token;
        public final String username;
        public final String nickname;
        public final String role;

        public LoginInfo(String token, String username, String nickname, String role) {
            this.token = token;
            this.username = username;
            this.nickname = nickname;
            this.role = role;
        }
    }
}
