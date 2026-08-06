package fr.syuko.emicreatecompat.config;

import fr.syuko.emicreatecompat.Emicreatecompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Emicreatecompat.MODID)
public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue
            ENABLED =
            BUILDER.comment("Master switch: show the recipe-tree category at the top of Create's Stock Keeper.")
                   .define("enabled", true);

    private static final ModConfigSpec.EnumValue<TreeVisibility>
            TREE_VISIBILITY =
            BUILDER.comment("When to show the category:",
                            " ALWAYS = as soon as an EMI recipe tree is active,",
                            " CRAFTING_MODE_ONLY = only while EMI's recipe-tree (crafting) view is enabled.")
                   .defineEnum("treeVisibility", TreeVisibility.ALWAYS);

    private static final ModConfigSpec.BooleanValue
            COUNT_PENDING_ORDER =
            BUILDER.comment("Count items already queued in the Stock Keeper order basket",
                            "(the list before you press send) as if they were in the player inventory, ",
                            "so the recipe tree updates live as you build your order.")
                   .define("countPendingOrder", true);

    private static final ModConfigSpec.EnumValue<RecipeRegistration>
            RECIPE_REGISTRATION =
            BUILDER.comment("Whether to register Create's machine recipes as native EMI categories:",
                            " AUTO = only when JEI is absent, because EMI already bridges Create's JEI plugin",
                            "        and registering on top of it would show every recipe twice,",
                            " ALWAYS = register even when JEI is present (expect duplicated categories),",
                            " NEVER = never register them.",
                            "Applies on the next EMI reload: rejoin a world or reload resources.")
                   .defineEnum("recipeRegistration", RecipeRegistration.AUTO);

    private static final ModConfigSpec.EnumValue<ChanceAmounts>
            CHANCE_AMOUNTS =
            BUILDER.comment("How EMI's recipe tree shows amounts for outputs that carry a produce chance:",
                            " EXPECTED = EMI's own behavior, the average cost of one success",
                            "            (round(amount / chance)), marked with a gold approximation sign,",
                            " RAW = the amounts the recipes actually declare, with no gold highlight,",
                            "       and the expected cost moved to the tooltip of the Total Cost row.",
                            "       Crafting progress then counts one for one.",
                            "Applies to every mod's chanced recipes, not only Create's.")
                   .defineEnum("chanceAmounts", ChanceAmounts.EXPECTED);

    private static final ModConfigSpec.BooleanValue
            ALIGN_CHANCED_FAVORITES =
            BUILDER.comment("Make the synthetic favorites of a chanced recipe show the same amount as the",
                            "recipe tree does. EMI counts them from the raw amount while the tree displays",
                            "the chanced one, so a step reading 6 in the tree shows 5 in the favorites bar",
                            "and only drops once two items have been gathered.",
                            "Applies to every mod's chanced recipes, not only Create's.")
                   .define("alignChancedFavorites", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enabled = true;

    public static TreeVisibility treeVisibility = TreeVisibility.ALWAYS;

    public static boolean countPendingOrder = true;

    public static RecipeRegistration recipeRegistration = RecipeRegistration.AUTO;

    public static ChanceAmounts chanceAmounts = ChanceAmounts.EXPECTED;

    public static boolean alignChancedFavorites = true;

    private Config() {
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }
        enabled = ENABLED.get();
        treeVisibility = TREE_VISIBILITY.get();
        countPendingOrder = COUNT_PENDING_ORDER.get();
        recipeRegistration = RECIPE_REGISTRATION.get();
        chanceAmounts = CHANCE_AMOUNTS.get();
        alignChancedFavorites = ALIGN_CHANCED_FAVORITES.get();
    }
}
