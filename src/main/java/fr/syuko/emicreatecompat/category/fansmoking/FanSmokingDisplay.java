package fr.syuko.emicreatecompat.category.fansmoking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record FanSmokingDisplay(ResourceLocation id, Ingredient input, ItemStack output) {
}
