package com.demo.controller;

import com.demo.dto.ApiResponse;
import com.demo.model.Product;
import com.demo.service.AuthService;
import com.demo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品管理接口 — CRUD
 * 除 GET 外均需认证（通过 Authorization header 传递 Bearer token）
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    /**
     * 获取所有产品（公开接口）
     */
    @GetMapping
    public ApiResponse<List<Product>> list() {
        return ApiResponse.ok(productService.findAll());
    }

    /**
     * 获取单个产品
     */
    @GetMapping("/{id}")
    public ApiResponse<Product> getById(@PathVariable String id) {
        return productService.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.fail(404, "产品不存在"));
    }

    /**
     * 新增产品（需登录）
     */
    @PostMapping
    public ApiResponse<Product> create(@RequestBody Product product,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = authService.validateToken(auth);
        if (username == null) return ApiResponse.fail(401, "请先登录");
        return ApiResponse.ok("创建成功", productService.create(product));
    }

    /**
     * 更新产品（需登录）
     */
    @PutMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable String id,
                                       @RequestBody Product updates,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = authService.validateToken(auth);
        if (username == null) return ApiResponse.fail(401, "请先登录");
        return productService.update(id, updates)
                .map(p -> ApiResponse.ok("更新成功", p))
                .orElse(ApiResponse.fail(404, "产品不存在"));
    }

    /**
     * 删除产品（需管理员权限）
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, String>> delete(@PathVariable String id,
                                                    @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = authService.validateToken(auth);
        if (username == null) return ApiResponse.fail(401, "请先登录");
        if (!authService.isAdmin(username)) return ApiResponse.fail(403, "仅管理员可删除");
        boolean ok = productService.delete(id);
        return ok ? ApiResponse.ok("删除成功", Map.of("id", id))
                  : ApiResponse.fail(404, "产品不存在");
    }
}
