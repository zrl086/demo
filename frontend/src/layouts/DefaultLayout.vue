<template>
  <div class="layout">
    <header class="layout-header">
      <div class="header-inner">
        <router-link to="/" class="header-logo">个人产品管理</router-link>
        <nav class="header-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link v-if="authStore.isAdmin" to="/manage" class="nav-link">管理</router-link>
        </nav>
        <UserAvatar />
      </div>
    </header>
    <main class="layout-main">
      <slot />
    </main>
    <BackToTop />
  </div>
</template>

<script setup>
import UserAvatar from "@/components/UserAvatar.vue";
import BackToTop from "@/components/BackToTop.vue";
import { useAuthStore } from "@/stores/auth.js";

const authStore = useAuthStore();
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--header-height);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  z-index: 100;
}

.header-inner {
  max-width: var(--max-width);
  margin: 0 auto;
  padding: 0 var(--space-md);
  height: 100%;
  display: flex;
  align-items: center;
}

.header-logo {
  font-size: var(--font-size-heading);
  font-weight: 700;
  color: var(--color-primary);
  text-decoration: none;
  margin-right: var(--space-lg);
}

.header-nav {
  display: flex;
  gap: var(--space-md);
  flex: 1;
}

.nav-link {
  font-size: var(--font-size-small);
  color: var(--color-text-secondary);
  text-decoration: none;
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-sm);
  transition: color 0.2s, background-color 0.2s;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: var(--color-primary);
  background-color: var(--color-bg);
}

.layout-main {
  flex: 1;
  padding-top: calc(var(--header-height) + var(--space-xl));
  padding-bottom: var(--space-2xl);
}
</style>