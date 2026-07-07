// API 客户端 — 基于浏览器原生 fetch，零外部依赖
// 接入后端：取消注释 baseURL，指向 Spring Boot 后端地址
// 纯前端模式：保持 baseURL 为 null，所有 API 调用自动降级为本地模式

const BASE_URL = "http://localhost:8080";

/**
 * 通用请求封装
 * @param {string} path - API 路径，如 "/api/products"
 * @param {object} options - fetch 配置（method, body 等）
 * @returns {Promise<object>} 解析后的 JSON 响应
 */
async function request(path, options = {}) {
  const url = `${BASE_URL}${path}`;

  // 自动附带 token
  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };
  const token = localStorage.getItem("token");
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(url, { ...options, headers });

  // 解析 JSON 响应体
  let data;
  try {
    data = await res.json();
  } catch {
    throw new Error(`服务器返回非 JSON 响应 (${res.status})`);
  }

  if (!res.ok) {
    throw new Error(data.message || `请求失败 (${res.status})`);
  }

  return data;
}

// 对外暴露方法（镜像 axios 风格）
export const api = {
  get:    (path)              => request(path),
  post:   (path, body)        => request(path, { method: "POST", body: JSON.stringify(body) }),
  put:    (path, body)        => request(path, { method: "PUT",  body: JSON.stringify(body) }),
  delete: (path)              => request(path, { method: "DELETE" }),
};

/**
 * 检测后端是否可用
 * @returns {Promise<boolean>}
 */
export async function checkBackend() {
  try {
    await api.get("/api/products");
    return true;
  } catch {
    return false;
  }
}
