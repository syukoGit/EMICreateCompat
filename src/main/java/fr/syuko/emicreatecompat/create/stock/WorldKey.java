package fr.syuko.emicreatecompat.create.stock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.regex.Pattern;

public final class WorldKey {

    private static final Pattern UNSAFE_CHARACTERS = Pattern.compile("[^A-Za-z0-9._-]");

    private static final String SINGLEPLAYER_PREFIX = "sp-";

    private static final String MULTIPLAYER_PREFIX = "mp-";

    private static final String UNKNOWN = "unknown";

    private WorldKey() {
    }

    public static String current() {
        Minecraft minecraft = Minecraft.getInstance();

        IntegratedServer singleplayerServer = minecraft.getSingleplayerServer();
        if (singleplayerServer != null) {
            Path levelDirectory = singleplayerServer.getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent();
            if (levelDirectory != null && levelDirectory.getFileName() != null) {
                return sanitize(SINGLEPLAYER_PREFIX + levelDirectory.getFileName());
            }
        }

        ServerData currentServer = minecraft.getCurrentServer();
        if (currentServer != null && !currentServer.ip.isBlank()) {
            return sanitize(MULTIPLAYER_PREFIX + currentServer.ip);
        }

        return UNKNOWN;
    }

    private static String sanitize(String raw) {
        return UNSAFE_CHARACTERS.matcher(raw).replaceAll("_");
    }
}
