---
paths:
  - "**/pwa/**"
  - "**/static/**"
---

# Progressive Web App

- Serve `sw.js` and `manifest.webmanifest` from the site root so the service worker controls
  the whole app. Serve `sw.js` with `Cache-Control: no-cache`.
- **Whenever a precached or static file changes, bump the `CACHE` name in `sw.js`.** Keep
  `PRECACHE` in sync with the files that actually exist.
- Caching strategy: static assets are cache-first; page navigations are network-first with an
  offline fallback to the cached page; non-GET requests (htmx mutations) and cross-origin
  requests bypass the worker.
- Service workers only register over HTTPS (localhost is exempt). Test installability on a
  deployed HTTPS URL.
- The manifest needs at least 192 px and 512 px icons, `start_url`, `display` and `theme_color`
  matching the page's `theme-color` meta tag.
