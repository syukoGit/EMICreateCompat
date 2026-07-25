package fr.syuko.emicreatecompat.emi;

import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiResolutionRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.bom.MaterialTree;
import dev.emi.emi.bom.ProgressState;
import dev.emi.emi.runtime.EmiFavorites;
import fr.syuko.emicreatecompat.config.Config;
import fr.syuko.emicreatecompat.config.TreeVisibility;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EmiRecipeTreeReader {

    private EmiRecipeTreeReader() {
    }

    public static Set<Item> neededTreeItems(List<ItemStack> extraStacks) {
        if (!Config.enabled) {
            return Set.of();
        }

        MaterialTree tree = BoM.tree;
        if (tree == null || tree.goal == null) {
            return Set.of();
        }

        if (Config.treeVisibility == TreeVisibility.CRAFTING_MODE_ONLY && !BoM.craftingMode) {
            return Set.of();
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return Set.of();
        }

        List<EmiStack> stacks = new ArrayList<>();
        Inventory playerInventory = minecraft.player.getInventory();
        for (int slot = 0; slot < playerInventory.getContainerSize(); slot++) {
            ItemStack held = playerInventory.getItem(slot);
            if (!held.isEmpty()) {
                stacks.add(toInventoryStack(held));
            }
        }
        boolean hasExtra = false;
        if (extraStacks != null) {
            for (ItemStack extra : extraStacks) {
                if (extra != null && !extra.isEmpty()) {
                    stacks.add(toInventoryStack(extra));
                    hasExtra = true;
                }
            }
        }

        EmiPlayerInventory inventory = new EmiPlayerInventory(stacks);
        if (hasExtra) {
            EmiFavorites.updateSynthetic(inventory);
        } else {
            tree.calculateProgress(inventory);
        }

        Set<Item> needed = new HashSet<>();
        collect(tree.goal, needed);
        return needed;
    }

    public static void restoreNativeFavorites() {
        if (BoM.tree == null) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            EmiFavorites.updateSynthetic(EmiPlayerInventory.of(minecraft.player));
        }
    }

    private static EmiStack toInventoryStack(ItemStack stack) {
        return EmiStack.of(stack).comparison(c -> Comparison.DEFAULT_COMPARISON).setAmount(stack.getCount());
    }

    private static void collect(MaterialNode node, Set<Item> out) {
        if (node == null) {
            return;
        }

        boolean resolution = node.recipe instanceof EmiResolutionRecipe;
        if (!resolution) {
            if (node.progress == ProgressState.COMPLETED) {
                return;
            }
            if (node.ingredient != null) {
                for (EmiStack option : node.ingredient.getEmiStacks()) {
                    ItemStack stack = option.getItemStack();
                    if (stack != null && !stack.isEmpty()) {
                        out.add(stack.getItem());
                    }
                }
            }
        }

        if (node.children != null) {
            for (MaterialNode child : node.children) {
                collect(child, out);
            }
        }
    }
}
