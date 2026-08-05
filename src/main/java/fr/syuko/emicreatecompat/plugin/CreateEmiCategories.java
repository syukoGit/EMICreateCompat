package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
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
