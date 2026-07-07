// 产品 API
import { api } from "./index.js";

/** 获取所有产品 */
export async function fetchProducts() {
  const res = await api.get("/api/products");
  return res.data;
}

/** 新增产品 */
export async function createProduct(product) {
  const res = await api.post("/api/products", product);
  return res.data;
}

/** 更新产品 */
export async function updateProduct(id, updates) {
  const res = await api.put(`/api/products/${id}`, updates);
  return res.data;
}

/** 删除产品（需管理员权限） */
export async function deleteProduct(id) {
  await api.delete(`/api/products/${id}`);
  return true;
}
