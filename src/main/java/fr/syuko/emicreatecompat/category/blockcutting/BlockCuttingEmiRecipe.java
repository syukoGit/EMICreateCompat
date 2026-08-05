package fr.syuko.emicreatecompat.category.blockcutting;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockCuttingEmiRecipe extends BasicEmiRecipe {

    private static final int WIDTH = 177;

    private static final int HEIGHT = 70;

    private static final int INPUT_X = 4;

    private static final int INPUT_Y = 4;

    private static final int OUTPUT_X = 77;

    private static final int OUTPUT_Y = 47;

    private static final int OUTPUT_SPACING = 19;

    private static final int COLUMNS = 5;

    private final List<EmiIngredient> outputGroups = new ArrayList<>();

    public BlockCuttingEmiRecipe(BlockCuttingDisplay display) {
        super(CreateEmiCategories.BLOCK_CUTTING, display.id(), WIDTH, HEIGHT);

        inputs.add(EmiIngredient.of(display.input()));
        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_SAW.get()));

        for (List<ItemStack> group : display.outputGroups()) {
            List<EmiStack> stacks = new ArrayList<>();
            for (ItemStack stack : group) {
                EmiStack emiStack = EmiStack.of(stack);
                stacks.add(emiStack);
                outputs.add(emiStack);
            }
            outputGroups.add(EmiIngredient.of(stacks));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, WIDTH, HEIGHT, (draw, mouseX, mouseY, delta) -> BlockCuttingRender.draw(draw, 0, 0));
        widgets.addSlot(inputs.getFirst(), INPUT_X, INPUT_Y);

        for (int i = 0; i < outputGroups.size(); i++) {
            int x = OUTPUT_X + (i % COLUMNS) * OUTPUT_SPACING;
            int y = OUTPUT_Y - (i / COLUMNS) * OUTPUT_SPACING;
            widgets.addSlot(outputGroups.get(i), x, y).recipeContext(this);
        }
    }
}
