package fr.syuko.emicreatecompat.category.mechanicalcrafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record MechanicalCraftingDisplay(ResourceLocation id, List<Ingredient> ingredients, int width, int height,
                                        ItemStack output) {

    private static final int SLOT_PITCH = 19;

    private static final int GRID_CENTER = 53;

    private static final int MAX_GRID_SIZE = 100;

    public float scale() {
        return Math.min(1, MAX_GRID_SIZE / ((float) SLOT_PITCH * Math.max(width, height)));
    }

    public int gridX() {
        return GRID_CENTER - (int) (scale() * width * SLOT_PITCH * .5);
    }

    public int gridY() {
        return GRID_CENTER - (int) (scale() * height * SLOT_PITCH * .5);
    }

    public int slotX(int index) {
        int column = index % width;
        return gridX() + Math.round(column * SLOT_PITCH * scale());
    }

    public int slotY(int index) {
        int row = index / width;
        return gridY() + Math.round(row * SLOT_PITCH * scale());
    }

    public int filledCount() {
        int amount = 0;

        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                amount++;
            }
        }

        return amount;
    }
}
