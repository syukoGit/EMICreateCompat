package fr.syuko.emicreatecompat.category.itemapplication;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.foundation.data.recipe.LogStrippingFakeRecipes;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class ItemApplicationRecipes {

    private ItemApplicationRecipes() {
    }

    public static List<ItemApplicationDisplay> all(RecipeManager manager) {
        RecipeType<ItemApplicationRecipe> itemApplication = AllRecipeTypes.ITEM_APPLICATION.getType();
        List<ItemApplicationDisplay> displays = new ArrayList<>();

        for (RecipeHolder<ItemApplicationRecipe> holder : manager.getAllRecipesFor(itemApplication)) {
            add(displays, holder.id(), holder.value());
        }

        for (RecipeHolder<ManualApplicationRecipe> holder : LogStrippingFakeRecipes.createRecipes()) {
            add(displays, holder.id(), holder.value());
        }

        return displays;
    }

    private static void add(List<ItemApplicationDisplay> displays, ResourceLocation id, ItemApplicationRecipe recipe) {
        if (recipe.getIngredients().size() < 2) {
            return;
        }

        List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
        if (outputs.isEmpty()) {
            return;
        }

        displays.add(new ItemApplicationDisplay(id,
                                                recipe.getProcessedItem(),
                                                recipe.getRequiredHeldItem(),
                                                recipe.shouldKeepHeldItem(),
                                                outputs));
    }
}
