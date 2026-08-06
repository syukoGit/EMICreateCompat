package fr.syuko.emicreatecompat.category.crushing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import fr.syuko.emicreatecompat.create.recipe.RecipeMatching;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class CrushingRecipes {

    private CrushingRecipes() {
    }

    public static List<CrushingDisplay> all(RecipeManager manager) {
        RecipeType<AbstractCrushingRecipe> crushing = AllRecipeTypes.CRUSHING.getType();
        RecipeType<AbstractCrushingRecipe> milling = AllRecipeTypes.MILLING.getType();

        List<RecipeHolder<AbstractCrushingRecipe>> crushingRecipes = manager.getAllRecipesFor(crushing);
        List<CrushingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<AbstractCrushingRecipe> holder : crushingRecipes) {
            add(displays, holder);
        }

        for (RecipeHolder<AbstractCrushingRecipe> holder : manager.getAllRecipesFor(milling)) {
            if (RecipeMatching.noInputMatches(holder.value(), crushingRecipes)) {
                add(displays, holder);
            }
        }

        return displays;
    }

    private static void add(List<CrushingDisplay> displays, RecipeHolder<AbstractCrushingRecipe> holder) {
        AbstractCrushingRecipe recipe = holder.value();
        if (recipe.getIngredients().isEmpty()) {
            return;
        }

        List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
        if (outputs.isEmpty()) {
            return;
        }

        displays.add(new CrushingDisplay(holder.id(), recipe.getIngredients().getFirst(), outputs));
    }
}
