package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class PendingOrder {

    private PendingOrder() {
    }

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
