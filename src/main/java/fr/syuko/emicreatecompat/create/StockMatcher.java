package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Selects the network-stock entries that satisfy a set of needed resources, de-duplicated by item.
 * Pure over its inputs (no game state, no side effects) so the matching rule can be reasoned about
 * independently of the mixin that feeds it Create's live item source.
 */
public final class StockMatcher {

    private StockMatcher() {
    }

    /**
     * @param needed     the items the active EMI recipe tree still requires
     * @param itemSource Create's stock, grouped by category ({@code currentItemSource})
     * @return the first stock entry seen for each needed item, in source order
     */
    public static List<BigItemStack> match(Set<Item> needed, List<List<BigItemStack>> itemSource) {
        if (needed.isEmpty() || itemSource == null) {
            return List.of();
        }
        List<BigItemStack> matches = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (List<BigItemStack> category : itemSource) {
            for (BigItemStack entry : category) {
                if (entry.stack == null || entry.stack.isEmpty()) {
                    continue;
                }
                Item item = entry.stack.getItem();
                if (needed.contains(item) && seen.add(item)) {
                    matches.add(entry);
                }
            }
        }
        return matches;
    }
}
