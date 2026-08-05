package fr.syuko.emicreatecompat.category.automaticshaped;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record AutomaticShapedDisplay(ResourceLocation id, List<Ingredient> ingredients, int width, int height,
                                     ItemStack output) {
}
