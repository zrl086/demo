<template>
  <el-dropdown v-if="authStore.isLoggedIn" trigger="click" @command="handleCommand">
    <div class="user-avatar">
      <div class="avatar-circle">{{ initial }}</div>
      <span class="avatar-name">{{ authStore.user?.nickname }}</span>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="logout" class="logout-item">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth.js";

const router = useRouter();
const authStore = useAuthStore();

const initial = computed(() => {
  const name = authStore.user?.nickname || "A";
  return name.charAt(0).toUpperCase();
});

function handleCommand(command) {
  if (command === "logout") {
    authStore.logout();
    router.push({ name: "login" });
  }
}
</script>

<style scoped>
.user-avatar {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  cursor: pointer;
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-md);
  transition: background-color 0.2s;
}

.user-avatar:hover {
  background-color: var(--color-bg);
}

.avatar-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-primary);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
}

.avatar-name {
  font-size: 13px;
  color: var(--color-text);
}

.logout-item {
  font-size: var(--font-size-small);
  color: var(--color-danger);
}
</style>