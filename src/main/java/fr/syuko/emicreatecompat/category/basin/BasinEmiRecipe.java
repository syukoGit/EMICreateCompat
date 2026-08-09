package fr.syuko.emicreatecompat.category.basin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.CountedIngredient;
import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BasinEmiRecipe extends BasicEmiRecipe {

    protected static final int WIDTH = 177;

    private static final int INPUT_X = 16;

    private static final int INPUT_Y = 50;

    private static final int OUTPUT_X = 141;

    private static final int OUTPUT_Y = 50;

    private static final int SLOT_PITCH = 19;

    private static final int SLOT_SIZE = 18;

    private static final int INPUT_COLUMNS = 3;

    private static final int BURNER_X = 133;

    private static final int CAKE_X = 152;

    private static final int HEAT_ITEM_Y = 80;

    protected final HeatRequirement heat;

    private final List<EmiIngredient> fluidInputs = new ArrayList<>();

    private final List<EmiStack> fluidOutputs = new ArrayList<>();

    protected BasinEmiRecipe(EmiRecipeCategory category, BasinDisplay display, int height) {
        super(category, display.id(), WIDTH, height);

        this.heat = display.heat();

        for (CountedIngredient input : display.itemInputs()) {
            inputs.add(EmiIngredient.of(input.ingredient(), input.count()));
        }

        for (List<FluidStack> options : display.fluidInputs()) {
            List<EmiStack> stacks = new ArrayList<>();
            for (FluidStack option : options) {
                stacks.add(EmiStack.of(option.getFluid(), option.getComponentsPatch(), option.getAmount()));
            }
            EmiIngredient ingredient = EmiIngredient.of(stacks);
            fluidInputs.add(ingredient);
            inputs.add(ingredient);
        }

        for (ChancedStack output : display.itemOutputs()) {
            outputs.add(EmiStack.of(output.stack()));
        }

        for (FluidStack output : display.fluidOutputs()) {
            EmiStack stack = EmiStack.of(output.getFluid(), output.getComponentsPatch(), output.getAmount());
            fluidOutputs.add(stack);
            outputs.add(stack);
        }

        catalysts.add(EmiStack.of(AllBlocks.BASIN.get()));
        if (heat.needsBlazeBurner()) {
            catalysts.add(EmiStack.of(AllBlocks.BLAZE_BURNER.get()));
        }
        if (heat.needsBlazeCake()) {
            catalysts.add(EmiStack.of(AllItems.BLAZE_CAKE.get()));
        }
    }

    protected abstract void drawBackground(GuiGraphics graphics, int outputRows);

    protected int outputRows() {
        return (1 + outputs.size()) / 2;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int rows = outputRows();
        widgets.addDrawable(0,
                            0,
                            WIDTH,
                            getDisplayHeight(),
                            (draw, mouseX, mouseY, delta) -> drawBackground(draw, rows));

        addInputSlots(widgets);
        addOutputSlots(widgets);
        addHeatSlots(widgets);
    }

    protected void addInputSlots(WidgetHolder widgets) {
        int count = inputs.size();
        int
                offset =
                count < INPUT_COLUMNS
                ? (INPUT_COLUMNS - count) * SLOT_PITCH / 2
                : 0;

        for (int i = 0; i < count; i++) {
            int x = INPUT_X + offset + (i % INPUT_COLUMNS) * SLOT_PITCH;
            int y = INPUT_Y - (i / INPUT_COLUMNS) * SLOT_PITCH;
            EmiIngredient ingredient = inputs.get(i);

            if (fluidInputs.contains(ingredient)) {
                widgets.addTank(ingredient, x, y, SLOT_SIZE, SLOT_SIZE, (int) ingredient.getAmount());
            } else {
                widgets.addSlot(ingredient, x, y);
            }
        }
    }

    protected void addOutputSlots(WidgetHolder widgets) {
        int count = outputs.size();

        for (int i = 0; i < count; i++) {
            int
                    x =
                    OUTPUT_X - (count % 2 != 0 && i == count - 1
                                ? 0
                                : i % 2 == 0
                                  ? 10
                                  : -9);
            int y = OUTPUT_Y - SLOT_PITCH * (i / 2);
            EmiStack output = outputs.get(i);

            if (fluidOutputs.contains(output)) {
                widgets.addTank(output, x, y, SLOT_SIZE, SLOT_SIZE, (int) output.getAmount()).recipeContext(this);
            } else {
                widgets.addSlot(output, x, y).recipeContext(this);
            }
        }
    }

    protected void addHeatSlots(WidgetHolder widgets) {
        if (heat.needsBlazeBurner()) {
            widgets.addSlot(EmiStack.of(AllBlocks.BLAZE_BURNER.get()), BURNER_X, HEAT_ITEM_Y).drawBack(false);
        }
        if (heat.needsBlazeCake()) {
            widgets.addSlot(EmiStack.of(AllItems.BLAZE_CAKE.get()), CAKE_X, HEAT_ITEM_Y).catalyst(true);
        }
    }
}
