package fr.syuko.emicreatecompat.category.sequencedassembly;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record SequencedAssemblyDisplay(ResourceLocation id, Ingredient input, ItemStack output, float outputChance,
                                       int loops, List<AssemblyStep> steps) {
}
