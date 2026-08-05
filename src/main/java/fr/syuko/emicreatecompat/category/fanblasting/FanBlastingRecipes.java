package fr.syuko.emicreatecompat.category.fanblasting;

import com.simibubi.create.AllRecipeTypes;
import fr.syuko.emicreatecompat.create.recipe.RecipeMatching;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class FanBlastingRecipes {

    private FanBlastingRecipes() {
    }

    public static List<FanBlastingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        List<RecipeHolder<? extends AbstractCookingRecipe>>
                blasting =
                new ArrayList<>(manager.getAllRecipesFor(RecipeType.BLASTING));
        List<RecipeHolder<? extends AbstractCookingRecipe>>
                smoking =
                new ArrayList<>(manager.getAllRecipesFor(RecipeType.SMOKING));

        List<RecipeHolder<? extends AbstractCookingRecipe>> selected = new ArrayList<>();

        for (RecipeHolder<? extends AbstractCookingRecipe> holder : manager.getAllRecipesFor(RecipeType.SMELTING)) {
            if (RecipeMatching.noInputMatches(holder.value(), blasting)) {
                selected.add(holder);
            }
        }

        selected.addAll(blasting);

        List<FanBlastingDisplay> displays = new ArrayList<>();
        for (RecipeHolder<? extends AbstractCookingRecipe> holder : selected) {
            if (!AllRecipeTypes.CAN_BE_AUTOMATED.test(holder) || RecipeMatching.anyFullyMatches(holder.value(),
                                                                                                smoking)) {
                continue;
            }

            AbstractCookingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            displays.add(new FanBlastingDisplay(holder.id(), recipe.getIngredients().getFirst(), output.copy()));
        }

        return displays;
    }
}
