package fr.syuko.emicreatecompat.category.automaticshapeless;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.infrastructure.config.AllConfigs;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinDisplays;
import fr.syuko.emicreatecompat.create.recipe.BasinRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

public final class AutomaticShapelessRecipes {

    private AutomaticShapelessRecipes() {
    }

    public static List<BasinDisplay> all(RecipeManager manager) {
        if (Minecraft.getInstance().level == null || !AllConfigs.server().recipes.allowShapelessInMixer.get()) {
            return List.of();
        }

        List<BasinDisplay> displays = new ArrayList<>();

        for (RecipeHolder<CraftingRecipe> holder : manager.getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            if (recipe instanceof ShapedRecipe || BasinRecipes.present(recipe.getIngredients())
                                                              .size() <= 1 || MechanicalPressBlockEntity.canCompress(
                    recipe) || AllRecipeTypes.shouldIgnoreInAutomation(holder)) {
                continue;
            }

            RecipeHolder<BasinRecipe> converted = BasinRecipe.convertShapeless(holder);
            BasinDisplays.add(displays, holder.id(), converted.value());
        }

        return displays;
    }
}
