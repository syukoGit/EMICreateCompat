package fr.syuko.emicreatecompat.category.automaticpacking;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.CountedIngredient;
import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

public final class AutomaticPackingRecipes {

    private AutomaticPackingRecipes() {
    }

    public static List<BasinDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !AllConfigs.server().recipes.allowShapedSquareInPress.get()) {
            return List.of();
        }

        List<BasinDisplay> displays = new ArrayList<>();

        for (RecipeHolder<?> holder : manager.getRecipes()) {
            if (!(holder.value() instanceof CraftingRecipe recipe) || recipe instanceof MechanicalCraftingRecipe || !MechanicalPressBlockEntity.canCompress(
                    recipe) || AllRecipeTypes.shouldIgnoreInAutomation(holder)) {
                continue;
            }

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            List<CountedIngredient> inputs = new ArrayList<>();
            for (Ingredient ingredient : recipe.getIngredients()) {
                inputs.add(new CountedIngredient(ingredient, 1));
            }

            displays.add(new BasinDisplay(holder.id(),
                                          List.copyOf(inputs),
                                          List.of(),
                                          List.of(new ChancedStack(output.copy(), 1f)),
                                          List.of(),
                                          HeatRequirement.NONE));
        }

        return displays;
    }
}
