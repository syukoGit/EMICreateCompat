package fr.syuko.emicreatecompat.create.stock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import fr.syuko.emicreatecompat.Emicreatecompat;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class BindingStore {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final String DIRECTORY = "binds";

    private static final String EXTENSION = ".json";

    private BindingStore() {
    }

    public static Optional<NetworkBinding> load(String worldKey) {
        Path file = file(worldKey);
        if (!Files.isRegularFile(file)) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonElement json = GSON.fromJson(reader, JsonElement.class);
            return NetworkBinding.CODEC.parse(JsonOps.INSTANCE, json)
                                       .resultOrPartial(error -> LOGGER.warn(
                                               "Discarding unreadable stock binding {}: {}",
                                               file,
                                               error));
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not read stock binding {}", file, exception);
            return Optional.empty();
        }
    }

    public static void save(String worldKey, NetworkBinding binding) {
        Optional<JsonElement> json = NetworkBinding.CODEC.encodeStart(JsonOps.INSTANCE, binding)
                                                         .resultOrPartial(error -> LOGGER.warn(
                                                                 "Could not encode stock binding: {}",
                                                                 error));
        if (json.isEmpty()) {
            return;
        }

        Path file = file(worldKey);
        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(json.get(), writer);
            }
        } catch (IOException exception) {
            LOGGER.warn("Could not write stock binding {}", file, exception);
        }
    }

    public static void clear(String worldKey) {
        Path file = file(worldKey);
        try {
            Files.deleteIfExists(file);
        } catch (IOException exception) {
            LOGGER.warn("Could not delete stock binding {}", file, exception);
        }
    }

    private static Path file(String worldKey) {
        return FMLPaths.GAMEDIR.get().resolve(Emicreatecompat.MODID).resolve(DIRECTORY).resolve(worldKey + EXTENSION);
    }
}
