package fr.syuko.emicreatecompat.category.automaticshaped;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

public final class AutomaticShapedRecipes {

    private AutomaticShapedRecipes() {
    }

    public static List<AutomaticShapedDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !AllConfigs.server().recipes.allowRegularCraftingInCrafter.get()) {
            return List.of();
        }

        List<AutomaticShapedDisplay> displays = new ArrayList<>();

        for (RecipeHolder<CraftingRecipe> holder : manager.getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            if (AllRecipeTypes.shouldIgnoreInAutomation(holder)) {
                continue;
            }

            boolean shaped = recipe instanceof ShapedRecipe;
            if (!shaped && recipe.getIngredients().size() != 1) {
                continue;
            }

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            int width = shaped
                        ? ((ShapedRecipe) recipe).getWidth()
                        : 1;
            int height = shaped
                         ? ((ShapedRecipe) recipe).getHeight()
                         : 1;

            displays.add(new AutomaticShapedDisplay(holder.id(),
                                                    List.copyOf(recipe.getIngredients()),
                                                    width,
                                                    height,
                                                    output.copy()));
        }

        return displays;
    }
}
