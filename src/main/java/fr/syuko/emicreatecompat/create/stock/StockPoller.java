package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.stockTicker.LogisticalStockRequestPacket;
import fr.syuko.emicreatecompat.config.Config;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;

public final class StockPoller {

    private static final long NEVER = -1L;

    private static final int MAX_RANGE_PADDING = 4096;

    private static final int STALE_INTERVALS = 3;

    private static long ticks;

    private static long lastRequestTick = NEVER;

    private static long lastResponseTick = NEVER;

    private StockPoller() {
    }

    public static void tick() {
        ticks++;

        if (Minecraft.getInstance().screen == null) {
            return;
        }

        NetworkBinding binding = BoundNetwork.current();
        if (binding == null || !availability().reachable()) {
            return;
        }

        long interval = Config.stockPollIntervalTicks;
        if (since(lastRequestTick) < interval || since(lastResponseTick) < interval) {
            return;
        }

        CatnipServices.NETWORK.sendToServer(new LogisticalStockRequestPacket(binding.pos()));
        lastRequestTick = ticks;
    }

    public static StockAvailability availability() {
        NetworkBinding binding = BoundNetwork.current();
        if (binding == null) {
            return StockAvailability.NOT_BOUND;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return StockAvailability.STALE;
        }
        if (!minecraft.level.dimension().equals(binding.dimension())) {
            return StockAvailability.WRONG_DIMENSION;
        }
        if (!minecraft.player.canInteractWithBlock(binding.pos(), MAX_RANGE_PADDING)) {
            return StockAvailability.OUT_OF_RANGE;
        }
        if (StockSnapshotCache.current() == null || since(lastResponseTick) > (long) Config.stockPollIntervalTicks * STALE_INTERVALS) {
            return StockAvailability.STALE;
        }
        return StockAvailability.LIVE;
    }

    public static void onSnapshotReceived() {
        lastResponseTick = ticks;
    }

    public static void reset() {
        lastRequestTick = NEVER;
        lastResponseTick = NEVER;
    }

    private static long since(long tick) {
        return tick == NEVER
               ? Long.MAX_VALUE
               : ticks - tick;
    }
}
