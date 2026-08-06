package fr.syuko.emicreatecompat.category.polishing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class PolishingRecipes {

    private PolishingRecipes() {
    }

    public static List<PolishingDisplay> all(RecipeManager manager) {
        RecipeType<SandPaperPolishingRecipe> polishing = AllRecipeTypes.SANDPAPER_POLISHING.getType();
        List<PolishingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<SandPaperPolishingRecipe> holder : manager.getAllRecipesFor(polishing)) {
            SandPaperPolishingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty() || recipe.getRollableResults().isEmpty()) {
                continue;
            }

            ProcessingOutput result = recipe.getRollableResults().getFirst();
            ItemStack stack = result.getStack();
            if (stack.isEmpty()) {
                continue;
            }

            displays.add(new PolishingDisplay(holder.id(),
                                              recipe.getIngredients().getFirst(),
                                              stack,
                                              result.getChance()));
        }

        return displays;
    }
}
