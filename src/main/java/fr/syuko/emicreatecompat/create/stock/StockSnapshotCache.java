package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class StockSnapshotCache {

    private static List<BigItemStack> pending;

    private static StockSnapshot snapshot;

    private StockSnapshotCache() {
    }

    public static void accept(BlockPos pos, List<BigItemStack> items, boolean lastPacket) {
        if (!BoundNetwork.matchesCurrentLevel(pos)) {
            return;
        }

        if (pending == null) {
            pending = new ArrayList<>();
        }
        pending.addAll(items);

        if (!lastPacket) {
            return;
        }
        snapshot = StockSnapshot.of(pending);
        pending = null;
        StockPoller.onSnapshotReceived();
    }

    public static StockSnapshot current() {
        return snapshot;
    }

    public static void clear() {
        pending = null;
        snapshot = null;
        StockPoller.reset();
    }
}
