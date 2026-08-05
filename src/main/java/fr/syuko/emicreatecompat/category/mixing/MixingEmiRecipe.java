package fr.syuko.emicreatecompat.category.mixing;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinEmiRecipe;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class MixingEmiRecipe extends BasinEmiRecipe {

    private static final int HEIGHT = 103;

    public MixingEmiRecipe(BasinDisplay display) {
        super(CreateEmiCategories.MIXING, display, HEIGHT);

        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_MIXER.get()));
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int outputRows) {
        MixingRender.draw(graphics, 0, 0, heat, true, outputRows);
    }
}
