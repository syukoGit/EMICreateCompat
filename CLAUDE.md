# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

EMI for Create is a **client-side NeoForge mod** that adds features bridging **EMI** (recipe/item viewer) and **Create**
(tech/automation mod). The first planned feature: when an EMI recipe tree is active (resource breakdown shown), display
a category at the top of the Create stock UI listing the available Create-stock resources that match the recipe tree's
ingredients.

All features are display/UI oriented — treat this as a **client-only** mod (`Dist.CLIENT`); avoid adding server-side
logic unless a feature genuinely requires it.

The display name is **EMI for Create** (`mod_name`), but the identifier stays `emicreatecompat` everywhere it is
technically load-bearing: `mod_id`, the `fr.syuko.emicreatecompat` package, `emicreatecompat.mixins.json`, the
`assets/emicreatecompat/` resources, the lang keys and the `emicreatecompat-client.toml` config file. Renaming those
would drop the config of every existing install, so keep them as they are.

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
./gradlew runClientWithJei   # same, with JEI installed, to exercise EMI's JEMI bridge
./gradlew runData            # run data generators (output: src/generated/resources)
```

`clientWithJei` is a second `neoForge.runs` entry sharing the `run/` directory with `client` — same world, same config,
same instance. JEI is toggled by the mods folder: `installJeiMod` copies the jar into `run/mods/` and hangs off
`prepareClientWithJeiRun`, `removeJeiMod` deletes it and hangs off `prepareClientRun`, so whichever run is launched
prepares the folder for itself. **JEI must arrive as a mod, not as a classpath entry.** A per-run
`additionalRuntimeClasspath` was tried first and fails silently in the worst way: the `mezz.jei` classes load — so EMI's
own JEI mixins apply and nothing errors — but FML never registers a `jei` mod, JEMI never starts, and the run quietly
tests the no-JEI case. Jars listed in `build/moddev/<run>LegacyClasspath.txt` are libraries; mods reach FML by another
path, which is why `emi-neoforge` appears in no file under `build/moddev/`.

`recipeRegistration` in `run/config/emicreatecompat-client.toml` decides what the JEI run exercises: under `AUTO` the
plugin stands down and Create's recipes arrive through the JEMI bridge, under `ALWAYS` both sets are registered and
every Create category shows twice. Check it before trusting a JEI-run result.

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
  `emicreatecompat.mixins.json`](src/main/resources/emicreatecompat.mixins.json). Eight of them target EMI **internals**
  rather than its API (`ChanceState`, `TreeCost`, `MaterialNode`, `EmiFavorites`, `EmiRecipes`,
  `BoMScreen` and its private inner `Node` / `Hover`). Those are reached by `targets = "dev.emi.emi...$Node"`, pinned by
  bytecode ordinal, and declared `remap = false` because the classes are not Minecraft's. Injection points were checked
  with `javap` against every EMI 1.21.1 release from 1.1.13 to 1.1.24 — identical descriptors, opcode targets and
  occurrence order throughout, which is what the `[1.1.13,1.2)` range in `neoforge.mods.toml` rests on.
  `defaultRequire: 1` turns a moved target into a loud startup failure rather than a silent degradation. **Re-verify
  them whenever `emi_version` is bumped.**
- **`chance/`** — [`ChanceInjector`](src/main/java/fr/syuko/emicreatecompat/chance/ChanceInjector.java), the layer that
  makes produce chances independent of who displays a recipe. It walks every registered `EmiRecipe`, resolves the
  Minecraft recipe behind it and posts the chance onto the outputs. Display and chance are **two separate concerns**:
  the categories are ours only when JEI is absent, the chance is always ours. Keep it that way — folding the chance back
  into `category/` is what made it vanish under JEI in the first place. **`ChanceInjector` is the only caller of
  `EmiStack#setChance` in the mod**, and `grep -rn "setChance" category/` must return nothing. A second writer is not
  merely redundant: an output that already carries a chance is skipped without consuming its `ChancedStack`, so the
  match cursor drifts and a later guaranteed output of the same item can inherit the chanced entry.
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

**Chance is posted onto recipes we did not build**, so `EXPECTED` survives JEI.
[`ChanceInjector`](src/main/java/fr/syuko/emicreatecompat/chance/ChanceInjector.java) runs from `EmiRecipesMixin` at the
`TAIL` of `EmiRecipes#bake` — after every plugin, JEMI included, which loads last and so cannot be decorated from our
own `register()`. `TAIL` rather than `HEAD` because `bake` folds `EmiData.recipes` into the list on entry. It resolves
the Minecraft recipe with `EmiRecipe#getBackingRecipe()`, polymorphic and already exactly right: the interface default
looks up `getId()`, `JemiRecipe` overrides it with `originalId`, and both resolve through the `RecipeManager`. **Do not
reach for `dev.emi.emi.jemi` to do this** — those classes carry `mezz.jei` field types and must not load without JEI.

The write rule is one line: **touch an output only when `getChance() == 1`.** That single test spares our own recipes
and every other mod's native EMI plugin, with no exclusion list.

[`RecipeChances`](src/main/java/fr/syuko/emicreatecompat/create/recipe/RecipeChances.java) is the reader.
`ProcessingRecipe#getRollableResults` covers Create and every addon built on it in one branch — that generality is the
whole point of the layer. `SequencedAssemblyRecipe` needs its own: **`resultPool` holds weights, not probabilities.**
The real figure is `getOutputChance()`, which is `resultPool.getFirst().getChance() / sum(chances)`. Feeding the raw
weights to `setChance` produces a tree that looks chanced — gold highlight, toggle present — while the amounts never
inflate, because the weights are either all `1` or greater than `1`. The gold comes from other recipes in the tree and
hides the bug. **Verify a chance change by the numbers, not by the highlight**: 3 Rotation Speed Controllers ask for 3 ×
5 per step under `=` and 4 × 5 under `≈`.

The `≈`/`=` toggle only appears when the tree in front of the player actually has something chanced in it, so it stays
hidden on trees that carry no chance at all. [
`TreeChance`](src/main/java/fr/syuko/emicreatecompat/emi/bom/TreeChance.java) is the signal, fed from
`ChanceStateMixin` **before** the `RAW` neutralization and reset on every `recalculateTree`. Reading it after the
neutralization would hide the button as soon as `RAW` is on and strand the player in that mode. Gate on the tree, not on
whether `CreateEmiPlugin` registered: `chanceAmounts` is global, so a Create-specific gate would still be mangling other
mods' chanced recipes, and it would make `emi/` depend on `plugin/`.

Independently of the mode, `Config.alignChancedFavorites` works around an EMI inconsistency: `EmiFavorites#countRecipes`
counts from the raw `MaterialNode#totalNeeded` while the tree displays the chanced amount, so a step reading 6 in the
tree shows 5 in the favorites bar and only drops once two items have been gathered. `TreeCostMixin` records the
accumulated multiplier on each node through the `ChancedNode` interface, and `ChancedFavorites` applies it back.

**Dependency rule:** `emi/` and `create/` do not know about each other. They meet in exactly three places — `mixin/`
(UI injection), `category/` (the EMI plugin) and `chance/` (the chance layer). Inside a `category/xxx/` package the
boundary is kept by naming; `chance/` keeps it by depending on `create/` only through its vanilla-typed facade
(`RecipeChances`, `ChancedStack`), never on `com.simibubi` directly. It is greppable — these five commands must all
return nothing:

```bash
grep -rl "dev\.emi" category/ | grep -v "EmiRecipe\.java$"          # only *EmiRecipe may see EMI
grep -rl -e "com\.simibubi" -e "dev\.emi" --include="*Display.java" category/
grep -rl "com\.simibubi" emi/
grep -rl "dev\.emi" create/
grep -rl "com\.simibubi" chance/                                    # chance/ goes through create/'s facade
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
