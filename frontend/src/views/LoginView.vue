<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
      <div class="bg-circle bg-circle-3"></div>
    </div>

    <div class="login-card">
      <!-- 顶部图标 -->
      <div class="login-icon">
        <div class="icon-circle">
          <span class="icon-emoji">&#x1F680;</span>
        </div>
      </div>

      <div class="login-header">
        <h1>个人产品管理</h1>
        <p>登录以管理你的产品和项目</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            size="large"
            autocomplete="username"
          >
            <template #prefix>
              <span class="input-icon">&#x1F464;</span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            placeholder="密码"
            size="large"
            autocomplete="current-password"
          >
            <template #prefix>
              <span class="input-icon">&#x1F512;</span>
            </template>
          </el-input>
        </el-form-item>
        <p v-if="errorMsg" class="login-error">{{ errorMsg }}</p>
        <el-form-item>
          <el-button
            class="login-btn"
            type="primary"
            native-type="submit"
            :loading="loading"
            size="large"
            round
          >
            {{ loading ? "登录中..." : "登 录" }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth.js";

const router = useRouter();
const authStore = useAuthStore();

const formRef = ref(null);
const errorMsg = ref("");
const loading = ref(false);

const form = reactive({
  username: "",
  password: "",
});

const rules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

async function handleLogin() {
  errorMsg.value = "";
  const valid = await formRef.value.validate().catch(() => false);
  if (!valid) return;

  loading.value = true;
  try {
    const success = await authStore.login(form.username, form.password);
    if (success) {
      router.push({ name: "home" });
    } else {
      errorMsg.value = "用户名或密码错误";
    }
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #F8F7FF 0%, #EDE9FE 40%, #DDD6FE 100%);
  overflow: hidden;
}

/* 背景装饰圆 */
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
}

.bg-circle-1 {
  width: 400px;
  height: 400px;
  background: var(--color-primary);
  top: -120px;
  right: -80px;
  animation: float 8s ease-in-out infinite;
}

.bg-circle-2 {
  width: 300px;
  height: 300px;
  background: var(--color-primary-light);
  bottom: -100px;
  left: -60px;
  animation: float 10s ease-in-out infinite reverse;
}

.bg-circle-3 {
  width: 200px;
  height: 200px;
  background: #C4B5FD;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 12s ease-in-out infinite 2s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-30px) rotate(10deg); }
}

/* 登录卡片 */
.login-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: var(--space-2xl);
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: var(--radius-lg);
  box-shadow: 0 20px 60px rgba(124, 92, 252, 0.15),
              0 4px 16px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.6);
  animation: cardIn 0.6s ease-out;
}

@keyframes cardIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 图标 */
.login-icon {
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-lg);
}

.icon-circle {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(124, 92, 252, 0.3);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 8px 24px rgba(124, 92, 252, 0.3); }
  50% { box-shadow: 0 8px 36px rgba(124, 92, 252, 0.5); }
}

.icon-emoji {
  font-size: 32px;
}

/* 标题 */
.login-header {
  text-align: center;
  margin-bottom: var(--space-xl);
}

.login-header h1 {
  font-size: var(--font-size-subtitle);
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: var(--space-xs);
}

.login-header p {
  font-size: var(--font-size-small);
  color: var(--color-text-secondary);
}

/* 表单 */
.login-form {
  margin-bottom: 0;
}

.input-icon {
  font-size: 16px;
  display: flex;
  align-items: center;
}

.login-error {
  color: var(--color-danger);
  font-size: var(--font-size-small);
  text-align: center;
  margin-bottom: var(--space-sm);
  animation: shake 0.4s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-6px); }
  75% { transform: translateX(6px); }
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: var(--font-size-body);
  letter-spacing: 4px;
}

/* el-input 微调 */
:deep(.el-input--large .el-input__wrapper) {
  border-radius: var(--radius-sm);
  box-shadow: 0 0 0 1px var(--color-border) inset;
}
:deep(.el-input--large .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--color-primary-light) inset;
}
</style>