package fr.syuko.emicreatecompat.category.fansmoking;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.fan.FanEmiRecipe;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class FanSmokingEmiRecipe extends FanEmiRecipe {

    public FanSmokingEmiRecipe(FanSmokingDisplay display) {
        super(CreateEmiCategories.FAN_SMOKING, display.id());

        inputs.add(EmiIngredient.of(display.input()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int arrowX) {
        FanSmokingRender.draw(graphics, 0, 0, arrowX);
    }
}
