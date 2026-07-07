// 认证状态管理 — Pinia store
// 策略：自动检测后端 API，也可手动切换模式
//
// 手动控制（在浏览器控制台调用）：
//   authStore.login(username, password)  — 自动尝试后端 → 降级本地
// 配置文件修改：
//   api/index.js    — BASE_URL 改为后端地址
//   data/products.js — 修改默认产品数据
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { loginApi } from "@/api/login.js";

const LOCAL_USERS = {
  admin: { username: "admin", password: "admin123", nickname: "Admin", role: "admin" },
  user:  { username: "user",  password: "user123",  nickname: "User",  role: "user" },
};

export const useAuthStore = defineStore("auth", () => {
  const savedUser = (() => {
    try { return JSON.parse(localStorage.getItem("user")); } catch { return null; }
  })();
  const user = ref(savedUser);
  const isLoggedIn = ref(!!localStorage.getItem("token"));
  const isAdmin = computed(() => user.value?.role === "admin");

  async function login(username, password) {
    try {
      const data = await loginApi(username, password);
      localStorage.setItem("token", data.token);
      const userData = { username: data.username, nickname: data.nickname, avatar: data.avatar || "", role: data.role };
      localStorage.setItem("user", JSON.stringify(userData));
      user.value = userData;
      isLoggedIn.value = true;
      return true;
    } catch {
      const found = Object.values(LOCAL_USERS).find(
        (u) => u.username === username && u.password === password
      );
      if (found) {
        localStorage.setItem("token", "mock-token-" + Date.now());
        const userData = { username: found.username, nickname: found.nickname, avatar: found.avatar || "", role: found.role };
        localStorage.setItem("user", JSON.stringify(userData));
        user.value = userData;
        isLoggedIn.value = true;
        return true;
      }
      return false;
    }
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    user.value = null;
    isLoggedIn.value = false;
  }

  return { user, isLoggedIn, isAdmin, login, logout };
});
