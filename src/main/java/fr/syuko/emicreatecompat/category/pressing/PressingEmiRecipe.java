package fr.syuko.emicreatecompat.category.pressing;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;

public class PressingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 70;

    private static final int INPUT_X = 26;

    private static final int INPUT_Y = 50;

    private static final int OUTPUT_X = 130;

    private static final int OUTPUT_Y = 49;

    private static final int OUTPUT_SPACING = 19;

    public PressingEmiRecipe(PressingDisplay display) {
        super(CreateEmiCategories.PRESSING, display.id(), WIDTH, HEIGHT);

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()));
        for (PressingDisplay.ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()).setChance(output.chance()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> PressingRender.draw(draw, 0, 0));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);

        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), OUTPUT_X + OUTPUT_SPACING * i, OUTPUT_Y).recipeContext(this);
        }
    }
}
