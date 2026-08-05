package fr.syuko.emicreatecompat.category.deploying;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class DeployingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 70;

    private static final int PROCESSED_X = 26;

    private static final int PROCESSED_Y = 50;

    private static final int HELD_X = 50;

    private static final int HELD_Y = 4;

    private static final int OUTPUT_X = 131;

    private static final int OUTPUT_Y = 50;

    private static final int OUTPUT_SPACING = 19;

    private static final String NOT_CONSUMED_KEY = "create.recipe.deploying.not_consumed";

    private final boolean keepHeldItem;

    public DeployingEmiRecipe(DeployingDisplay display) {
        super(CreateEmiCategories.DEPLOYING, display.id(), WIDTH, HEIGHT);

        this.keepHeldItem = display.keepHeldItem();

        inputs.add(EmiIngredient.of(display.processed()));
        inputs.add(EmiIngredient.of(display.heldItem()));
        catalysts.add(EmiStack.of(AllBlocks.DEPLOYER.get()));
        catalysts.add(EmiStack.of(AllBlocks.DEPOT.get()));
        catalysts.add(EmiStack.of(AllItems.BELT_CONNECTOR.get()));
        for (ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()).setChance(output.chance()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int outputCount = outputs.size();
        widgets.addDrawable(0,
                            0,
                            WIDTH,
                            HEIGHT,
                            (draw, mouseX, mouseY, delta) -> DeployingRender.draw(draw, 0, 0, outputCount));
        widgets.addSlot(inputs.getFirst(), PROCESSED_X, PROCESSED_Y);

        SlotWidget heldItem = widgets.addSlot(inputs.get(1), HELD_X, HELD_Y);
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
