package fr.syuko.emicreatecompat;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Emicreatecompat.MODID)
public class Emicreatecompat {
    public static final String MODID = "emicreatecompat";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Emicreatecompat() {
        LOGGER.info("EMICreateCompat loading");
    }
}
