package fr.syuko.emicreatecompat.category.automaticshaped;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;

public class AutomaticShapedEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 107;

    private static final int OUTPUT_X = 133;

    private static final int OUTPUT_Y = 80;

    private final AutomaticShapedDisplay display;

    public AutomaticShapedEmiRecipe(AutomaticShapedDisplay display) {
        super(CreateEmiCategories.AUTOMATIC_SHAPED, display.id(), WIDTH, HEIGHT);

        this.display = display;

        for (Ingredient ingredient : display.ingredients()) {
            if (!ingredient.isEmpty()) {
                inputs.add(EmiIngredient.of(ingredient));
            }
        }
        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_CRAFTER.get()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> drawBackground(draw));

        int slot = 0;
        for (int i = 0; i < display.ingredients().size(); i++) {
            if (display.ingredients().get(i).isEmpty()) {
                continue;
            }
            widgets.addSlot(inputs.get(slot), display.slotX(i), display.slotY(i));
            slot++;
        }

        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }

    private void drawBackground(GuiGraphics graphics) {
        AutomaticShapedRender.draw(graphics, 0, 0, display);
    }
}
