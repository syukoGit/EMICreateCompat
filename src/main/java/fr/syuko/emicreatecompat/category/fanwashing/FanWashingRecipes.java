package fr.syuko.emicreatecompat.category.fanwashing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class FanWashingRecipes {

    private FanWashingRecipes() {
    }

    public static List<FanWashingDisplay> all(RecipeManager manager) {
        RecipeType<SplashingRecipe> splashing = AllRecipeTypes.SPLASHING.getType();
        List<FanWashingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<SplashingRecipe> holder : manager.getAllRecipesFor(splashing)) {
            SplashingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new FanWashingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
        }

        return displays;
    }
}
