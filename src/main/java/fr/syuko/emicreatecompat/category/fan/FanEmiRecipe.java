package fr.syuko.emicreatecompat.category.fan;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class FanEmiRecipe extends BasicEmiRecipe {

    protected static final int WIDTH = 178;

    protected static final int HEIGHT = 72;

    private static final int INPUT_X = 20;

    private static final int OUTPUT_X = 140;

    private static final int SLOT_Y = 47;

    private static final int SLOT_SPACING = 19;

    private static final int COLUMNS = 3;

    private static final int EXCESSIVE_OUTPUTS = 9;

    private static final int ARROW_X = 54;

    protected FanEmiRecipe(EmiRecipeCategory category, ResourceLocation id) {
        super(category, id, WIDTH, HEIGHT);
    }

    protected abstract void drawBackground(GuiGraphics graphics, int arrowX);

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int spread = 1 - Math.min(COLUMNS, outputs.size());
        int arrowX = 7 * spread + ARROW_X;
        boolean excessive = outputs.size() > EXCESSIVE_OUTPUTS;

        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> drawBackground(draw, arrowX));
        widgets.addSlot(inputs.getFirst(), 5 * spread + INPUT_X, SLOT_Y);

        for (int i = 0; i < outputs.size(); i++) {
            int x = OUTPUT_X + (i % COLUMNS) * SLOT_SPACING + 9 * spread;
            int
                    y =
                    SLOT_Y - (i / COLUMNS) * SLOT_SPACING + (excessive
                                                             ? 8
                                                             : 0);
            widgets.addSlot(outputs.get(i), x, y).recipeContext(this);
        }
    }
}
