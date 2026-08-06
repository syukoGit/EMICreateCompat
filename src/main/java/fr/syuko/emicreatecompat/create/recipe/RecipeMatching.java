package fr.syuko.emicreatecompat.create.recipe;

import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public final class RecipeMatching {

    private RecipeMatching() {
    }

    public static boolean inputsMatch(Recipe<?> first, Recipe<?> second) {
        if (first.getIngredients().isEmpty() || second.getIngredients().isEmpty()) {
            return false;
        }

        ItemStack[] matching = first.getIngredients().getFirst().getItems();
        return matching.length != 0 && second.getIngredients().getFirst().test(matching[0]);
    }

    public static boolean outputsMatch(Recipe<?> first, Recipe<?> second) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return false;
        }

        return ItemHelper.sameItem(first.getResultItem(level.registryAccess()),
                                   second.getResultItem(level.registryAccess()));
    }

    public static boolean noInputMatches(Recipe<?> recipe, List<? extends RecipeHolder<?>> others) {
        for (RecipeHolder<?> other : others) {
            if (inputsMatch(recipe, other.value())) {
                return false;
            }
        }

        return true;
    }

    public static boolean anyFullyMatches(Recipe<?> recipe, List<? extends RecipeHolder<?>> others) {
        for (RecipeHolder<?> other : others) {
            if (inputsMatch(recipe, other.value()) && outputsMatch(recipe, other.value())) {
                return true;
            }
        }

        return false;
    }
}
