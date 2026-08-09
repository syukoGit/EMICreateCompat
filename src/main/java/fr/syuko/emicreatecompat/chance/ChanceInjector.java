package fr.syuko.emicreatecompat.chance;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.RecipeChances;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public final class ChanceInjector {

    private ChanceInjector() {
    }

    public static void applyTo(List<EmiRecipe> recipes) {
        for (EmiRecipe recipe : recipes) {
            apply(recipe);
        }
    }

    private static void apply(EmiRecipe recipe) {
        RecipeHolder<?> holder = recipe.getBackingRecipe();
        if (holder == null) {
            return;
        }

        List<ChancedStack> chances = RecipeChances.of(holder.value());
        if (chances.isEmpty()) {
            return;
        }

        List<ChancedStack> unmatched = new ArrayList<>(chances);
        for (EmiStack output : recipe.getOutputs()) {
            if (output.getChance() != 1f) {
                continue;
            }

            ChancedStack match = take(unmatched, output);
            if (match != null) {
                output.setChance(match.chance());
            }
        }
    }

    private static ChancedStack take(List<ChancedStack> unmatched, EmiStack output) {
        Item item = output.getKeyOfType(Item.class);
        if (item == null) {
            return null;
        }

        for (int i = 0; i < unmatched.size(); i++) {
            if (unmatched.get(i).stack().is(item)) {
                return unmatched.remove(i);
            }
        }

        return null;
    }
}
