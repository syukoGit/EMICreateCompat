package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.logistics.stockTicker.StockCheckingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public final class TickerPresence {

    private static final int TICKS_MISSING_BEFORE_UNBIND = 20;

    private static int ticksMissing;

    private TickerPresence() {
    }

    public static void verify() {
        NetworkBinding binding = BoundNetwork.current();
        if (binding == null || !isProvenMissing(binding)) {
            ticksMissing = 0;
            return;
        }

        ticksMissing++;
        if (ticksMissing < TICKS_MISSING_BEFORE_UNBIND) {
            return;
        }

        ticksMissing = 0;
        BoundNetwork.unbind();
        announceUnbind();
    }

    private static boolean isProvenMissing(NetworkBinding binding) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.dimension().equals(binding.dimension())) {
            return false;
        }

        BlockPos pos = binding.pos();
        if (level.getChunk(SectionPos.blockToSectionCoord(pos.getX()),
                           SectionPos.blockToSectionCoord(pos.getZ()),
                           ChunkStatus.FULL,
                           false) == null) {
            return false;
        }

        return !(level.getBlockEntity(pos) instanceof StockCheckingBlockEntity);
    }

    private static void announceUnbind() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        player.displayClientMessage(Component.translatable("gui.emicreatecompat.stock_keeper.unbound_missing"), true);
    }
}
