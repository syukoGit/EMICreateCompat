package fr.syuko.emicreatecompat.category.itemapplication;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemApplicationEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 60;

    private static final int PROCESSED_X = 26;

    private static final int PROCESSED_Y = 37;

    private static final int HELD_X = 50;

    private static final int HELD_Y = 4;

    private static final int OUTPUT_X = 131;

    private static final int OUTPUT_Y = 37;

    private static final int OUTPUT_SPACING = 19;

    private static final String NOT_CONSUMED_KEY = "create.recipe.deploying.not_consumed";

    private final Ingredient processed;

    private final boolean keepHeldItem;

    public ItemApplicationEmiRecipe(ItemApplicationDisplay display) {
        super(CreateEmiCategories.ITEM_APPLICATION, display.id(), WIDTH, HEIGHT);

        this.processed = display.processed();
        this.keepHeldItem = display.keepHeldItem();

        inputs.add(EmiIngredient.of(display.processed()));
        inputs.add(EmiIngredient.of(display.heldItem()));
        for (ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()).setChance(output.chance()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0,
                            0,
                            WIDTH,
                            HEIGHT,
                            (draw, mouseX, mouseY, delta) -> ItemApplicationRender.draw(draw, 0, 0, processed));
        widgets.addSlot(inputs.getFirst(), PROCESSED_X, PROCESSED_Y);

        var heldItem = widgets.addSlot(inputs.get(1), HELD_X, HELD_Y);
        if (keepHeldItem) {
            heldItem.appendTooltip(Component.translatable(NOT_CONSUMED_KEY).withStyle(ChatFormatting.GOLD));
        }

        for (int i = 0; i < outputs.size(); i++) {
            int x = OUTPUT_X + (i % 2) * OUTPUT_SPACING;
            int y = OUTPUT_Y - (i / 2) * OUTPUT_SPACING;
            widgets.addSlot(outputs.get(i), x, y).recipeContext(this);
        }
    }
}
