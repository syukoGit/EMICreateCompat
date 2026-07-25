# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

EMICreateCompat is a **client-side NeoForge mod** that adds features bridging **EMI** (recipe/item viewer) and **Create** (tech/automation mod). The first planned feature: when an EMI recipe tree is active (resource breakdown shown), display a category at the top of the Create stock UI listing the available Create-stock resources that match the recipe tree's ingredients.

All features are display/UI oriented — treat this as a **client-only** mod (`Dist.CLIENT`); avoid adding server-side logic unless a feature genuinely requires it.

## Environment

- **Minecraft** 1.21.1, **NeoForge** 21.1.241, **Java 21** (toolchain enforced in `build.gradle`).
- Mappings: Parchment (`parchment_minecraft_version` / `parchment_mappings_version` in `gradle.properties`).
- All version numbers live in `gradle.properties` — change them there, not in `build.gradle`.

## Commands

Use the Gradle wrapper (`./gradlew` on bash, `gradlew.bat` on cmd). Build config caching and daemon are enabled.

```bash
./gradlew compileJava        # fast compile check
./gradlew build              # full build + jar into build/libs
./gradlew runClient          # launch Minecraft client with the mod (dev)
./gradlew runData            # run data generators (output: src/generated/resources)
```

There are no tests yet. Gametests are wired up (`neoforge.enabledGameTestNamespaces=emicreatecompat`) and run via the `runGameTestServer` config / the in-game `/test` command if added later.

## Architecture

The codebase is intentionally minimal after cleanup — only the mod entrypoint remains. Key facts for extending it:

- **Entrypoint**: [`Emicreatecompat.java`](src/main/java/fr/syuko/emicreatecompat/Emicreatecompat.java) — the `@Mod` class. `MODID = "emicreatecompat"`. Constructor receives `IEventBus modEventBus` and `ModContainer` from FML.
- **Base package**: `fr.syuko.emicreatecompat` (group `fr.syuko`). Mixins go in the `fr.syuko.emicreatecompat.mixin` sub-package.
- **Mixins are the primary integration mechanism.** [`emicreatecompat.mixins.json`](src/main/resources/emicreatecompat.mixins.json) is already declared and registered via the templated mods.toml. Add mixin classes to the `mixins`/`client` arrays there. Prefer Create/EMI **public APIs** where they exist; use mixins for UI injection where they don't.
- **Mod metadata is templated**: edit [`src/main/templates/META-INF/neoforge.mods.toml`](src/main/templates/META-INF/neoforge.mods.toml), not a file under `build/`. The `generateModMetadata` Gradle task expands `${...}` placeholders (from `gradle.properties`) into `build/generated/sources/modMetadata`. Adding an EMI/Create dependency means adding a `[[dependencies."emicreatecompat"]]` block here **and** the Gradle dependency.
- **Dependencies (EMI, Create) are not yet added.** The Maven source is undecided (Modrinth Maven / CurseMaven / local `libs/`). The `dependencies { }` block in `build.gradle` and `repositories { }` are the two places to wire them. Declare mod APIs as `compileOnly` and the full artifacts as `runtimeOnly` where the split exists.

## Conventions

- Keep new user-facing strings in [`en_us.json`](src/main/resources/assets/emicreatecompat/lang/en_us.json) and reference them via `Component.translatable(...)`.
- The mod is client-only: gate any code touching rendering/screens behind client dist checks (`@EventBusSubscriber(value = Dist.CLIENT)` or equivalent).
