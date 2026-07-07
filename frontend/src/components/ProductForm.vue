<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑产品' : '新增产品'"
    width="480px"
    :close-on-click-modal="false"
    class="product-dialog"
    @close="emit('close')"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-position="top"
      class="product-form"
      @submit.prevent="handleSubmit"
    >
      <el-form-item label="产品名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入产品名称" maxlength="50" clearable />
      </el-form-item>

      <el-form-item label="产品链接" prop="url">
        <el-input v-model="form.url" placeholder="https://example.com" clearable />
      </el-form-item>

      <el-form-item label="产品简介" prop="description">
        <el-input v-model="form.description" placeholder="简短描述你的产品" maxlength="200" type="textarea" :rows="2" />
      </el-form-item>

      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="图标" prop="icon">
            <el-input v-model="form.icon" placeholder="emoji" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-input-number v-model="form.priority" :min="1" :max="99" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="产品状态" prop="status">
        <div class="status-radios">
          <el-radio v-model="form.status" value="online">上线</el-radio>
          <el-radio v-model="form.status" value="developing">开发中</el-radio>
          <el-radio v-model="form.status" value="offline">下线</el-radio>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="emit('close')">取消</el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ isEdit ? '保存' : '添加' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, computed, ref, watch } from "vue";

const props = defineProps({
  product: { type: Object, default: null },
});

const emit = defineEmits(["save", "close"]);

const formRef = ref(null);
const visible = ref(true);

const isEdit = computed(() => !!props.product);

const form = reactive({
  name: "",
  description: "",
  url: "",
  icon: "",
  status: "online",
  priority: 10,
});

const rules = {
  name: [{ required: true, message: "请输入产品名称", trigger: "blur" }],
  url: [
    { required: true, message: "请输入链接", trigger: "blur" },
    { type: "url", message: "请输入正确的 URL 地址", trigger: "blur" },
  ],
};

function initForm() {
  form.name = props.product?.name || "";
  form.description = props.product?.description || "";
  form.url = props.product?.url || "";
  form.icon = props.product?.icon || "";
  form.status = props.product?.status || "online";
  form.priority = props.product?.priority || 10;
}

initForm();
watch(() => props.product, initForm);

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false);
  if (!valid) return;
  emit("save", { ...form });
}
</script>

<style scoped>
.product-dialog :deep(.el-dialog) {
  border-radius: var(--radius-md);
}

.product-dialog :deep(.el-dialog__header) {
  padding: var(--space-lg) var(--space-lg) var(--space-md);
  margin-bottom: 0;
  border-bottom: none;
}

.product-dialog :deep(.el-dialog__title) {
  font-size: var(--font-size-subtitle);
  font-weight: 600;
}

.product-dialog :deep(.el-dialog__headerbtn) {
  top: var(--space-lg);
  right: var(--space-lg);
}

.product-dialog :deep(.el-dialog__body) {
  padding: var(--space-md) var(--space-lg);
}

.product-dialog :deep(.el-dialog__footer) {
  padding: 0 var(--space-lg) var(--space-lg);
}

.product-form :deep(.el-form-item) { margin-bottom: var(--space-md); }

.product-form :deep(.el-form-item__label) {
  font-size: var(--font-size-body);
  font-weight: 500;
  color: var(--color-text);
  padding-bottom: var(--space-xs);
}

.product-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-sm);
  box-shadow: 0 0 0 1px var(--color-border) inset;
}

.product-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--color-primary-light) inset;
}

.product-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--color-primary) inset, 0 0 0 3px var(--color-primary-alpha);
}

.product-form :deep(.el-input__inner) { font-size: var(--font-size-body); }
.product-form :deep(.el-input__inner::placeholder) { color: var(--color-text-secondary); }

.product-form :deep(.el-textarea__inner) {
  font-size: var(--font-size-body);
  border-radius: var(--radius-sm);
  box-shadow: 0 0 0 1px var(--color-border) inset;
}

.product-form :deep(.el-textarea__inner:hover) {
  box-shadow: 0 0 0 1px var(--color-primary-light) inset;
}

.product-form :deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 1px var(--color-primary) inset, 0 0 0 3px var(--color-primary-alpha);
}

.product-form :deep(.el-textarea__inner::placeholder) { color: var(--color-text-secondary); }

.product-form :deep(.el-input-number .el-input__wrapper) { border-radius: var(--radius-sm); }

.status-radios { display: flex; gap: var(--space-lg); }

.status-radios .el-radio { margin-right: 0; }

.status-radios :deep(.el-radio__label) { font-size: var(--font-size-body); }

.status-radios :deep(.el-radio__input.is-checked .el-radio__inner) {
  background: var(--color-primary);
  border-color: var(--color-primary);
}

.status-radios :deep(.el-radio__input.is-checked + .el-radio__label) {
  color: var(--color-primary);
}
</style>