package fr.syuko.emicreatecompat.category.mechanicalcrafting;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.emi.ScaledSlotWidget;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;

public class MechanicalCraftingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 107;

    private static final int OUTPUT_X = 133;

    private static final int OUTPUT_Y = 80;

    private static final int SLOT_PITCH = 19;

    private final MechanicalCraftingDisplay display;

    public MechanicalCraftingEmiRecipe(MechanicalCraftingDisplay display) {
        super(CreateEmiCategories.MECHANICAL_CRAFTING, display.id(), WIDTH, HEIGHT);

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

        float scale = display.scale();
        int columns = display.width();
        int slot = 0;

        for (int i = 0; i < display.ingredients().size(); i++) {
            Ingredient ingredient = display.ingredients().get(i);
            if (ingredient.isEmpty()) {
                continue;
            }

            int x = display.gridX() + Math.round((i % columns) * SLOT_PITCH * scale);
            int y = display.gridY() + Math.round(((float) i / columns) * SLOT_PITCH * scale);
            widgets.add(new ScaledSlotWidget(inputs.get(slot), x, y, scale));
            slot++;
        }

        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }

    private void drawBackground(GuiGraphics graphics) {
        MechanicalCraftingRender.draw(graphics, 0, 0, display);
    }
}
