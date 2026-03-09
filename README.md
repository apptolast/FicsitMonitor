# FICSIT Monitor

A **Kotlin Multiplatform** (KMP) mobile application for real-time monitoring of Satisfactory dedicated servers. Built
with **Compose Multiplatform**, targeting **Android** and **iOS** from a single codebase.

FICSIT Monitor connects to
the [Satisfactory Server Dashboard](https://github.com/PabloHurtadoGonzalo86/satisfactory-server) backend via REST API
and WebSocket to deliver live factory metrics straight to your phone.

## Features

- **Real-time updates** — WebSocket-driven via Laravel Reverb (Pusher-compatible protocol) with automatic reconnection
  and exponential backoff
- **Power grid monitoring** — Track circuits, capacity, consumption, battery levels, and generators
- **Production dashboard** — Monitor item production/consumption rates and identify bottlenecks
- **Train & drone logistics** — Track train routes and drone station activity
- **Extractor monitoring** — View miner and resource extractor status
- **Factory building status** — Overview of all buildings with performance indicators
- **World inventory** — Browse complete resource stockpiles
- **AWESOME Sink tracker** — Track points and available coupons
- **Player tracking** — See who's online and their status
- **Server health** — Monitor uptime, connection status, and server metrics
- **Live event feed** — Real-time activity stream across your factory
- **Multi-language** — English and Spanish

## Tech Stack

| Layer                    | Technology                                  |
|--------------------------|---------------------------------------------|
| **Language**             | Kotlin 2.3                                  |
| **UI Framework**         | Compose Multiplatform 1.10 (Material 3)     |
| **Networking**           | Ktor 3.4 (OkHttp on Android, Darwin on iOS) |
| **WebSocket**            | Ktor WebSocket with Pusher/Reverb protocol  |
| **Dependency Injection** | Koin 4.1                                    |
| **Serialization**        | Kotlinx Serialization (JSON)                |
| **Navigation**           | Compose Navigation                          |
| **State Management**     | Kotlin Coroutines + StateFlow / SharedFlow  |
| **Build Config**         | BuildKonfig                                 |
| **Android min SDK**      | 26 (Android 8.0)                            |
| **iOS targets**          | arm64, simulator arm64                      |

## Architecture

The project follows a **layered clean architecture** with unidirectional data flow:

```
┌─────────────────────────────────────────────┐
│              Presentation Layer             │
│  Compose UI  ←  ViewModels  ←  StateFlow    │
├─────────────────────────────────────────────┤
│               Domain Layer                  │
│          Repository Interfaces              │
├─────────────────────────────────────────────┤
│                Data Layer                   │
│  Repository Impl  ←  API Service            │
│                   ←  WebSocket Client       │
│                   ←  Event Dispatcher       │
└─────────────────────────────────────────────┘
```

**Key patterns:**

- **Repository pattern** — Abstracts data sources behind interfaces
- **Event Dispatcher** — Decouples WebSocket events from business logic, routes updates to the correct repository
- **ViewModel** — Lifecycle-aware state holders with `viewModelScope`
- **StateFlow** — Reactive state propagation from data layer to UI

**Real-time data flow:**

```
Backend (Reverb) → WebSocket Frame → ReverbWebSocketClient
  → WebSocketEventDispatcher → Repository.update()
    → StateFlow emission → ViewModel → Compose recomposition
```

## Project Structure

```
FicsitMonitor/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/.../fiscsitmonitor/
│       │   ├── App.kt                        # Entry point
│       │   ├── di/                            # Koin modules
│       │   │   ├── InitKoin.kt
│       │   │   ├── DataModule.kt
│       │   │   └── PresentationModule.kt
│       │   ├── data/
│       │   │   ├── model/                     # DTOs (13 serializable models)
│       │   │   ├── remote/
│       │   │   │   ├── api/                   # REST API service
│       │   │   │   └── websocket/             # WebSocket client & event dispatcher
│       │   │   └── repository/                # Repository implementations
│       │   ├── domain/
│       │   │   └── repository/                # Repository interfaces
│       │   ├── presentation/
│       │   │   ├── navigation/                # Routes & NavHost
│       │   │   ├── viewmodel/                 # 5 ViewModels
│       │   │   └── ui/
│       │   │       ├── screens/               # 5 screens (Home, Energy, Factory, Logistics, Live)
│       │   │       ├── components/            # Reusable composables
│       │   │       └── theme/                 # Material 3 theme, colors, typography
│       │   └── util/                          # Formatters
│       ├── commonMain/composeResources/       # i18n strings & drawables
│       ├── androidMain/                       # Android entry point & resources
│       └── iosMain/                           # iOS entry point
├── iosApp/                                    # iOS Xcode project
├── gradle/
│   └── libs.versions.toml                     # Version catalog
└── build.gradle.kts
```

## Getting Started

### Prerequisites

- **Android Studio** Ladybug or later (with KMP plugin)
- **Xcode 15+** (for iOS builds)
- **JDK 11+**
- A running instance of [satisfactory-server](https://github.com/PabloHurtadoGonzalo86/satisfactory-server) backend

### Configuration

Create a `local.properties` file in the project root with:

```properties
API_BASE_URL=https://your-server-url.com/api/v1
WS_APP_KEY=your-websocket-app-key
```

### Build & Run

**Android:**

```shell
./gradlew :composeApp:assembleDebug
```

**iOS:**

Open the `iosApp/` directory in Xcode and run from there, or use the KMP run configuration in Android Studio.

## Backend

This app consumes the API provided by
the [Satisfactory Server Dashboard](https://github.com/PabloHurtadoGonzalo86/satisfactory-server) — a Laravel 12 backend
with:

- REST API for server data and initial state
- Laravel Reverb (WebSocket) for real-time broadcasting
- Redis caching for live metrics
- Data sourced from the [FicsitRemoteMonitoring](https://ficsit.app/mod/FicsitRemoteMonitoring) game mod

### WebSocket Events

The app subscribes to the public channel `servers.{serverId}` and handles 12 event types:

| Event                     | Payload Key  | Description                  |
|---------------------------|--------------|------------------------------|
| `server.status.updated`   | *(flat)*     | Server online/offline status |
| `server.metrics.updated`  | *(flat)*     | Server performance metrics   |
| `power.updated`           | `circuits`   | Power circuit data           |
| `production.updated`      | `items`      | Production item rates        |
| `players.updated`         | `players`    | Player list                  |
| `trains.updated`          | `trains`     | Train status                 |
| `drones.updated`          | `stations`   | Drone station status         |
| `generators.updated`      | `generators` | Generator details            |
| `factory.updated`         | `buildings`  | Factory building status      |
| `extractors.updated`      | `extractors` | Resource extractors          |
| `world_inventory.updated` | `inventory`  | World inventory items        |
| `resource_sink.updated`   | `sink`       | AWESOME Sink data            |

## Authors

- [AppToLast](https://github.com/AppToLast)
