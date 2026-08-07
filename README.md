# EMI for Create

**Better integration between EMI and Create — plus a few extras.**

[![CurseForge](https://img.shields.io/curseforge/dt/1625163?logo=curseforge&label=Curseforge&suffix=%20&style=flat&color=242629&labelColor=F16436&logoColor=1C1C1C)](https://www.curseforge.com/minecraft/mc-mods/create-for-emi)

## About

EMI for Create improves the compatibility between [EMI](https://github.com/emilyploszaj/emi) and
[Create](https://github.com/Creators-of-Create/Create), and adds new features on top of the two mods so they work as one
coherent tool instead of two side-by-side ones.

> **Client-side only.** The mod does nothing on the server - install it on your client, and it will work on any world or
> server running Create and EMI.

## Features

### Stock Keeper

<details>
<summary><b>EMI recipe tree category</b> - see what your network already stocks</summary>

A category at the top of Create's Stock Keeper lists the stocked items your active EMI recipe tree still needs. What you
already have is filtered out.

Items queued in the order basket count as owned, so the tree updates as you compose your order.

<img width="1242" height="1090" alt="java_DdDqmM7bj6" src="https://github.com/user-attachments/assets/fd2ab54d-e61d-4aee-8b6c-323bafdf620f" />

</details>

<details>
<summary><b>Stock counts in item tooltips</b> - know what your network holds, anywhere</summary>

Bind a stock network with the target button in the Stock Keeper's request screen. While you wear Create's Engineer's
Goggles, every item tooltip then tells you how many the network holds. Click again to unbind.

The binding is saved per world or server. When the count cannot be trusted, the line grays out and shows `?`.

<img width="359" height="174" alt="image" src="https://github.com/user-attachments/assets/385fd036-9a53-4267-b00e-e16818238ed7" />

</details>

### Recipe tree

<details>
<summary><b>Expected / raw chance amounts</b> - choose how chanced recipes are counted</summary>

On chanced recipes, EMI asks for more than Create's JEI does: it pays for the failures. A button in EMI's
bill-of-materials screen switches between the two readings.

- **Expected** (default) - what one success costs on average, rounded to whole crafts and marked with a gold `≈`.
- **Raw** - what the recipes declare, with the expected cost moved to the Total Cost tooltip.

<img width="762" height="581" alt="java_MfxcehyHtK" src="https://github.com/user-attachments/assets/bfc26f31-0d3c-488e-a930-4cbc0215c5ce" />

</details>

## Requirements

|           |                            |
|-----------|----------------------------|
| Minecraft | 1.21.1                     |
| Loader    | NeoForge 21.1.241 or newer |
| Create    | 6.0.10                     |
| EMI       | 1.1.24                     |
| Side      | Client only                |

## Configuration

Options live in `config/emicreatecompat-client.toml` and are read on the client only.

| Option                   | Type                            | Default    | Description                                                                                                                                                              |
|--------------------------|---------------------------------|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `enabled`                | boolean                         | `true`     | Master switch: show the recipe-tree category at the top of Create's Stock Keeper.                                                                                        |
| `treeVisibility`         | `ALWAYS` / `CRAFTING_MODE_ONLY` | `ALWAYS`   | When to show the category: as soon as an EMI recipe tree is active, or only while EMI's recipe-tree (crafting) view is enabled.                                          |
| `countPendingOrder`      | boolean                         | `true`     | Count items already queued in the order basket as if they were in your inventory, so the recipe tree updates live as you build your order.                               |
| `chanceAmounts`          | `EXPECTED` / `RAW`              | `EXPECTED` | How the recipe tree shows amounts for outputs carrying a produce chance: the average cost of one success, or the amounts the recipes declare.                            |
| `extraCraftThreshold`    | double, `0.0`-`1.0`             | `0.5`      | In `EXPECTED` mode, how much of an extra craft is tolerated before rounding up to one more whole craft. `0.0` always adds one, `0.5` rounds to the nearest, `1.0` never. |
| `alignChancedFavorites`  | boolean                         | `true`     | Make the synthetic favorites of a chanced recipe show the same amount as the recipe tree does.                                                                           |
| `showStockInTooltips`    | boolean                         | `true`     | Show how many of an item the bound stock network holds, on every item tooltip, while wearing the Engineer's Goggles.                                                     |
| `stockPollIntervalTicks` | int, `20`-`200`                 | `40`       | How often, in ticks, to ask the bound network for its contents while a screen is open. Raise it on a busy server; 20 ticks = 1 second.                                   |

## Building from source

Java 21 is required; the Gradle toolchain enforces it.

```bash
./gradlew build       # full build, jar lands in build/libs
./gradlew runClient   # launch a dev client with the mod
```

## Contributing

Issues and pull requests are welcome.

The code is organized by **integration boundary**, not by technical layer: `emi/` and `create/` are thin adapters around
each third-party API, and they only meet inside a mixin. Mixins hold no business logic. Please keep that structure when
adding features - see [CLAUDE.md](CLAUDE.md) for the full guidelines.

## License

Released under the MIT License. See [LICENSE](LICENSE).

## Credits

- [Create](https://github.com/Creators-of-Create/Create) by the Creators of Create
- [EMI](https://github.com/emilyploszaj/emi) by Emi
- [NeoForge](https://neoforged.net/)
