<template>
  <DefaultLayout>
    <div class="container">
      <div class="manage-header">
        <h1 class="page-title">产品管理</h1>
        <el-button class="btn-add" @click="openAdd">+ 新增产品</el-button>
      </div>

      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索产品名称..."
          clearable
          style="max-width: 300px"
        />
        <span class="search-count">共 {{ filteredProducts.length }} 个产品</span>
      </div>

      <el-table :data="filteredProducts" stripe size="large"
        style="width: 100%"
        @sort-change="onSortChange"
        :default-sort="{ prop: 'priority', order: 'ascending' }"
      >
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column label="图标" width="90" align="center">
          <template #default="{ row }">
            <span style="font-size: 22px">{{ row.icon || "📦" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" sortable="custom" min-width="160" />
        <el-table-column label="链接" min-width="160" show-overflow-tooltip>
          <template #default="{ row }">
            <a :href="row.url" target="_blank" class="table-link">{{ row.url }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" sortable="custom" width="120" align="center">
          <template #default="{ row }">
            <span
              class="status-tag"
              :class="statusClass(row.status)"
              @click="toggleStatus(row)"
            >
              {{ statusLabel(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" sortable="custom" width="120" align="center" />
        <el-table-column label="操作" width="140" align="center">
          <template #default="{ row }">
            <el-button class="btn-action" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button class="btn-action btn-action-del" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <span class="empty-text">暂无产品，点击右上角"新增产品"开始</span>
        </template>
      </el-table>

      <ProductForm
        v-if="showForm"
        :product="editingProduct"
        @save="handleSave"
        @close="showForm = false"
      />
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed } from "vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ProductForm from "@/components/ProductForm.vue";
import { useProductsStore } from "@/stores/products.js";
import { ElMessageBox } from "element-plus";

const productsStore = useProductsStore();
const searchQuery = ref("");
const showForm = ref(false);
const editingProduct = ref(null);

// 排序状态
const sortProp = ref("priority");
const sortOrder = ref("ascending");

// 状态映射
const STATUS = {
  online:     { label: "上线",   class: "status-online" },
  developing: { label: "开发中", class: "status-developing" },
  offline:    { label: "下线",   class: "status-offline" },
};
const NEXT = { online: "developing", developing: "offline", offline: "online" };

function statusLabel(s) { return STATUS[s]?.label || s; }
function statusClass(s) { return STATUS[s]?.class || ""; }

function onSortChange({ prop, order }) {
  sortProp.value = prop;
  sortOrder.value = order || "ascending";
}

const filteredProducts = computed(() => {
  let list = [...productsStore.products];
  const q = searchQuery.value.toLowerCase().trim();
  if (q) list = list.filter((p) => p.name.toLowerCase().includes(q));
  if (sortProp.value) {
    list.sort((a, b) => {
      const va = a[sortProp.value] ?? "";
      const vb = b[sortProp.value] ?? "";
      const cmp = typeof va === "number" ? va - vb : String(va).localeCompare(String(vb));
      return sortOrder.value === "descending" ? -cmp : cmp;
    });
  }
  return list;
});

function openAdd() {
  editingProduct.value = null;
  showForm.value = true;
}

function openEdit(product) {
  editingProduct.value = { ...product };
  showForm.value = true;
}

async function handleSave(formData) {
  if (editingProduct.value) {
    await productsStore.updateProduct(editingProduct.value.id, formData);
  } else {
    await productsStore.addProduct({
      ...formData,
      id: productsStore.generateId(),
      tags: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    });
  }
  showForm.value = false;
  editingProduct.value = null;
}

async function toggleStatus(product) {
  const newStatus = NEXT[product.status] || "online";
  await productsStore.updateProduct(product.id, { status: newStatus });
}

async function handleDelete(product) {
  try {
    await ElMessageBox.confirm(
      `确定要删除"${product.name}" 吗？`,
      "确认删除",
      { confirmButtonText: "删除", cancelButtonText: "取消", type: "warning" }
    );
    await productsStore.deleteProduct(product.id);
  } catch { /* 取消 */ }
}
</script>

<style scoped>
.manage-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-lg);
}

.search-bar {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);
}

.search-count {
  font-size: var(--font-size-body);
  color: var(--color-text-secondary);
}

.table-link {
  font-size: var(--font-size-small);
  color: var(--color-primary);
  text-decoration: none;
}
.table-link:hover { color: var(--color-primary-light); }

.status-tag {
  font-size: var(--font-size-small);
  cursor: pointer;
}
.status-tag:hover { opacity: 0.8; }

.status-online  { color: var(--color-success); }
.status-developing { color: var(--color-warning); }
.status-offline { color: var(--color-text-secondary); }

.empty-text {
  color: var(--color-text-secondary);
  font-size: var(--font-size-body);
}

@media (max-width: 640px) {
  .manage-header { flex-direction: column; align-items: flex-start; gap: var(--space-md); }
  .search-bar { flex-direction: column; align-items: flex-start; }
}

/* 新增按钮 —— 浅紫背景 */
.btn-add {
  --el-button-bg-color: var(--color-primary);
  --el-button-border-color: var(--color-primary);
  --el-button-text-color: #fff;
  --el-button-hover-bg-color: var(--color-primary-light);
  --el-button-hover-border-color: var(--color-primary-light);
  --el-button-hover-text-color: #fff;
}

/* 操作按钮 —— 浅紫色调无背景 */
.btn-action {
  --el-button-bg-color: transparent;
  --el-button-border-color: transparent;
  --el-button-text-color: var(--color-primary);
  --el-button-hover-bg-color: transparent;
  --el-button-hover-border-color: transparent;
  --el-button-hover-text-color: var(--color-primary-dark);
}
.btn-action-del {
  --el-button-bg-color: transparent;
  --el-button-border-color: transparent;
  --el-button-text-color: var(--color-danger);
  --el-button-hover-bg-color: transparent;
  --el-button-hover-border-color: transparent;
  --el-button-hover-text-color: var(--color-danger);
}

/* ===== Element Plus 表格字体深度覆盖 ===== */
/* 表格整体 */
:deep(.el-table) {
  --el-table-font-size: var(--font-size-body);
  font-size: var(--font-size-body);
}
/* 表头 */
:deep(.el-table th.el-table__cell) {
  font-size: var(--font-size-body);
  font-weight: 600;
  color: var(--color-text);
  background-color: #f5f3ff;
}
/* 表头内的 cell */
:deep(.el-table th.el-table__cell .cell) {
  font-size: var(--font-size-body);
}
/* 表体单元格 */
:deep(.el-table td.el-table__cell) {
  font-size: var(--font-size-body);
}
/* 表体内的 cell */
:deep(.el-table td.el-table__cell .cell) {
  font-size: var(--font-size-body);
}
/* 空状态文字 */
:deep(.el-table__empty-text) {
  font-size: var(--font-size-body);
}
/* 索引列 */
:deep(.el-table .el-table__cell .cell) {
  font-size: var(--font-size-body);
}
/* 搜索输入框 */
:deep(.el-input__inner) {
  font-size: var(--font-size-body);
}
/* el-button 内部文字 */
:deep(.el-button) {
  font-size: var(--font-size-small);
  font-weight: 400;
}
/* 操作列按钮间距 */
:deep(.el-button + .el-button) {
  margin-left: var(--space-xs);
}
</style>