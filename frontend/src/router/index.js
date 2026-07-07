// Vue Router 路由配置
// 使用 hash 模式，兼容 GitHub Pages 等静态托管
import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/views/HomeView.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue"),
  },
  {
    path: "/manage",
    name: "manage",
    component: () => import("@/views/ManageView.vue"),
    meta: {
      requiresAuth: true,   // 需要登录
      requiresAdmin: true,  // 仅管理员可访问
    },
  },
];

const router = createRouter({ history: createWebHashHistory(), routes });

// 从 localStorage 读取当前用户信息（避免循环依赖 Pinia）
function getUser() {
  try { return JSON.parse(localStorage.getItem("user")); } catch { return null; }
}

// 全局导航守卫：校验登录状态与角色权限
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  // 需要登录但未登录 → 跳转登录页
  if (to.meta.requiresAuth && !token) return next({ name: "login" });

  // 需要管理员但非管理员 → 跳转首页
  if (to.meta.requiresAdmin) {
    const user = getUser();
    if (!user || user.role !== "admin") return next({ name: "home" });
  }

  // 已登录访问登录页 → 跳转首页
  if (to.name === "login" && token) return next({ name: "home" });

  next();
});

export default router;