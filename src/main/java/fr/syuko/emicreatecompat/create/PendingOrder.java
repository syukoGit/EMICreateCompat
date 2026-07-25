package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapts Create's Stock Keeper order basket ({@code itemsToOrder}) into plain {@link ItemStack}s,
 * each expanded to its requested count, so the EMI recipe tree can treat queued items as owned.
 */
public final class PendingOrder {

    private PendingOrder() {
    }

    /**
     * @param itemsToOrder the Stock Keeper's pending order basket (may be null/empty)
     * @return one {@link ItemStack} per non-empty ordered entry, sized to its requested count
     */
    public static List<ItemStack> stacks(List<BigItemStack> itemsToOrder) {
        if (itemsToOrder == null || itemsToOrder.isEmpty()) {
            return List.of();
        }
        List<ItemStack> stacks = new ArrayList<>();
        for (BigItemStack ordered : itemsToOrder) {
            if (ordered.stack != null && !ordered.stack.isEmpty() && ordered.count > 0) {
                stacks.add(ordered.stack.copyWithCount(ordered.count));
            }
        }
        return stacks;
    }
}
