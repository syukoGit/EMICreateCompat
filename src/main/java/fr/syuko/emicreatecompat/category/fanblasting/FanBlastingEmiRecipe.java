package fr.syuko.emicreatecompat.category.fanblasting;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.fan.FanEmiRecipe;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class FanBlastingEmiRecipe extends FanEmiRecipe {

    public FanBlastingEmiRecipe(FanBlastingDisplay display) {
        super(CreateEmiCategories.FAN_BLASTING, display.id());

        inputs.add(EmiIngredient.of(display.input()));
        outputs.add(EmiStack.of(display.output()));
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int arrowX) {
        FanBlastingRender.draw(graphics, 0, 0, arrowX);
    }
}
