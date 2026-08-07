package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.BigItemStack;
import fr.syuko.emicreatecompat.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class StockTooltipLine {

    private static final String INFINITE = "∞";

    private StockTooltipLine() {
    }

    public static void append(ItemStack stack, List<Component> tooltip) {
        if (!Config.showStockInTooltips || stack.isEmpty()) {
            return;
        }

        StockSnapshot snapshot = StockSnapshotCache.current();
        if (snapshot == null) {
            return;
        }

        int count = snapshot.countOf(stack);
        if (count <= 0) {
            return;
        }

        String amount = count >= BigItemStack.INF
                        ? INFINITE
                        : String.valueOf(count);
        tooltip.add(Component.translatable("tooltip.emicreatecompat.stock_count", amount)
                             .withStyle(ChatFormatting.BLUE));
    }
}
