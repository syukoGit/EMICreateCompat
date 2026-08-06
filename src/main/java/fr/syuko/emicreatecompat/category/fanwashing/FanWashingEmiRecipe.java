package fr.syuko.emicreatecompat.category.fanwashing;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.fan.FanEmiRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.plugin.CreateEmiCategories;
import net.minecraft.client.gui.GuiGraphics;

public class FanWashingEmiRecipe extends FanEmiRecipe {

    public FanWashingEmiRecipe(FanWashingDisplay display) {
        super(CreateEmiCategories.FAN_WASHING, display.id());

        inputs.add(EmiIngredient.of(display.input()));
        for (ChancedStack output : display.outputs()) {
            outputs.add(EmiStack.of(output.stack()).setChance(output.chance()));
        }
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, int arrowX) {
        FanWashingRender.draw(graphics, 0, 0, arrowX);
    }
}
