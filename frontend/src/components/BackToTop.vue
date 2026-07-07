<template>
  <button class="back-to-top" :class="{ visible: show }" @click="scrollToTop" title="返回顶部">
    &uarr;
  </button>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const show = ref(false);

function onScroll() {
  show.value = window.scrollY > 300;
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: "smooth" });
}

onMounted(() => window.addEventListener("scroll", onScroll));
onUnmounted(() => window.removeEventListener("scroll", onScroll));
</script>

<style scoped>
.back-to-top {
  position: fixed;
  bottom: var(--space-xl);
  right: var(--space-xl);
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  background: var(--color-primary);
  color: #fff;
  font-size: 22px;
  cursor: pointer;
  opacity: 0;
  transform: translateY(10px);
  transition: opacity 0.3s, transform 0.3s, background-color 0.2s;
  pointer-events: none;
  z-index: 50;
}

.back-to-top.visible {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.back-to-top:hover {
  background: var(--color-primary-light);
}
</style>