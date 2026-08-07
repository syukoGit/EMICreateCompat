# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

EMICreateCompat is a **client-side NeoForge mod** that adds features bridging **EMI** (recipe/item viewer) and
**Create** (tech/automation mod). The first planned feature: when an EMI recipe tree is active (resource breakdown
shown), display a category at the top of the Create stock UI listing the available Create-stock resources that match the
recipe tree's ingredients.

All features are display/UI oriented — treat this as a **client-only** mod (`Dist.CLIENT`); avoid adding server-side
logic unless a feature genuinely requires it.

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

There are no tests yet. Gametests are wired up (`neoforge.enabledGameTestNamespaces=emicreatecompat`) and run via the
`runGameTestServer` config / the in-game `/test` command if added later.

## Architecture

The code is organized by **integration boundary**, not by technical layer. The organising rule: a pure core reasoned
about in isolation, with thin adapters around each third-party API. **Preserve this structure when adding features — do
not collapse logic back into the mixins or introduce a layered (domain/application/infrastructure) split.**

Base package `fr.syuko.emicreatecompat` (group `fr.syuko`). Sub-packages:

- **(root)** — [`Emicreatecompat.java`](src/main/java/fr/syuko/emicreatecompat/Emicreatecompat.java), the `@Mod`
  entrypoint. `MODID = "emicreatecompat"`; constructor takes `ModContainer` and registers the config spec.
- **`config/`** — [`Config`](src/main/java/fr/syuko/emicreatecompat/config/Config.java) (client `ModConfigSpec` +
  mirrored static fields) and the option enums it defines ([
  `TreeVisibility`](src/main/java/fr/syuko/emicreatecompat/config/TreeVisibility.java), `RecipeRegistration`, [
  `ChanceAmounts`](src/main/java/fr/syuko/emicreatecompat/config/ChanceAmounts.java)). In `Config`,
  `SPEC = BUILDER.build()` **must stay after every `define(...)` call** or the option is silently dropped from the spec.
- **`emi/`** — [`EmiRecipeTreeReader`](src/main/java/fr/syuko/emicreatecompat/emi/EmiRecipeTreeReader.java): the EMI
  adapter. Reads the active BoM tree and returns a plain `Set<Item>`; nothing outside this package touches `dev.emi.*`.
  `emi/bom/` holds the bill-of-materials adapter, one responsibility per class: [
  `BomAmountMode`](src/main/java/fr/syuko/emicreatecompat/emi/bom/BomAmountMode.java) (the single decision point), [
  `ExpectedCostIndex`](src/main/java/fr/syuko/emicreatecompat/emi/bom/ExpectedCostIndex.java), `ExpectedCostTooltip`,
  `ChancedFavorites`, the `ChancedNode` duck-type interface, and [
  `ExpectedAmounts`](src/main/java/fr/syuko/emicreatecompat/emi/bom/ExpectedAmounts.java) — the arithmetic, kept free of
  any `dev.emi` import so it stays testable on its own.
- **`create/`** — the Create adapters and the matching logic: [
  `PendingOrder`](src/main/java/fr/syuko/emicreatecompat/create/PendingOrder.java), [
  `StockMatcher`](src/main/java/fr/syuko/emicreatecompat/create/StockMatcher.java) (pure over its inputs), [
  `StockCategoryInjector`](src/main/java/fr/syuko/emicreatecompat/create/StockCategoryInjector.java) (returns fresh
  lists for the mixin to assign back). `create/render/` holds render primitives shared by several categories — see [
  `KineticsRender`](src/main/java/fr/syuko/emicreatecompat/create/render/KineticsRender.java) (lighting, shaft state,
  shaft angle), the replacement for Create's unusable `AnimatedKinetics`.
- **`category/`** — one package per Create recipe category, each holding the four files that only make sense together:
  `XxxDisplay` (record of **vanilla types only**), `XxxRecipes` (reads the `RecipeManager`), `XxxRender` (draws into a
  `GuiGraphics`) and `XxxEmiRecipe` (the `BasicEmiRecipe`). See [
  `category/pressing/`](src/main/java/fr/syuko/emicreatecompat/category/pressing).
- **`mixin/`** — thin adapters only. A mixin captures a hook, gathers screen state, delegates to `emi`/`create`, and
  writes the result back. It holds **no business logic**. Register new classes in the `client` array of [
  `emicreatecompat.mixins.json`](src/main/resources/emicreatecompat.mixins.json). Seven of them target EMI **internals**
  rather than its API (`ChanceState`, `TreeCost`, `MaterialNode`, `EmiFavorites`,
  `BoMScreen` and its private inner `Node` / `Hover`). Those are reached by `targets = "dev.emi.emi...$Node"`, pinned by
  bytecode ordinal, and declared `remap = false` because the classes are not Minecraft's. Injection points were checked
  with `javap` against every EMI 1.21.1 release from 1.1.13 to 1.1.24 — identical descriptors, opcode targets and
  occurrence order throughout, which is what the `[1.1.13,1.2)` range in `neoforge.mods.toml` rests on.
  `defaultRequire: 1` turns a moved target into a loud startup failure rather than a silent degradation. **Re-verify
  them whenever `emi_version` is bumped.**
- **`plugin/`** — the EMI plugin registry: [
  `CreateEmiPlugin`](src/main/java/fr/syuko/emicreatecompat/plugin/CreateEmiPlugin.java) (`@EmiEntrypoint`, discovered
  by EMI itself — no `neoforge.mods.toml` entry), `CreateEmiCategories`, the `RegisteredCategory` record, and the
  package-private icon renderables `CreateEmiCategories` builds its category icons from (`DoubleItemIcon`). Nothing else
  belongs here.
  `register()` holds **one generic loop** over `categories()`: keep it free of per-category branching, and never
  dispatch on recipe classes there. Whether recipes are registered at all is driven by `Config.recipeRegistration`:
  `AUTO` (default) skips registration when `jei` is loaded, because EMI already bridges Create's JEI plugin and the
  recipes would be duplicated.

**Adding a category** = a new `category/xxx/` package with its four files, one constant in `CreateEmiCategories`, and
one `RegisteredCategory` entry in `CreateEmiPlugin.categories()` (category, workstations, and a
`RecipeManager -> recipes` function). Reuse Create's own ids and lang keys (`create:pressing`, `create.recipe.pressing`)
by overriding `EmiRecipeCategory#getName()`. Coordinates copied from Create's JEI category must be **shifted by -1 on
both axes**: JEI places a 16x16 ingredient with a background at `-1,-1`, EMI's `SlotWidget` is 18x18 placed by its
corner. Every output slot needs `.recipeContext(this)` or the recipe never resolves in EMI's tree.

**Chanced outputs read higher than in JEI, and that is a setting, not a bug.** `EmiStack#setChance` makes EMI's bill of
materials budget the failure rate: `TreeCost` turns a produce chance into `1 / chance` and reports
`round(amount / chance)`. A sequenced assembly with 5 loops and a ~0.85 output chance therefore asks for 6 of each step
ingredient where Create's JEI shows 5 — the expected cost of one success versus the cost of one attempt. **Never drop a
`setChance` call to make the numbers match JEI.** The choice belongs to `Config.chanceAmounts`.

`EXPECTED` (default) leaves EMI untouched. `RAW` neutralizes the chance **at its source**, in `ChanceState#produce` /
`#consume`, so nothing is ever flagged chanced: costs stay flat, the `≈` sign and the gold highlight disappear, and
crafting progress consumes the inventory one for one instead of truncating `amount -= (long) (given / chance)`.
Neutralizing at display time instead does not work — it was tried, and the progress counter stalls because the
consumption itself happens in expected space.

Under `RAW` the expected value then exists nowhere, so [
`ExpectedCostIndex`](src/main/java/fr/syuko/emicreatecompat/emi/bom/ExpectedCostIndex.java) rebuilds it: a preliminary
`calculateCost()` pass runs inside `BomAmountMode.whileMeasuringExpectedCosts`, which suspends `rawAmounts()` for its
duration, and the result is indexed per ingredient for the Total Cost tooltip. Ingredients with no chanced share are
left out of the index so they gain no tooltip line.

Independently of the mode, `Config.alignChancedFavorites` works around an EMI inconsistency: `EmiFavorites#countRecipes`
counts from the raw `MaterialNode#totalNeeded` while the tree displays the chanced amount, so a step reading 6 in the
tree shows 5 in the favorites bar and only drops once two items have been gathered. `TreeCostMixin` records the
accumulated multiplier on each node through the `ChancedNode` interface, and `ChancedFavorites` applies it back.

**Dependency rule:** `emi/` and `create/` do not know about each other. They meet in `mixin/` (UI injection) and in
`category/` (the EMI plugin). Inside a `category/xxx/` package the boundary is kept by naming, and it is greppable —
these four commands must all return nothing:

```bash
grep -rl "dev\.emi" category/ | grep -v "EmiRecipe\.java$"          # only *EmiRecipe may see EMI
grep -rl -e "com\.simibubi" -e "dev\.emi" --include="*Display.java" category/
grep -rl "com\.simibubi" emi/
grep -rl "dev\.emi" create/
```

Prefer Create/EMI **public APIs** where they exist; use mixins for UI injection where they don't.

**Reusing Create's JEI code:** everything under `com.simibubi.create.compat.jei.**` is off limits at runtime —
`AnimatedKinetics` implements JEI's `IDrawable`, so loading `AnimatedPress` and friends without JEI installed throws
`NoClassDefFoundError`. Port the body into the category's `XxxRender` instead, on top of `KineticsRender`.
`AllGuiTextures` (including its `JEI_*` entries) and its textures ship with Create and carry no JEI dependency, so they
can be used directly.

Mod metadata is templated in [
`src/main/templates/META-INF/neoforge.mods.toml`](src/main/templates/META-INF/neoforge.mods.toml) (expanded by the
`generateModMetadata` task from `gradle.properties`), never edited under `build/`. Dependencies (EMI, Create, Ponder,
Flywheel, Registrate) are wired in `build.gradle`; declare mod APIs as `compileOnly` and full artifacts as `runtimeOnly`
where that split exists. `net.createmod.catnip.**` (`GuiGameElement`, `AnimationTickHolder`, `ILightingSettings`, …) is
already compilable without a new dependency: its classes are bundled inside the `ponder-neoforge` jar.

## Conventions

- **No comments — the code must be self-descriptive.** Do not add Javadoc, descriptive, explanatory, example, or warning
  comments in any file (`.java`, `.gradle`, `.properties`, `.yaml`, …). Make intent clear through names and structure
  instead; if a comment feels necessary, extract a well-named method or constant. (`.comment(...)` calls in `Config` are
  not code comments — they generate the user-facing config file and must stay. License headers in generated wrapper
  scripts also stay.)
- Keep new user-facing strings in [`en_us.json`](src/main/resources/assets/emicreatecompat/lang/en_us.json) and
  reference them via `Component.translatable(...)`.
- The mod is client-only: gate any code touching rendering/screens behind client dist checks
  (`@EventBusSubscriber(value = Dist.CLIENT)` or equivalent).

## Git

- **Never run `git commit` without an explicit go for that specific commit.** Editing files, staging them and proposing
  a message is fine; creating the commit is not. An instruction to "do the changes for commit N" is **not** permission
  to commit them, and permission for one commit never carries over to the next.
- The same rule covers every history rewrite (`reset`, `commit --amend`, `rebase`, `cherry-pick`) and `git push`.
- Commit messages are a **single Conventional Commits line** — no body, no `Co-Authored-By` trailer. Match the existing
  history: `feat: add spout filling, draining and mechanical crafting recipe support with EMI integration`.
