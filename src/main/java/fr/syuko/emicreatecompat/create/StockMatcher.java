package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class StockMatcher {

    private StockMatcher() {
    }

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
