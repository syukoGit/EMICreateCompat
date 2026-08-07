package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.BigItemStack;
import fr.syuko.emicreatecompat.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class StockTooltipLine {

    private static final String INFINITE = "∞";

    private static final String UNKNOWN = "?";

    private StockTooltipLine() {
    }

    public static void append(ItemStack stack, List<Component> tooltip) {
        if (!Config.showStockInTooltips || stack.isEmpty()) {
            return;
        }

        StockAvailability availability = StockPoller.availability();
        if (availability == StockAvailability.NOT_BOUND) {
            return;
        }
        if (availability != StockAvailability.LIVE) {
            tooltip.add(line(UNKNOWN, ChatFormatting.GRAY));
            return;
        }

        StockSnapshot snapshot = StockSnapshotCache.current();
        int count = snapshot == null
                    ? 0
                    : snapshot.countOf(stack);
        if (count <= 0) {
            return;
        }

        tooltip.add(line(count >= BigItemStack.INF
                         ? INFINITE
                         : String.valueOf(count), ChatFormatting.BLUE));
    }

    private static Component line(String amount, ChatFormatting style) {
        return Component.translatable("tooltip.emicreatecompat.stock_count", amount).withStyle(style);
    }
}
