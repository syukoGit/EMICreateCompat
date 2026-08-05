package fr.syuko.emicreatecompat.category.basin;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.CountedIngredient;
import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record BasinDisplay(ResourceLocation id, List<CountedIngredient> itemInputs, List<List<FluidStack>> fluidInputs,
                           List<ChancedStack> itemOutputs, List<FluidStack> fluidOutputs, HeatRequirement heat) {
}
