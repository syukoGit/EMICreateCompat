package fr.syuko.emicreatecompat;

import com.mojang.logging.LogUtils;
import fr.syuko.emicreatecompat.config.Config;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Emicreatecompat.MODID)
public class Emicreatecompat {
    public static final String MODID = "emicreatecompat";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Emicreatecompat(ModContainer modContainer) {
        LOGGER.info("EMI for Create loading");
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}
