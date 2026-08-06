package fr.syuko.emicreatecompat.category.draining;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public record DrainingDisplay(ResourceLocation id, Ingredient input, FluidStack fluid, ItemStack output) {
}
