package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class CreateEmiCategories {
    private static final String CREATE_NAMESPACE = "create";

    public static final EmiRecipeCategory
            PRESSING =
            createCategory("pressing", "create.recipe.pressing", EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()));

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

    private CreateEmiCategories() {
    }

    private static EmiRecipeCategory createCategory(String path, String nameKey, EmiRenderable icon) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CREATE_NAMESPACE, path);
        return new EmiRecipeCategory(id, icon) {
            @Override
            public Component getName() {
                return Component.translatable(nameKey);
            }
        };
    }
}
