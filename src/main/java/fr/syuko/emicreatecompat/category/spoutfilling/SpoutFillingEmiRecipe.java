package fr.syuko.emicreatecompat.category.spoutfilling;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SpoutFillingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 70;

    private static final int INPUT_X = 26;

    private static final int INPUT_Y = 50;

    private static final int FLUID_Y = 31;

    private static final int OUTPUT_X = 131;

    private static final int SLOT_SIZE = 18;

    private final EmiIngredient fluid;

    private final FluidStack rendered;

    public SpoutFillingEmiRecipe(SpoutFillingDisplay display) {
        super(CreateEmiCategories.SPOUT_FILLING, display.id(), WIDTH, HEIGHT);

        List<EmiStack> options = new ArrayList<>();
        for (FluidStack option : display.fluids()) {
            options.add(EmiStack.of(option.getFluid(), option.getComponentsPatch(), option.getAmount()));
        }
        this.fluid = EmiIngredient.of(options);
        this.rendered = display.fluids().getFirst();

        inputs.add(EmiIngredient.of(display.input()));
        inputs.add(fluid);
        catalysts.add(EmiStack.of(AllBlocks.SPOUT.get()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> drawBackground(draw));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);
        widgets.addTank(fluid, INPUT_X, FLUID_Y, SLOT_SIZE, SLOT_SIZE, (int) fluid.getAmount());
        widgets.addSlot(outputs.getFirst(), OUTPUT_X, INPUT_Y).recipeContext(this);
    }

    private void drawBackground(GuiGraphics graphics) {
        SpoutFillingRender.draw(graphics, 0, 0, rendered);
    }
}
