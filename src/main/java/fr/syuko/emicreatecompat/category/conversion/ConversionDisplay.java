package fr.syuko.emicreatecompat.category.conversion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ConversionDisplay(ResourceLocation id, ItemStack input, ItemStack output) {
}
