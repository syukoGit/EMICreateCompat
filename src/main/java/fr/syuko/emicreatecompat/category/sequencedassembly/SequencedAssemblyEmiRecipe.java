package fr.syuko.emicreatecompat.category.sequencedassembly;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class SequencedAssemblyEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 180;

    private static final int HEIGHT = 115;

    private static final int INPUT_X = 26;

    private static final int OUTPUT_X = 131;

    private static final int SLOT_Y = 90;

    private static final int STEP_SLOT_Y = 14;

    private static final int STEP_SLOT_INSET = 3;

    private static final int SLOT_SIZE = 18;

    private static final String NOT_CONSUMED_KEY = "create.recipe.deploying.not_consumed";

    private final List<AssemblyStep> steps;

    private final float outputChance;

    private final int loops;

    public SequencedAssemblyEmiRecipe(SequencedAssemblyDisplay display) {
        super(CreateEmiCategories.SEQUENCED_ASSEMBLY, display.id(), WIDTH, HEIGHT);

        this.steps = display.steps();
        this.outputChance = display.outputChance();
        this.loops = display.loops();

        inputs.add(EmiIngredient.of(display.input()));
        for (AssemblyStep step : steps) {
            if (step.heldItem() != null) {
                inputs.add(EmiIngredient.of(step.heldItem(), loops));
            }
            for (FluidStack fluid : step.fluids()) {
                inputs.add(EmiStack.of(fluid.getFluid(), fluid.getComponentsPatch(), (long) fluid.getAmount() * loops));
            }
        }

        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int
                shift =
                outputChance == 1
                ? 0
                : -7;

        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> drawBackground(draw));

        widgets.addSlot(inputs.getFirst(), INPUT_X + shift, SLOT_Y);
        widgets.addSlot(outputs.getFirst(), OUTPUT_X + shift, SLOT_Y).recipeContext(this);

        addStepSlots(widgets);
    }

    private void addStepSlots(WidgetHolder widgets) {
        int x = SequencedAssemblyRender.stepsWidth(steps) / -2 + WIDTH / 2;
        int ingredient = 1;

        for (AssemblyStep step : steps) {
            if (step.heldItem() != null) {
                SlotWidget slot = widgets.addSlot(inputs.get(ingredient), x + STEP_SLOT_INSET, STEP_SLOT_Y);
                if (step.keepHeldItem()) {
                    slot.appendTooltip(Component.translatable(NOT_CONSUMED_KEY).withStyle(ChatFormatting.GOLD));
                }
                ingredient++;
            }

            for (int i = 0; i < step.fluids().size(); i++) {
                EmiIngredient fluid = inputs.get(ingredient);
                widgets.addTank(fluid, x + STEP_SLOT_INSET, STEP_SLOT_Y, SLOT_SIZE, SLOT_SIZE, (int) fluid.getAmount());
                ingredient++;
            }

            x += AssemblyStep.WIDTH + AssemblyStep.MARGIN;
        }
    }

    private void drawBackground(GuiGraphics graphics) {
        SequencedAssemblyRender.draw(graphics, 0, 0, WIDTH, steps, outputChance, loops);
    }
}
