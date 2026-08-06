package fr.syuko.emicreatecompat.category.polishing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record PolishingDisplay(ResourceLocation id, Ingredient input, ItemStack output, float chance) {
}
