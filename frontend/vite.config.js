// Vite 构建配置
// GitHub Pages 部署时使用相对路径 base: "./"
const { defineConfig } = require("vite");
const vue = require("@vitejs/plugin-vue");

module.exports = defineConfig({
  base: "/demo/",
  plugins: [vue()],
  resolve: {
    alias: {
      // @ 别名指向 src 目录，方便组件内 import 路径
      "@": "/src",
    },
  },
  server: {
    port: 5173,
  },
});