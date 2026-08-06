package fr.syuko.emicreatecompat.category.conversion;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;

public class ConversionEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 50;

    private static final int INPUT_X = 26;

    private static final int INPUT_Y = 16;

    private static final int OUTPUT_X = 131;

    private static final int OUTPUT_Y = 16;

    public ConversionEmiRecipe(ConversionDisplay display) {
        super(CreateEmiCategories.MYSTERY_CONVERSION, display.id(), WIDTH, HEIGHT);

        inputs.add(EmiStack.of(display.input()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> ConversionRender.draw(draw, 0, 0));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);
        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }
}
