# FicsitMonitor - Kotlin Multiplatform Application

## Project Overview

FicsitMonitor is a Kotlin Multiplatform (KMP) client application that consumes the API provided by the **satisfactory-server** backend.

## API Reference (Critical)

The backend API is maintained in Pablo's repository:
- **Repository:** `PabloHurtadoGonzalo86/satisfactory-server`
- **URL:** https://github.com/PabloHurtadoGonzalo86/satisfactory-server.git
- **MCP access:** Available via the `github-pablo` MCP server

**When working on any task related to API consumption, data models, endpoints, DTOs, WebSocket events, or data structure, always consult Pablo's repository first** using the `mcp__github-pablo__*` tools to read the actual backend source code (controllers, routes, events, resources, DTOs).

### Key backend components to reference

- **API Routes:** `routes/api.php` and `routes/web.php` - endpoint definitions
- **Controllers:** `app/Http/Controllers/` - request/response structure
- **Resources:** `app/Http/Resources/` - JSON response format (the actual shape of API responses)
- **Events:** `app/Events/` - WebSocket broadcast event payloads
- **DTOs:** `app/DTOs/` - data transfer object definitions
- **FrmApiClient:** `app/Services/FrmApiClient.php` - Satisfactory FRM API integration

### Backend stack

- Laravel (PHP 8.3) with Blade + Alpine.js + SCSS frontend
- Laravel Reverb for WebSocket broadcasting
- Redis for caching real-time metrics
- Horizon for queue processing
- Deployed on Kubernetes (Traefik ingress, TLS)

### Known API domains

- Power metrics (circuits, batteries, generators)
- Production metrics (items produced/consumed)
- Server health and status
- Player tracking
- Trains and drone stations (logistics)
- Extractors (miners)
- World inventory
- Resource sink (AWESOME points/coupons)
- Factory building status