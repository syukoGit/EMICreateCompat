package fr.syuko.emicreatecompat.category.sawing;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;

public class SawingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 70;

    private static final int INPUT_X = 43;

    private static final int INPUT_Y = 4;

    private static final int OUTPUT_X = 117;

    private static final int OUTPUT_Y = 47;

    private static final int OUTPUT_SPACING = 19;

    public SawingEmiRecipe(SawingDisplay display) {
        super(CreateEmiCategories.SAWING, display.id(), WIDTH, HEIGHT);

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_SAW.get()));
        for (ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()).setChance(output.chance()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> SawingRender.draw(draw, 0, 0));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);

        for (int i = 0; i < outputs.size(); i++) {
            int x = OUTPUT_X + (i % 2) * OUTPUT_SPACING;
            int y = OUTPUT_Y - (i / 2) * OUTPUT_SPACING;
            widgets.addSlot(outputs.get(i), x, y).recipeContext(this);
        }
    }
}
