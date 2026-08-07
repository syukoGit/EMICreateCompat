package fr.syuko.emicreatecompat.create.stock;

import fr.syuko.emicreatecompat.Emicreatecompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Emicreatecompat.MODID, value = Dist.CLIENT)
public final class StockClientEvents {

    private StockClientEvents() {
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        TickerPresence.verify();
        StockPoller.tick();
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        StockTooltipLine.append(event.getItemStack(), event.getToolTip());
    }

    @SubscribeEvent
    static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        BoundNetwork.enterWorld(WorldKey.current());
    }

    @SubscribeEvent
    static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        BoundNetwork.leaveWorld();
    }
}
