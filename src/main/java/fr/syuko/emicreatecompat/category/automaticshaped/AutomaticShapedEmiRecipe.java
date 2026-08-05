package fr.syuko.emicreatecompat.category.automaticshaped;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.world.item.crafting.Ingredient;

public class AutomaticShapedEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 107;

    private static final int OUTPUT_X = 133;

    private static final int OUTPUT_Y = 80;

    private static final int SLOT_PITCH = 19;

    private static final int GRID_CENTER = 53;

    private final int width;

    private final int height;

    public AutomaticShapedEmiRecipe(AutomaticShapedDisplay display) {
        super(CreateEmiCategories.AUTOMATIC_SHAPED, display.id(), WIDTH, HEIGHT);

        this.width = display.width();
        this.height = display.height();

        for (Ingredient ingredient : display.ingredients()) {
            inputs.add(EmiIngredient.of(ingredient));
        }
        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_CRAFTER.get()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int gridX = GRID_CENTER - width * SLOT_PITCH / 2;
        int gridY = GRID_CENTER - height * SLOT_PITCH / 2;
        int count = inputs.size();

        widgets.addDrawable(0,
                            0,
                            WIDTH,
                            HEIGHT,
                            (draw, mouseX, mouseY, delta) -> AutomaticShapedRender.draw(draw,
                                                                                        0,
                                                                                        0,
                                                                                        gridX,
                                                                                        gridY,
                                                                                        width,
                                                                                        height,
                                                                                        count));

        for (int i = 0; i < count; i++) {
            int x = gridX + (i % width) * SLOT_PITCH;
            int y = gridY + (i / width) * SLOT_PITCH;
            widgets.addSlot(inputs.get(i), x, y);
        }

        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }
}
