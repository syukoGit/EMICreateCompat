package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.stockTicker.LogisticalStockResponsePacket;
import fr.syuko.emicreatecompat.create.stock.StockSnapshotCache;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogisticalStockResponsePacket.class)
public abstract class LogisticalStockResponsePacketMixin {

    @Inject(method = "handle", at = @At("HEAD"), remap = false)
    private void emicreatecompat$captureBoundStock(LocalPlayer player, CallbackInfo ci) {
        LogisticalStockResponsePacket packet = (LogisticalStockResponsePacket) (Object) this;
        StockSnapshotCache.accept(packet.pos(), packet.items(), packet.lastPacket());
    }
}
