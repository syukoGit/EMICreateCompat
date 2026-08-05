package fr.syuko.emicreatecompat.category.automaticshapeless;

import com.simibubi.create.AllBlocks;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinEmiRecipe;
import fr.syuko.emicreatecompat.category.mixing.MixingRender;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class AutomaticShapelessEmiRecipe extends BasinEmiRecipe {

    private static final int HEIGHT = 85;

    public AutomaticShapelessEmiRecipe(BasinDisplay display) {
        super(CreateEmiCategories.AUTOMATIC_SHAPELESS, display, HEIGHT);

        catalysts.add(EmiStack.of(AllBlocks.MECHANICAL_MIXER.get()));
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int outputRows) {
        MixingRender.draw(graphics, 0, 0, heat, false, outputRows);
    }
}
