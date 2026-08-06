package fr.syuko.emicreatecompat.category.fansmoking;

import com.simibubi.create.AllRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;

import java.util.ArrayList;
import java.util.List;

public final class FanSmokingRecipes {

    private FanSmokingRecipes() {
    }

    public static List<FanSmokingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        List<FanSmokingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<SmokingRecipe> holder : manager.getAllRecipesFor(RecipeType.SMOKING)) {
            if (!AllRecipeTypes.CAN_BE_AUTOMATED.test(holder)) {
                continue;
            }

            SmokingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            displays.add(new FanSmokingDisplay(holder.id(), recipe.getIngredients().getFirst(), output.copy()));
        }

        return displays;
    }
}
