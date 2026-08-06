package fr.syuko.emicreatecompat.plugin;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.function.Function;

record RegisteredCategory(EmiRecipeCategory category, List<EmiStack> workstations,
                          Function<RecipeManager, List<? extends EmiRecipe>> recipes) {
}
