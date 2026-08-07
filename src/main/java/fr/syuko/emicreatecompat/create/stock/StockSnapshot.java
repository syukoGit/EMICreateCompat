package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.packager.InventorySummary;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record StockSnapshot(InventorySummary summary) {

    public static StockSnapshot of(List<BigItemStack> stacks) {
        InventorySummary summary = new InventorySummary();
        for (BigItemStack stack : stacks) {
            summary.add(stack);
        }
        return new StockSnapshot(summary);
    }

    public int countOf(ItemStack stack) {
        return summary.getCountOf(stack);
    }
}
