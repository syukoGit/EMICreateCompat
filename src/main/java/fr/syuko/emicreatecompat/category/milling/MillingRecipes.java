package fr.syuko.emicreatecompat.category.milling;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class MillingRecipes {

    private MillingRecipes() {
    }

    public static List<MillingDisplay> all(RecipeManager manager) {
        RecipeType<AbstractCrushingRecipe> milling = AllRecipeTypes.MILLING.getType();
        List<MillingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<AbstractCrushingRecipe> holder : manager.getAllRecipesFor(milling)) {
            AbstractCrushingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new MillingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
        }

        return displays;
    }
}
