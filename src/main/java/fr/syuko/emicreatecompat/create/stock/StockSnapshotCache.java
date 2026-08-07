package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.BigItemStack;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class StockSnapshotCache {

    private static List<BigItemStack> pending;

    private static BlockPos pendingPos;

    private static StockSnapshot snapshot;

    private StockSnapshotCache() {
    }

    public static void accept(BlockPos pos, List<BigItemStack> items, boolean lastPacket) {
        if (!BoundNetwork.matchesCurrentLevel(pos)) {
            discardPending();
            return;
        }

        if (pending == null || !pos.equals(pendingPos)) {
            pending = new ArrayList<>();
            pendingPos = pos;
        }
        pending.addAll(items);

        if (!lastPacket) {
            return;
        }
        snapshot = StockSnapshot.of(pending);
        discardPending();
        StockPoller.onSnapshotReceived();
    }

    public static StockSnapshot current() {
        return snapshot;
    }

    public static void clear() {
        discardPending();
        snapshot = null;
        StockPoller.reset();
    }

    private static void discardPending() {
        pending = null;
        pendingPos = null;
    }
}
