# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

> Communication: reply to the user in Chinese (中文). Code and this document stay in English.

个人产品管理系统 — a single-user product/project showcase. Public home page displays "online" products as cards; an admin-only manage page does CRUD. Vue 3 SPA frontend + Spring Boot backend.

> Read [design.md](design.md) before writing any code — it is the authoritative product spec (data model, routes, UI/UX, color system).

## Commands

Frontend (run from `frontend/`):

| Command | What it does |
|---------|--------------|
| `npm install` | Install dependencies |
| `npm run dev` | Vite dev server at `http://localhost:5173` |
| `npm run build` | Production build to `frontend/dist/` |
| `npm run preview` | Preview the production build |

Backend (run from `backend/`):

| Command | What it does |
|---------|--------------|
| `mvn spring-boot:run` | Start Spring Boot at `http://localhost:8080` |
| `mvn package` | Build the jar |

No linter or test suite is configured for either side yet.

## Architecture

The defining pattern is **graceful degradation**: the frontend runs fully standalone (in-memory data) and automatically upgrades to the backend when it is reachable. This means most frontend work can be done without running the backend at all.

### Frontend / backend handshake

- `api/index.js` is a hand-rolled `fetch` wrapper (not axios, despite older docs) pointed at `BASE_URL = http://localhost:8080`. It auto-attaches `Bearer <token>` from `localStorage`.
- On startup, `stores/products.js#checkApi()` calls `GET /api/products`. Success → `useApi = true` and it uses live data; failure → stays in local mode seeded from `data/products.js`.
- Every store mutation (`addProduct`/`updateProduct`/`deleteProduct`) tries the API when `useApi` is true and **falls back to local state if the call throws**. So a backend that goes down mid-session silently degrades rather than erroring.
- `auth.js#login()` does the same: tries `loginApi`, falls back to a hardcoded `LOCAL_USERS` table on failure.
- Manual overrides for debugging (browser console): `productsStore.forceLocal()` / `productsStore.forceApi()`.

When changing the API contract, update **both** sides: the Java controller/service and the matching `frontend/src/api/*.js` module + store action.

### State ownership

Pinia stores own all state; views call store actions and never mutate data directly. `auth.js` holds the user/role and persists `token` + `user` to `localStorage`. `products.js` exposes computed views (`onlineProducts`, `visibleProducts`, `allProducts`) all sorted by `priority` ascending.

### Auth & routing

- Hash-mode router (`createWebHashHistory`) for static-host compatibility (GitHub Pages). `vite.config.js` uses `base: "./"` for the same reason.
- Route guard in `router/index.js` reads `token`/`user` directly from `localStorage` (deliberately bypasses Pinia to avoid a circular import). `/manage` requires `requiresAuth` **and** `requiresAdmin` (role must be `admin`).
- Backend mirrors this: `GET /api/products` is public; create/update need a valid token (`401` otherwise); delete additionally requires `admin` role (`403` otherwise).
- Two seeded accounts on both sides: `admin`/`admin123` (role `admin`) and `user`/`user123` (role `user`).

### Backend specifics

- Storage is an in-memory `ConcurrentHashMap` in `ProductService` / `AuthService` — data resets on restart. JPA/H2, Spring Security, and JWT deps exist but are **commented out** in `pom.xml` and `application.yml`; tokens are mock strings (`mock-jwt-<uuid>`), not real JWTs.
- All responses are wrapped in `ApiResponse<T>` (`{ code, message, data }`); the frontend reads `res.data`.
- `update()` is a partial update — only non-null fields are applied.
- CORS is opened in `config/CorsConfig.java`.

## Conventions

- 2-space indent. Vue components PascalCase (`ProductCard.vue`); JS/store files kebab-case. Stores follow `useXxxStore`.
- Composition API + `<script setup>` only. SFC order: `<template>` → `<script setup>` → `<style scoped>`.
- UI uses **Element Plus** (globally registered in `main.js`). Pull colors/spacing from `styles/variables.css` — never hardcode the design-system values listed in design.md §7.
- Responsive breakpoints at 640px / 1024px (1 / 2 / 3 card columns).
- Commits: Conventional Commits (`feat:`, `fix:`, `chore:`), one logical change each.
- Product `status` values seen in code: `online`, `developing`, `offline` (only `offline` is hidden from the home page).

## Known doc drift

`AGENTS.md` and parts of `design.md` predate the current code. Where they conflict, the code wins: the backend is implemented (not a placeholder), the HTTP client is `fetch` (not axios), the UI uses Element Plus (not native-CSS-only), and there is real role-based access control.
