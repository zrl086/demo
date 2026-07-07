# Repository Guidelines

## Project Structure & Module Organization

```
demo/
├── frontend/                # Vue 3 SPA (main workspace)
│   ├── src/
│   │   ├── api/             # fetch-based client (index.js) + endpoint modules
│   │   ├── assets/images/   # Static images and icons
│   │   ├── components/      # Shared Vue components (ProductCard, BackToTop, etc.)
│   │   ├── data/            # Hardcoded fallback product list
│   │   ├── layouts/         # Page layout wrappers
│   │   ├── router/          # Vue Router + navigation guards
│   │   ├── stores/          # Pinia stores (auth, products)
│   │   ├── styles/          # Global CSS (variables, reset, global)
│   │   └── views/           # Page components (LoginView, HomeView, ManageView)
│   ├── index.html
│   └── vite.config.js
├── backend/                 # Spring Boot app (implemented, in-memory storage)
│   └── src/main/java/com/demo/
│       ├── controller/      # AuthController, ProductController
│       ├── service/         # AuthService, ProductService
│       ├── model/ dto/      # Entities + ApiResponse wrapper
│       └── config/          # CorsConfig
└── design.md                # Full product design spec
```

- **Views** contain page logic; shared components are pure presentational.
- **Stores** own all state. Views call store actions — never mutate data directly.
- **API modules** wrap the `fetch` client; stores call them and fall back to local state on failure (graceful degradation).

## Build, Test, and Development Commands

Frontend — run from `frontend/`:

| Command | What it does |
|---------|-------------|
| `npm install` | Install dependencies |
| `npm run dev` | Start Vite dev server (default `http://localhost:5173`) |
| `npm run build` | Production build to `frontend/dist/` |
| `npm run preview` | Preview production build locally |

Backend — run from `backend/`:

| Command | What it does |
|---------|-------------|
| `mvn spring-boot:run` | Start Spring Boot (`http://localhost:8080`) |
| `mvn package` | Build the jar |

No linter or test suite is configured yet. Add `npm run lint` and `npm test` when introducing ESLint/Prettier and Vitest.

## Coding Style & Naming Conventions

- **Indentation**: 2 spaces, no tabs.
- **Vue components**: PascalCase, multi-word (`ProductCard.vue`).
- **JS/Store files**: kebab-case (`auth.js`, `products.js`).
- **CSS classes**: kebab-case, component-prefixed (`.product-card__title`).
- **Store naming**: `useXxxStore` pattern.
- **SFC order**: `<template>` → `<script setup>` → `<style scoped>`.
- Use Composition API only. Reference CSS custom properties from `variables.css` for all colors and spacing — never hardcode values.

## Testing Guidelines

No tests configured yet. When added:

- Use **Vitest** + **Vue Test Utils**.
- Co-locate tests: `ProductCard.vue` → `ProductCard.spec.js`.
- Run with `npm test`; watch mode with `npm test -- --watch`.

## Commit & Pull Request Guidelines

- **Commits**: Conventional commits (`feat:`, `fix:`, `chore:`). One logical change per commit.
- **Branches**: Use `codex/` prefix (e.g., `codex/login-page`).
- **PRs**: Summarize changes, reference `design.md` sections, include screenshots for UI work.

## Architecture Notes

- **Auth**: Login tries the backend (`/api/login`) first and falls back to a hardcoded `LOCAL_USERS` table. Stores `token` + `user` in `localStorage`. Router guard checks the token and role before allowing `/manage` access.
- **Graceful degradation**: The frontend runs standalone on local data and auto-upgrades to the backend when reachable. `stores/products.js` probes `GET /api/products` on startup (`checkApi`); each mutation tries the API and falls back to local state if it throws. `api/index.js` is a hand-rolled `fetch` client (not axios) with `BASE_URL` pointing at the Spring Boot backend. Console overrides: `productsStore.forceLocal()` / `forceApi()`.
- **Roles**: Two seeded accounts on both sides — `admin`/`admin123` (admin) and `user`/`user123` (user). `/manage` requires admin; the backend delete endpoint also enforces admin (`403` otherwise), create/update require any valid token (`401` otherwise).
- **Backend storage**: In-memory `ConcurrentHashMap` (data resets on restart). Responses wrapped in `ApiResponse<T>` (`{ code, message, data }`). JPA/H2, Spring Security, and JWT deps are commented out; tokens are mock strings.
- **Responsive design**: Breakpoints at 640px and 1024px, defined in `variables.css`. Reference breakpoint variables instead of inline media queries.

- 写任何代码需完整阅读@design.md
