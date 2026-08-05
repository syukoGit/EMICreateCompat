package fr.syuko.emicreatecompat.category.fanblasting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record FanBlastingDisplay(ResourceLocation id, Ingredient input, ItemStack output) {
}
