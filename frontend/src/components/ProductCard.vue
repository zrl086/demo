<template>
  <a
    :href="isOnline ? product.url : undefined"
    :target="isOnline ? '_blank' : undefined"
    :rel="isOnline ? 'noopener' : undefined"
    class="product-card"
    :class="{ 'card-offline': isOffline }"
  >
    <div class="card-icon">{{ product.icon || "🔗" }}</div>
    <div class="card-header">
      <h3 class="card-name">{{ product.name }}</h3>
      <span class="card-status" :class="statusClass">{{ statusText }}</span>
    </div>
    <p class="card-desc" v-if="product.description">{{ product.description }}</p>
  </a>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  product: { type: Object, required: true },
});

const isOnline = computed(() => props.product.status === "online");
const isOffline = computed(() => props.product.status === "offline");

const statusMap = {
  online:     { text: "上线",   class: "status-online" },
  developing: { text: "开发中", class: "status-developing" },
  offline:    { text: "下线",   class: "status-offline" },
};

const statusText = computed(() => statusMap[props.product.status]?.text || props.product.status);
const statusClass = computed(() => statusMap[props.product.status]?.class || "");
</script>

<style scoped>
.product-card {
  display: block;
  padding: var(--space-lg);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  text-decoration: none;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.product-card:not(.card-offline):hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
  border-color: var(--color-primary-light);
}

.card-offline {
  opacity: 0.75;
}

.card-icon { font-size: 32px; margin-bottom: var(--space-md); }

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-sm);
  margin-bottom: var(--space-sm);
}

.card-name { font-size: var(--font-size-heading); color: var(--color-text); margin: 0; }

.card-status {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
  flex-shrink: 0;
}

.status-online { background: rgba(16, 185, 129, 0.12); color: var(--color-success); }
.status-developing { background: rgba(245, 158, 11, 0.12); color: var(--color-warning); }
.status-offline { background: rgba(107, 114, 128, 0.12); color: var(--color-text-secondary); }

.card-desc {
  font-size: var(--font-size-small);
  color: var(--color-text-secondary);
  line-height: var(--line-height);
}
</style>
