package fr.syuko.emicreatecompat.category.crushing;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.createmod.catnip.layout.LayoutHelper;

public class CrushingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 100;

    private static final int INPUT_X = 50;

    private static final int INPUT_Y = 2;

    private static final int OUTPUT_ORIGIN_X = WIDTH / 2;

    private static final int OUTPUT_ORIGIN_Y = 86;

    private static final int SLOT_SIZE = 18;

    private static final int SLOT_SPACING = 1;

    public CrushingEmiRecipe(CrushingDisplay display) {
        super(CreateEmiCategories.CRUSHING, display.id(), WIDTH, HEIGHT);

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllBlocks.CRUSHING_WHEEL.get()));
        for (ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> CrushingRender.draw(draw, 0, 0));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);

        LayoutHelper layout = LayoutHelper.centeredHorizontal(outputs.size(), 1, SLOT_SIZE, SLOT_SIZE, SLOT_SPACING);
        for (EmiStack output : outputs) {
            widgets.addSlot(output, OUTPUT_ORIGIN_X + layout.getX(), OUTPUT_ORIGIN_Y + layout.getY())
                   .recipeContext(this);
            layout.next();
        }
    }
}
