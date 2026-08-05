package fr.syuko.emicreatecompat.category.pressing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record PressingDisplay(ResourceLocation id, Ingredient input, List<ChancedStack> outputs) {

    public record ChancedStack(ItemStack stack, float chance) {
    }
}
