package fr.syuko.emicreatecompat.category.fanhaunting;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class FanHauntingRecipes {

    private FanHauntingRecipes() {
    }

    public static List<FanHauntingDisplay> all(RecipeManager manager) {
        RecipeType<HauntingRecipe> haunting = AllRecipeTypes.HAUNTING.getType();
        List<FanHauntingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<HauntingRecipe> holder : manager.getAllRecipesFor(haunting)) {
            HauntingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new FanHauntingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
        }

        return displays;
    }
}
