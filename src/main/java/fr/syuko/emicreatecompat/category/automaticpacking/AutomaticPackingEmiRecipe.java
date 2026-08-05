package fr.syuko.emicreatecompat.category.automaticpacking;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinEmiRecipe;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class AutomaticPackingEmiRecipe extends BasinEmiRecipe {

    private static final int HEIGHT = 85;

    private static final int WIDE_INPUT_X = 17;

    private static final int NARROW_INPUT_X = 26;

    private static final int INPUT_Y = 50;

    private static final int OUTPUT_X = 141;

    private static final int OUTPUT_Y = 50;

    private static final int SLOT_PITCH = 19;

    public AutomaticPackingEmiRecipe(BasinDisplay display) {
        super(CreateEmiCategories.AUTOMATIC_PACKING, display, HEIGHT);

        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()));
    }

    @Override
    protected void addInputSlots(WidgetHolder widgets) {
        int count = inputs.size();
        int
                columns =
                count == 4
                ? 2
                : 3;
        int
                originX =
                columns == 2
                ? NARROW_INPUT_X
                : WIDE_INPUT_X;

        for (int i = 0; i < count; i++) {
            int x = originX + (i % columns) * SLOT_PITCH;
            int y = INPUT_Y - (i / columns) * SLOT_PITCH;
            widgets.addSlot(inputs.get(i), x, y);
        }
    }

    @Override
    protected void addOutputSlots(WidgetHolder widgets) {
        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int outputRows) {
        AutomaticPackingRender.draw(graphics, 0, 0);
    }
}
