package com.demo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品实体 — 对应 design.md 中的 Product 数据模型
 */
public class Product {

    private String id;
    private String name;          // 产品名称，最长 50 字符
    private String description;   // 产品简介，最长 200 字符
    private String url;           // 跳转链接（完整 URL）
    private String icon;          // 图标 URL 或 emoji，默认 🔗
    private List<String> tags = new ArrayList<>();
    private String status;        // online: 上线 | developing: 开发中
    private int priority;         // 排序优先级，数值越小越靠前，默认 99
    private Instant createdAt;
    private Instant updatedAt;

    public Product() {}

    public Product(String id, String name, String url, String status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.priority = 99;
        this.icon = "🔗";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
