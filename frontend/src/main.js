// 应用入口 — 初始化 Vue 3 + Pinia + Vue Router + Element Plus
import { createApp } from "vue";
import { createPinia } from "pinia";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import App from "./App.vue";
import router from "./router";

// 全局样式（按顺序加载：重置 → 变量 → 全局）
import "./styles/reset.css";
import "./styles/variables.css";
import "./styles/global.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(ElementPlus);

app.mount("#app");
