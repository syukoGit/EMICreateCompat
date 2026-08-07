package fr.syuko.emicreatecompat;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import java.util.List;

@EventBusSubscriber(modid = Emicreatecompat.MODID, value = Dist.CLIENT)
public final class MixinTargetSelfCheck {

    private static final String ENABLED_PROPERTY = "emicreatecompat.selfCheck";

    private static final String LOG_PREFIX = "[self-check] ";

    private static final List<String> MIXIN_TARGETS = List.of("dev.emi.emi.bom.ChanceState",
                                                              "dev.emi.emi.bom.MaterialNode",
                                                              "dev.emi.emi.bom.TreeCost",
                                                              "dev.emi.emi.runtime.EmiFavorites",
                                                              "dev.emi.emi.screen.BoMScreen",
                                                              "dev.emi.emi.screen.BoMScreen$Node",
                                                              "dev.emi.emi.screen.BoMScreen$Hover",
                                                              "com.simibubi.create.content.logistics.stockTicker.LogisticalStockResponsePacket",
                                                              "com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen",
                                                              "com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen$CategoryEntry");

    private static final Logger LOGGER = LogUtils.getLogger();

    private MixinTargetSelfCheck() {
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        if (!Boolean.getBoolean(ENABLED_PROPERTY)) {
            return;
        }

        event.enqueueWork(() -> System.exit(applyEveryMixin()));
    }

    private static int applyEveryMixin() {
        int failures = 0;

        for (String target : MIXIN_TARGETS) {
            try {
                Class.forName(target, false, MixinTargetSelfCheck.class.getClassLoader());
                LOGGER.info("{}applied on {}", LOG_PREFIX, target);
            } catch (Throwable failure) {
                failures++;
                LOGGER.error("{}FAILED on {}", LOG_PREFIX, target, failure);
            }
        }

        LOGGER.info("{}{} of {} targets loaded", LOG_PREFIX, MIXIN_TARGETS.size() - failures, MIXIN_TARGETS.size());
        return failures == 0
               ? 0
               : 1;
    }
}
