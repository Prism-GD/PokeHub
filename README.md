# PokéHub

Open-source community hub for Pokémon-related homebrew projects, ROM-hack patches, emulators, save utilities, and ROM development tools.

## Current status

**Alpha 0.1.0 — Android app shell in development.**

The current app includes:
- Home, Explore, Library, and Profile navigation
- Search and category filters
- Catalog item detail pages
- Session-only favorites
- Clearly labeled demonstration catalog entries

The catalog is still demonstration data. Downloads, accounts, repository syncing, and persistent favorites are not implemented yet. No APK is being published until the modules are integrated and tested.

## Technology

- Kotlin
- Android SDK
- Jetpack Compose
- Material 3
- Gradle

## Project structure

- `app/` — Android application
- `core/` — shared models and application logic (planned)
- `repository/` — catalog and repository integrations (planned)
- `metadata/` — catalog metadata and schemas
- `assets/` — project assets
- `docs/` — documentation and roadmap
- `scripts/` — development scripts
- `tests/` — automated tests

## Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md).

## Legal distribution policy

PokéHub focuses on homebrew, original fan projects, development tools, emulators, and legally shareable patches. It will not host or distribute commercial copyrighted ROM files. ROM hacks should use patch distribution wherever possible.

## License

To be defined.
