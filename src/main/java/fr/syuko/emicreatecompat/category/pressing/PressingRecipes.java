package fr.syuko.emicreatecompat.category.pressing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class PressingRecipes {

    private PressingRecipes() {
    }

    public static List<PressingDisplay> all(RecipeManager manager) {
        RecipeType<PressingRecipe> pressing = AllRecipeTypes.PRESSING.getType();
        List<PressingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<PressingRecipe> holder : manager.getAllRecipesFor(pressing)) {
            PressingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new PressingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
        }

        return displays;
    }
}
