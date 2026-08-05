package fr.syuko.emicreatecompat.category.polishing;

import com.simibubi.create.AllItems;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.world.item.crafting.Ingredient;

public class PolishingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 55;

    private static final int INPUT_X = 26;

    private static final int INPUT_Y = 28;

    private static final int OUTPUT_X = 131;

    private static final int OUTPUT_Y = 28;

    private final Ingredient polished;

    public PolishingEmiRecipe(PolishingDisplay display) {
        super(CreateEmiCategories.SANDPAPER_POLISHING, display.id(), WIDTH, HEIGHT);

        this.polished = display.input();

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllItems.SAND_PAPER.get()));
        catalysts.add(EmiStack.of(AllItems.RED_SAND_PAPER.get()));
        outputs.add(EmiStack.of(display.output()).setChance(display.chance()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0,
                            0,
                            WIDTH,
                            HEIGHT,
                            (draw, mouseX, mouseY, delta) -> PolishingRender.draw(draw, 0, 0, polished));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);
        widgets.addSlot(outputs.getFirst(), OUTPUT_X, OUTPUT_Y).recipeContext(this);
    }
}
