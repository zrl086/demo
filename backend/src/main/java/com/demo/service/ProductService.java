package com.demo.service;

import com.demo.model.Product;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品管理服务 — CRUD 操作
 * 当前阶段使用内存存储
 * 后续可切换为 JPA Repository + 数据库
 */
@Service
public class ProductService {

    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public ProductService() {
        // 初始化示例数据
        addSample("a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                "示例项目", "这是一个示例产品，展示卡片效果",
                "https://github.com", "📦", List.of("示例"), "online", 1);
        addSample("b2c3d4e5-f6a7-8901-bcde-f12345678901",
                "待上线项目", "正在开发中的新功能",
                "https://example.com", "🚧", List.of("WIP"), "developing", 2);
    }

    private void addSample(String id, String name, String desc, String url,
                           String icon, List<String> tags, String status, int priority) {
        Product p = new Product(id, name, url, status);
        p.setDescription(desc);
        p.setIcon(icon);
        p.setTags(tags);
        p.setPriority(priority);
        p.setCreatedAt(Instant.parse("2026-01-01T00:00:00Z"));
        p.setUpdatedAt(Instant.now());
        store.put(id, p);
    }

    /** 获取所有产品，按优先级升序排列 */
    public List<Product> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparingInt(Product::getPriority))
                .toList();
    }

    /** 根据 ID 查找 */
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /** 新增产品 */
    public Product create(Product product) {
        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        if (product.getStatus() == null) product.setStatus("online");
        if (product.getPriority() == 0) product.setPriority(99);
        if (product.getIcon() == null || product.getIcon().isEmpty()) product.setIcon("🔗");
        store.put(product.getId(), product);
        return product;
    }

    /** 更新产品（部分字段） */
    public Optional<Product> update(String id, Product updates) {
        Product existing = store.get(id);
        if (existing == null) return Optional.empty();

        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getDescription() != null) existing.setDescription(updates.getDescription());
        if (updates.getUrl() != null) existing.setUrl(updates.getUrl());
        if (updates.getIcon() != null) existing.setIcon(updates.getIcon());
        if (updates.getTags() != null) existing.setTags(updates.getTags());
        if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
        if (updates.getPriority() > 0) existing.setPriority(updates.getPriority());
        existing.setUpdatedAt(Instant.now());

        return Optional.of(existing);
    }

    /** 删除产品 */
    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}
