package fr.syuko.emicreatecompat.category.mechanicalcrafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record MechanicalCraftingDisplay(ResourceLocation id, List<Ingredient> ingredients, int width, int height,
                                        ItemStack output) {

    public float scale() {
        return Math.min(1, 100 / (19f * Math.max(width, height)));
    }

    public int gridX() {
        return 3 + 50 - (int) (scale() * width * 19 * .5);
    }

    public int gridY() {
        return 3 + 50 - (int) (scale() * height * 19 * .5);
    }
}
