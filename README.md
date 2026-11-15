# PokéHub

Open-source Android hub for Pokémon-related homebrew projects, authorized ROM-hack patches, emulators, save utilities, and development tools.

## Current development status

**Alpha 0.2 — core platform foundations are being implemented.**

Implemented in source:
- Kotlin + Jetpack Compose Android app shell
- Room entities and DAO for catalog, favorites, independent repositories, downloads, and publisher drafts
- HTTPS repository index parser and synchronization service
- WorkManager download queue worker with optional SHA-256 integrity verification
- Developer metadata validation and JSON repository index generator
- Unit tests for repository index and publisher validation

Not yet verified:
- The Android project has not been built or tested in this environment.
- The new data services still need full integration into the Compose user interface.
- There is no production catalog endpoint configured by default.
- No APK is being claimed or distributed.

## Stack
Kotlin, Android SDK, Jetpack Compose, Material 3, Room, OkHttp, WorkManager, Gradle.

## Repository structure
- app/ — Android application and platform services
- core/ — shared domain logic (planned)
- repository/ — repository specifications and integrations
- metadata/ — catalog metadata
- assets/ — project assets
- docs/ — documentation
- scripts/ — development scripts
- tests/ — additional tests

## Repository index
See docs/ROADMAP.md for schema v1 and roadmap.

## Legal distribution policy
PokéHub may catalog and distribute homebrew, original projects, tools, emulators, and authorized patches. It must not host commercial copyrighted Pokémon ROM files. Prefer patch distribution and require users to provide their own legally obtained game files.

## License
To be defined.
