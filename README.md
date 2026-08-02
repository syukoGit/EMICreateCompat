# Create for EMI

**Better integration between EMI and Create — plus a few extras.**

<!-- TODO: banner -->

[![CurseForge](https://img.shields.io/curseforge/dt/1625163?logo=curseforge&label=&suffix=%20&style=flat&color=242629&labelColor=F16436&logoColor=1C1C1C)](https://www.curseforge.com/minecraft/mc-mods/create-for-emi)

## About

Create for EMI improves the compatibility between [EMI](https://github.com/emilyploszaj/emi) and
[Create](https://github.com/Creators-of-Create/Create), and adds new features on top of the two mods so they work as one
coherent tool instead of two side-by-side ones.

> **Client-side only.** The mod does nothing on the server — install it on your client, and it will work on any world or
> server running Create and EMI.

## Features

### Stock Keeper

<details>
<summary><b>EMI recipe tree category</b> — see what your network already stocks</summary>

When an EMI recipe tree is active, a category is added at the top of Create's Stock Keeper listing the stocked items
that match the tree's missing ingredients. Branches you have already completed are filtered out, so only what you still
need shows up.

Items already queued in the order basket count as owned, so the recipe tree recalculates while you compose your order —
no need to send it first to see what is left to gather.

<!-- TODO: screenshot -->

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

| Option              | Type                            | Default              | Description                                                                                                                                |
|---------------------|---------------------------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `enabled`           | boolean                         | `true`               | Master switch: show the recipe-tree category at the top of Create's Stock Keeper.                                                          |
| `treeVisibility`    | `ALWAYS` / `CRAFTING_MODE_ONLY` | `CRAFTING_MODE_ONLY` | When to show the category: as soon as an EMI recipe tree is active, or only while EMI's recipe-tree (crafting) view is enabled.            |
| `countPendingOrder` | boolean                         | `true`               | Count items already queued in the order basket as if they were in your inventory, so the recipe tree updates live as you build your order. |

## Roadmap

- Full Create recipe support in EMI, without needing JEI
- Support for multi-output recipes and chance percentages

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
