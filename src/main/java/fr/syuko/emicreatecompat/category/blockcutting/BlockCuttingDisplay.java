package fr.syuko.emicreatecompat.category.blockcutting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record BlockCuttingDisplay(ResourceLocation id, Ingredient input, List<List<ItemStack>> outputGroups) {
}
