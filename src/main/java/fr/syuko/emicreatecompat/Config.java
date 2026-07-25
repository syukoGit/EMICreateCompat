package fr.syuko.emicreatecompat;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Client config for the Stock Keeper recipe-tree category.
 */
@EventBusSubscriber(modid = Emicreatecompat.MODID)
public final class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Master switch: show the recipe-tree category at the top of Create's Stock Keeper.")
            .define("enabled", true);

    private static final ModConfigSpec.EnumValue<TreeVisibility> TREE_VISIBILITY = BUILDER
            .comment("When to show the category:",
                    " ALWAYS = as soon as an EMI recipe tree is active,",
                    " CRAFTING_MODE_ONLY = only while EMI's recipe-tree (crafting) view is enabled.")
            .defineEnum("treeVisibility", TreeVisibility.ALWAYS);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enabled = true;
    public static TreeVisibility treeVisibility = TreeVisibility.ALWAYS;

    private Config() {
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }
        enabled = ENABLED.get();
        treeVisibility = TREE_VISIBILITY.get();
    }
}
