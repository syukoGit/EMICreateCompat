package fr.syuko.emicreatecompat.category.pressing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.world.item.ItemStack;
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

            List<PressingDisplay.ChancedStack> outputs = new ArrayList<>();
            for (ProcessingOutput result : recipe.getRollableResults()) {
                ItemStack stack = result.getStack();
                if (!stack.isEmpty()) {
                    outputs.add(new PressingDisplay.ChancedStack(stack, result.getChance()));
                }
            }
            if (outputs.isEmpty()) {
                continue;
            }

            displays.add(new PressingDisplay(holder.id(), recipe.getIngredients().getFirst(), List.copyOf(outputs)));
        }

        return displays;
    }
}
