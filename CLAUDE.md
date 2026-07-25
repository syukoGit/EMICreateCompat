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

The code is organized by **integration boundary**, not by technical layer. The organising rule: a pure core reasoned
about in isolation, with thin adapters around each third-party API. **Preserve this structure when adding features — do
not collapse logic back into the mixins or introduce a layered (domain/application/infrastructure) split.**

Base package `fr.syuko.emicreatecompat` (group `fr.syuko`). Sub-packages:

- **(root)** — [`Emicreatecompat.java`](src/main/java/fr/syuko/emicreatecompat/Emicreatecompat.java), the `@Mod`
  entrypoint. `MODID = "emicreatecompat"`; constructor takes `ModContainer` and registers the config spec.
- **`config/`** — [`Config`](src/main/java/fr/syuko/emicreatecompat/config/Config.java) (client `ModConfigSpec` +
  mirrored static fields) and [`TreeVisibility`](src/main/java/fr/syuko/emicreatecompat/config/TreeVisibility.java). In
  `Config`, `SPEC = BUILDER.build()` **must stay after every `define(...)` call** or the option is silently dropped from
  the spec.
- **`emi/`** — [`EmiRecipeTreeReader`](src/main/java/fr/syuko/emicreatecompat/emi/EmiRecipeTreeReader.java): the EMI
  adapter. Reads the active BoM tree and returns a plain `Set<Item>`; nothing outside this package touches `dev.emi.*`.
- **`create/`** — the Create adapters and the matching logic: [
  `PendingOrder`](src/main/java/fr/syuko/emicreatecompat/create/PendingOrder.java), [
  `StockMatcher`](src/main/java/fr/syuko/emicreatecompat/create/StockMatcher.java) (pure over its inputs), [
  `StockCategoryInjector`](src/main/java/fr/syuko/emicreatecompat/create/StockCategoryInjector.java) (returns fresh
  lists for the mixin to assign back).
- **`mixin/`** — thin adapters only. A mixin captures a hook, gathers screen state, delegates to `emi`/`create`, and
  writes the result back. It holds **no business logic**. Register new classes in the `client` array of [
  `emicreatecompat.mixins.json`](src/main/resources/emicreatecompat.mixins.json).

**Dependency rule:** `emi/` and `create/` do not know about each other and only meet inside a mixin. Prefer Create/EMI
**public APIs** where they exist; use mixins for UI injection where they don't.

Mod metadata is templated in [
`src/main/templates/META-INF/neoforge.mods.toml`](src/main/templates/META-INF/neoforge.mods.toml) (expanded by the
`generateModMetadata` task from `gradle.properties`), never edited under `build/`. Dependencies (EMI, Create, Ponder,
Flywheel, Registrate) are wired in `build.gradle`; declare mod APIs as `compileOnly` and full artifacts as `runtimeOnly`
where that split exists.

## Conventions

- **No comments — the code must be self-descriptive.** Do not add Javadoc, descriptive, explanatory, example, or warning
  comments in any file (`.java`, `.gradle`, `.properties`, `.yaml`, …). Make intent clear through names and structure
  instead; if a comment feels necessary, extract a well-named method or constant. (`.comment(...)` calls in `Config` are
  not code comments — they generate the user-facing config file and must stay. License headers in generated wrapper
  scripts also stay.)
- Keep new user-facing strings in [`en_us.json`](src/main/resources/assets/emicreatecompat/lang/en_us.json) and reference them via `Component.translatable(...)`.
- The mod is client-only: gate any code touching rendering/screens behind client dist checks (`@EventBusSubscriber(value = Dist.CLIENT)` or equivalent).
