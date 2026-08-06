package fr.syuko.emicreatecompat.category.packing;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinEmiRecipe;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class PackingEmiRecipe extends BasinEmiRecipe {

    private static final int HEIGHT = 103;

    public PackingEmiRecipe(BasinDisplay display) {
        super(CreateEmiCategories.PACKING, display, HEIGHT);

        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()));
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int outputRows) {
        PackingRender.draw(graphics, 0, 0, heat, true, outputRows);
    }
}
