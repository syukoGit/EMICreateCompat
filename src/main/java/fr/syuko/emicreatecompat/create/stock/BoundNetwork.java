package fr.syuko.emicreatecompat.create.stock;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class BoundNetwork {

    private static String worldKey;

    private static NetworkBinding binding;

    private BoundNetwork() {
    }

    public static void enterWorld(String key) {
        worldKey = key;
        binding = BindingStore.load(key).orElse(null);
    }

    public static void leaveWorld() {
        worldKey = null;
        binding = null;
        StockSnapshotCache.clear();
    }

    public static NetworkBinding current() {
        return binding;
    }

    public static boolean isBoundTo(ResourceKey<Level> dimension, BlockPos pos) {
        return binding != null && binding.dimension().equals(dimension) && binding.pos().equals(pos);
    }

    public static boolean matchesCurrentLevel(BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        return level != null && isBoundTo(level.dimension(), pos);
    }

    public static void toggle(ResourceKey<Level> dimension, BlockPos pos) {
        if (isBoundTo(dimension, pos)) {
            unbind();
            return;
        }
        bind(new NetworkBinding(dimension, pos));
    }

    public static void bind(NetworkBinding value) {
        binding = value;
        StockSnapshotCache.clear();
        if (worldKey != null) {
            BindingStore.save(worldKey, value);
        }
    }

    public static void unbind() {
        binding = null;
        StockSnapshotCache.clear();
        if (worldKey != null) {
            BindingStore.clear(worldKey);
        }
    }
}
