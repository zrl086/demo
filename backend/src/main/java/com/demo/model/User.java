package com.demo.model;

/**
 * 用户实体 — 对应 design.md 中的 User 数据模型
 */
public class User {

    private String username;
    private String password;   // 当前阶段明文存储，生产环境需加密
    private String nickname;
    private String avatar;
    private String role;       // admin: 管理员 | user: 普通用户

    public User() {}

    public User(String username, String password, String nickname, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
