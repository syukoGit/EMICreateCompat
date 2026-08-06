package fr.syuko.emicreatecompat.category.mechanicalcrafting;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class MechanicalCraftingRecipes {

    private MechanicalCraftingRecipes() {
    }

    public static List<MechanicalCraftingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        RecipeType<MechanicalCraftingRecipe> crafting = AllRecipeTypes.MECHANICAL_CRAFTING.getType();
        List<MechanicalCraftingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<MechanicalCraftingRecipe> holder : manager.getAllRecipesFor(crafting)) {
            MechanicalCraftingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            displays.add(new MechanicalCraftingDisplay(holder.id(),
                                                       List.copyOf(recipe.getIngredients()),
                                                       recipe.getWidth(),
                                                       recipe.getHeight(),
                                                       output.copy()));
        }

        return displays;
    }
}
