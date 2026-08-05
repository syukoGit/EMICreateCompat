package fr.syuko.emicreatecompat.category.packing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinDisplays;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class PackingRecipes {

    private PackingRecipes() {
    }

    public static List<BasinDisplay> all(RecipeManager manager) {
        RecipeType<BasinRecipe> compacting = AllRecipeTypes.COMPACTING.getType();
        List<BasinDisplay> displays = new ArrayList<>();

        for (RecipeHolder<BasinRecipe> holder : manager.getAllRecipesFor(compacting)) {
            BasinDisplays.add(displays, holder.id(), holder.value());
        }

        return displays;
    }
}
