package fr.syuko.emicreatecompat.category.spoutfilling;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record SpoutFillingDisplay(ResourceLocation id, Ingredient input, List<FluidStack> fluids, ItemStack output) {
}
