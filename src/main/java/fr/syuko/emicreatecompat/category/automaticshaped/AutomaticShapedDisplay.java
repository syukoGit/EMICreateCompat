package fr.syuko.emicreatecompat.category.automaticshaped;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record AutomaticShapedDisplay(ResourceLocation id, List<Ingredient> ingredients, int width, int height,
                                     ItemStack output) {

    private static final int SLOT_PITCH = 19;

    private static final int GRID_CENTER = 53;

    public int gridX() {
        return GRID_CENTER - width * SLOT_PITCH / 2;
    }

    public int gridY() {
        return GRID_CENTER - height * SLOT_PITCH / 2;
    }

    public int slotX(int index) {
        return gridX() + (index % width) * SLOT_PITCH;
    }

    public int slotY(int index) {
        return gridY() + (index / width) * SLOT_PITCH;
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
