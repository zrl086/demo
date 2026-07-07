<template>
  <DefaultLayout>
    <div class="container">
      <h1 class="page-title">我的产品</h1>
      <p class="page-desc">一些我做的小项目和工具</p>
      <div class="card-grid">
        <ProductCard
          v-for="product in productsStore.visibleProducts"
          :key="product.id"
          :product="product"
        />
      </div>
      <p class="empty-hint" v-if="productsStore.visibleProducts.length === 0">
        还没有产品<template v-if="authStore.isAdmin">，去
        <router-link to="/manage">管理页</router-link>
        添加吧</template>。
      </p>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { onMounted } from "vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ProductCard from "@/components/ProductCard.vue";
import { useProductsStore } from "@/stores/products.js";
import { useAuthStore } from "@/stores/auth.js";

const productsStore = useProductsStore();
const authStore = useAuthStore();

onMounted(() => {
  productsStore.checkApi();
});
</script>

<style scoped>
.page-desc {
  color: var(--color-text-secondary);
  margin-top: var(--space-sm);
  margin-bottom: var(--space-xl);
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-lg);
}

@media (max-width: 1024px) {
  .card-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .card-grid {
    grid-template-columns: 1fr;
  }
}

.empty-hint {
  text-align: center;
  color: var(--color-text-secondary);
  padding: var(--space-2xl) 0;
}
</style>
