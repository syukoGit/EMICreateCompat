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
import fr.syuko.emicreatecompat.Config;
import fr.syuko.emicreatecompat.TreeVisibility;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads the active EMI recipe tree ("Bill of Materials") and exposes every resource it involves —
 * the final result, the intermediates, and the base leaves — walking the whole node tree rather
 * than only the flattened leaf costs.
 *
 * <p>Nodes already satisfied by the inventory ({@link ProgressState#COMPLETED}) are skipped along
 * with their subtree, mirroring EMI's own calculation (which stops recursing once a node is met).
 * The tree costs are recomputed against the inventory first, exactly like EMI's BoMScreen does.
 */
public final class RecipeTreeResources {

    private RecipeTreeResources() {
    }

    /**
     * Side effect: when {@code extraStacks} is non-empty, EMI's own favorites/recipe-tree display is
     * re-driven off the same (player + extra) inventory so the native panel decrements too. Call
     * {@link #restoreNativeFavorites()} when the Stock Keeper closes to undo this.
     *
     * @param extraStacks additional stacks to treat as owned on top of the real player inventory
     *                    (e.g. items already queued in the Stock Keeper order basket, each with its
     *                    requested count). May be null or empty.
     * @return the set of {@link Item}s still needed anywhere in the active recipe tree (final +
     * intermediates + leaves), after accounting for the player inventory and {@code extraStacks}.
     */
    public static Set<Item> neededTreeItems(List<ItemStack> extraStacks) {
        if (!Config.enabled) {
            return Set.of();
        }

        MaterialTree tree = BoM.tree;
        if (tree == null || tree.goal == null) {
            return Set.of();
        }

        // Respect the visibility setting: optionally require EMI's recipe-tree (crafting) view.
        if (Config.treeVisibility == TreeVisibility.CRAFTING_MODE_ONLY && !BoM.craftingMode) {
            return Set.of();
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return Set.of();
        }

        // Build the inventory the exact same way EMI does (comparison -> DEFAULT_COMPARISON, so map
        // lookups against recipe ingredients match), straight from the real player inventory plus
        // any extra stacks such as the pending order basket.
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
            // Drive EMI's own favorites/recipe-tree display off the same (player + pending) inventory,
            // so the native panel decrements too. updateSynthetic recomputes the tree progress against
            // this inventory, leaving node states ready for collect() below.
            EmiFavorites.updateSynthetic(inventory);
        } else {
            tree.calculateProgress(inventory);
        }

        Set<Item> needed = new HashSet<>();
        collect(tree.goal, needed);
        return needed;
    }

    /**
     * Restores EMI's native favorites/recipe-tree display to the real player inventory. Call when the
     * Stock Keeper closes so the panel does not stay stuck on a pending-order snapshot.
     */
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

        // Resolution nodes are just the "this ingredient can be any of these; I picked X" indirection.
        // EMI never tracks their progress, and their ingredient is the full tag (all alternatives),
        // so we skip emitting it and only follow the concrete choice in the children.
        boolean resolution = node.recipe instanceof EmiResolutionRecipe;
        if (!resolution) {
            // A satisfied node also has a satisfied (and possibly stale) subtree — stop here.
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
