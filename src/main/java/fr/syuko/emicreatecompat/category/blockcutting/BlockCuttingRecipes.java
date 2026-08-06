package fr.syuko.emicreatecompat.category.blockcutting;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

public final class BlockCuttingRecipes {

    private static final int OUTPUTS_PER_GROUP = 15;

    private BlockCuttingRecipes() {
    }

    public static List<BlockCuttingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !AllConfigs.server().recipes.allowStonecuttingOnSaw.get()) {
            return List.of();
        }

        List<ResourceLocation> ids = new ArrayList<>();
        List<Ingredient> inputs = new ArrayList<>();
        List<List<ItemStack>> outputs = new ArrayList<>();

        for (RecipeHolder<StonecutterRecipe> holder : manager.getAllRecipesFor(RecipeType.STONECUTTING)) {
            if (AllRecipeTypes.shouldIgnoreInAutomation(holder)) {
                continue;
            }

            StonecutterRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty()) {
                continue;
            }

            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (result.isEmpty()) {
                continue;
            }

            Ingredient input = recipe.getIngredients().getFirst();
            int existing = indexOf(inputs, input);
            if (existing < 0) {
                ids.add(holder.id());
                inputs.add(input);
                outputs.add(new ArrayList<>(List.of(result.copy())));
            } else {
                outputs.get(existing).add(result.copy());
            }
        }

        List<BlockCuttingDisplay> displays = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            displays.add(new BlockCuttingDisplay(ids.get(i), inputs.get(i), group(outputs.get(i))));
        }

        return displays;
    }

    private static int indexOf(List<Ingredient> inputs, Ingredient input) {
        for (int i = 0; i < inputs.size(); i++) {
            if (ItemHelper.matchIngredients(input, inputs.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private static List<List<ItemStack>> group(List<ItemStack> stacks) {
        List<List<ItemStack>> groups = new ArrayList<>();

        for (int i = 0; i < stacks.size(); i++) {
            int group = i % OUTPUTS_PER_GROUP;
            if (group >= groups.size()) {
                groups.add(new ArrayList<>());
            }
            groups.get(group).add(stacks.get(i));
        }

        return List.copyOf(groups);
    }
}
