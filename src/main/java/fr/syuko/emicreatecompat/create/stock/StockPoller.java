package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.stockTicker.LogisticalStockRequestPacket;
import fr.syuko.emicreatecompat.config.Config;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;

public final class StockPoller {

    private static final long NEVER = -1L;

    private static final int MAX_RANGE_PADDING = 4096;

    private static final int STALE_INTERVALS = 3;

    private static final int UNANSWERED_REQUESTS_BEFORE_BACKOFF = 3;

    private static final int BACKOFF_INTERVAL_TICKS = 600;

    private static long ticks;

    private static long lastRequestTick = NEVER;

    private static long lastResponseTick = NEVER;

    private static int unansweredRequests;

    private StockPoller() {
    }

    public static void tick() {
        ticks++;

        if (!Config.showStockInTooltips || Minecraft.getInstance().screen == null || Goggles.missing()) {
            return;
        }

        NetworkBinding binding = BoundNetwork.current();
        if (binding == null || !availability().reachable()) {
            return;
        }

        long interval = pollInterval();
        if (since(lastRequestTick) < interval || since(lastResponseTick) < interval) {
            return;
        }

        if (lastRequestTick != NEVER && lastRequestTick > lastResponseTick) {
            unansweredRequests++;
        }

        CatnipServices.NETWORK.sendToServer(new LogisticalStockRequestPacket(binding.pos()));
        lastRequestTick = ticks;
    }

    private static long pollInterval() {
        return unansweredRequests >= UNANSWERED_REQUESTS_BEFORE_BACKOFF
               ? BACKOFF_INTERVAL_TICKS
               : Config.stockPollIntervalTicks;
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
        unansweredRequests = 0;
    }

    public static void reset() {
        lastRequestTick = NEVER;
        lastResponseTick = NEVER;
        unansweredRequests = 0;
    }

    private static long since(long tick) {
        return tick == NEVER
               ? Long.MAX_VALUE
               : ticks - tick;
    }
}
