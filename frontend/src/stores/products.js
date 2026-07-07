// 产品数据状态管理 — Pinia store
// 策略：自动检测后端 API，也可手动切换模式
//
// 手动控制（在浏览器控制台调用）：
//   productsStore.forceLocal()  — 强制本地模式
//   productsStore.forceApi()    — 强制切换后端并同步数据
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { defaultProducts } from "@/data/products.js";
import * as productsApi from "@/api/products.js";

export const useProductsStore = defineStore("products", () => {
  const products = ref([...defaultProducts]);
  const useApi = ref(false);
  const apiChecked = ref(false);

  // 仅上线产品，按优先级升序
  const onlineProducts = computed(() =>
    products.value
      .filter((p) => p.status === "online")
      .sort((a, b) => a.priority - b.priority)
  );

  // 所有产品，按优先级升序
  const allProducts = computed(() =>
    [...products.value].sort((a, b) => a.priority - b.priority)
  );

  // 首页可见产品（排除下线状态）
  const visibleProducts = computed(() =>
    products.value
      .filter((p) => p.status !== "offline")
      .sort((a, b) => a.priority - b.priority)
  );

  /** 启动时自动检测后端 */
  async function checkApi() {
    if (apiChecked.value) return;
    apiChecked.value = true;
    try {
      const data = await productsApi.fetchProducts();
      products.value = data;
      useApi.value = true;
    } catch {
      useApi.value = false;
    }
  }

  /** 强制切换到本地模式 */
  function forceLocal() {
    useApi.value = false;
    products.value = [...defaultProducts];
  }

  /** 强制切换到 API 模式 */
  async function forceApi() {
    try {
      const data = await productsApi.fetchProducts();
      products.value = data;
      useApi.value = true;
      apiChecked.value = true;
    } catch (e) {
      console.warn("[products] 后端不可用:", e.message);
    }
  }

  async function addProduct(product) {
    if (useApi.value) {
      try {
        const created = await productsApi.createProduct(product);
        products.value.push(created);
        return;
      } catch { useApi.value = false; }
    }
    products.value.push(product);
  }

  async function updateProduct(id, updates) {
    if (useApi.value) {
      try {
        const updated = await productsApi.updateProduct(id, updates);
        const i = products.value.findIndex((p) => p.id === id);
        if (i !== -1) products.value[i] = updated;
        return;
      } catch { useApi.value = false; }
    }
    const i = products.value.findIndex((p) => p.id === id);
    if (i !== -1)
      products.value[i] = { ...products.value[i], ...updates, updatedAt: new Date().toISOString() };
  }

  async function deleteProduct(id) {
    if (useApi.value) {
      try {
        await productsApi.deleteProduct(id);
        products.value = products.value.filter((p) => p.id !== id);
        return;
      } catch { useApi.value = false; }
    }
    products.value = products.value.filter((p) => p.id !== id);
  }

  function generateId() {
    if (crypto?.randomUUID) return crypto.randomUUID();
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
      var r = (Math.random() * 16) | 0;
      return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
    });
  }

  return { products, onlineProducts, allProducts, visibleProducts, useApi,
    checkApi, forceLocal, forceApi,
    addProduct, updateProduct, deleteProduct, generateId };
});
