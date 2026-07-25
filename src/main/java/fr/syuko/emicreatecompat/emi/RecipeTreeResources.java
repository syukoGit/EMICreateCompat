package fr.syuko.emicreatecompat.emi;

import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.bom.MaterialTree;
import dev.emi.emi.bom.ProgressState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * Reads the active EMI recipe tree ("Bill of Materials") and exposes every resource it involves —
 * the final result, the intermediates, and the base leaves — walking the whole node tree rather
 * than only the flattened leaf costs.
 *
 * <p>Nodes already satisfied by the player inventory ({@link ProgressState#COMPLETED}) are skipped,
 * so the set stays "what is still needed" at each level. The tree costs are recomputed against the
 * inventory first, exactly like EMI's own BoMScreen does.
 */
public final class RecipeTreeResources {

    private RecipeTreeResources() {
    }

    public static Set<Item> neededTreeItems() {
        MaterialTree tree = BoM.tree;
        if (tree == null || tree.goal == null) {
            return Set.of();
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return Set.of();
        }

        // Decrement the tree against the player's inventory so per-node progress is up to date.
        tree.calculateProgress(EmiPlayerInventory.of(minecraft.player));

        Set<Item> needed = new HashSet<>();
        collect(tree.goal, needed);
        return needed;
    }

    private static void collect(MaterialNode node, Set<Item> out) {
        if (node == null) {
            return;
        }
        if (node.progress != ProgressState.COMPLETED && node.ingredient != null) {
            for (EmiStack option : node.ingredient.getEmiStacks()) {
                ItemStack stack = option.getItemStack();
                if (stack != null && !stack.isEmpty()) {
                    out.add(stack.getItem());
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
