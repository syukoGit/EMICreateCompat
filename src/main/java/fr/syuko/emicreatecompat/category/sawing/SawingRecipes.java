package fr.syuko.emicreatecompat.category.sawing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class SawingRecipes {

    private SawingRecipes() {
    }

    public static List<SawingDisplay> all(RecipeManager manager) {
        RecipeType<CuttingRecipe> cutting = AllRecipeTypes.CUTTING.getType();
        List<SawingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<CuttingRecipe> holder : manager.getAllRecipesFor(cutting)) {
            CuttingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new SawingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
        }

        return displays;
    }
}
