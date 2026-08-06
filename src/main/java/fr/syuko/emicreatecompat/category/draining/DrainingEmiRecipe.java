package fr.syuko.emicreatecompat.category.draining;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

public class DrainingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 50;

    private static final int INPUT_X = 26;

    private static final int INPUT_Y = 7;

    private static final int OUTPUT_X = 131;

    private static final int FLUID_Y = 7;

    private static final int ITEM_OUTPUT_Y = 26;

    private static final int SLOT_SIZE = 18;

    private final EmiStack fluid;

    private final FluidStack rendered;

    public DrainingEmiRecipe(DrainingDisplay display) {
        super(CreateEmiCategories.DRAINING, display.id(), WIDTH, HEIGHT);

        this.rendered = display.fluid();
        this.fluid = EmiStack.of(rendered.getFluid(), rendered.getComponentsPatch(), rendered.getAmount());

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllBlocks.ITEM_DRAIN.get()));
        outputs.add(fluid);
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> drawBackground(draw));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);
        widgets.addTank(fluid, OUTPUT_X, FLUID_Y, SLOT_SIZE, SLOT_SIZE, (int) fluid.getAmount()).recipeContext(this);
        widgets.addSlot(outputs.get(1), OUTPUT_X, ITEM_OUTPUT_Y).recipeContext(this);
    }

    private void drawBackground(GuiGraphics graphics) {
        DrainingRender.draw(graphics, 0, 0, rendered);
    }
}
