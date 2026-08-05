package fr.syuko.emicreatecompat.create.recipe;

public enum HeatRequirement {
    NONE(0xffffff, "create.recipe.heat_requirement.none"),
    HEATED(0xE88300, "create.recipe.heat_requirement.heated"),
    SUPERHEATED(0x5C93E8, "create.recipe.heat_requirement.superheated");

    private final int color;

    private final String translationKey;

    HeatRequirement(int color, String translationKey) {
        this.color = color;
        this.translationKey = translationKey;
    }

    public int color() {
        return color;
    }

    public String translationKey() {
        return translationKey;
    }

    public boolean needsBlazeBurner() {
        return this != NONE;
    }

    public boolean needsBlazeCake() {
        return this == SUPERHEATED;
    }
}
