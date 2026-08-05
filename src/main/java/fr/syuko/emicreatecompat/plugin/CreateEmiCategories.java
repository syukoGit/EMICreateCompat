package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public final class CreateEmiCategories {
    private static final String CREATE_NAMESPACE = "create";

    public static final EmiRecipeCategory
            PRESSING =
            createCategory("pressing",
                           "create.recipe.pressing",
                           AllBlocks.MECHANICAL_PRESS.get(),
                           AllItems.IRON_SHEET.get());

    public static final EmiRecipeCategory
            SANDPAPER_POLISHING =
            createCategory("sandpaper_polishing",
                           "create.recipe.sandpaper_polishing",
                           EmiStack.of(AllItems.SAND_PAPER.get()));

    public static final EmiRecipeCategory
            MYSTERY_CONVERSION =
            createCategory("mystery_conversion",
                           "create.recipe.mystery_conversion",
                           EmiStack.of(AllBlocks.PECULIAR_BELL.get()));

    public static final EmiRecipeCategory
            MILLING =
            createCategory("milling", "create.recipe.milling", AllBlocks.MILLSTONE.get(), AllItems.WHEAT_FLOUR.get());

    public static final EmiRecipeCategory
            SAWING =
            createCategory("sawing", "create.recipe.sawing", AllBlocks.MECHANICAL_SAW.get(), Items.OAK_LOG);

    public static final EmiRecipeCategory
            ITEM_APPLICATION =
            createCategory("item_application",
                           "create.recipe.item_application",
                           EmiStack.of(AllItems.BRASS_HAND.get()));

    public static final EmiRecipeCategory
            FAN_WASHING =
            createCategory("fan_washing", "create.recipe.fan_washing", AllItems.PROPELLER.get(), Items.WATER_BUCKET);

    public static final EmiRecipeCategory
            FAN_SMOKING =
            createCategory("fan_smoking", "create.recipe.fan_smoking", AllItems.PROPELLER.get(), Items.CAMPFIRE);

    public static final EmiRecipeCategory
            FAN_HAUNTING =
            createCategory("fan_haunting", "create.recipe.fan_haunting", AllItems.PROPELLER.get(), Items.SOUL_CAMPFIRE);

    public static final EmiRecipeCategory
            CRUSHING =
            createCategory("crushing",
                           "create.recipe.crushing",
                           AllBlocks.CRUSHING_WHEEL.get(),
                           AllItems.CRUSHED_GOLD.get());

    public static final EmiRecipeCategory
            DEPLOYING =
            createCategory("deploying", "create.recipe.deploying", EmiStack.of(AllBlocks.DEPLOYER.get()));

    public static final EmiRecipeCategory
            FAN_BLASTING =
            createCategory("fan_blasting", "create.recipe.fan_blasting", AllItems.PROPELLER.get(), Items.LAVA_BUCKET);

    public static final EmiRecipeCategory
            BLOCK_CUTTING =
            createCategory("block_cutting",
                           "create.recipe.block_cutting",
                           AllBlocks.MECHANICAL_SAW.get(),
                           Items.STONE_BRICK_STAIRS);

    public static final EmiRecipeCategory
            AUTOMATIC_SHAPED =
            createCategory("automatic_shaped",
                           "create.recipe.automatic_shaped",
                           EmiStack.of(AllBlocks.MECHANICAL_CRAFTER.get()));

    private CreateEmiCategories() {
    }

    private static EmiRecipeCategory createCategory(String path, String nameKey, ItemLike primary, ItemLike secondary) {
        return createCategory(path,
                              nameKey,
                              new DoubleItemIcon(new ItemStack(primary), new ItemStack(secondary)),
                              EmiStack.of(primary));
    }

    private static EmiRecipeCategory createCategory(String path, String nameKey, EmiRenderable icon) {
        return createCategory(path, nameKey, icon, icon);
    }

    private static EmiRecipeCategory createCategory(String path,
                                                    String nameKey,
                                                    EmiRenderable icon,
                                                    EmiRenderable simplified) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CREATE_NAMESPACE, path);
        return new EmiRecipeCategory(id, icon, simplified) {
            @Override
            public Component getName() {
                return Component.translatable(nameKey);
            }
        };
    }
}
