package fr.syuko.emicreatecompat.category.mixing;

import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import fr.syuko.emicreatecompat.create.render.BasinRender;
import fr.syuko.emicreatecompat.create.render.MixerRender;
import net.minecraft.client.gui.GuiGraphics;

public final class MixingRender {

    private static final int MACHINE_X = 91;

    private MixingRender() {
    }

    public static void draw(GuiGraphics graphics,
                            int x,
                            int y,
                            HeatRequirement heat,
                            boolean showHeatBar,
                            int outputRows) {
        BasinRender.drawFrame(graphics, x, y, heat, showHeatBar, outputRows);
        BasinRender.drawBlazeBurner(graphics, x + MACHINE_X, y + 55, heat);
        MixerRender.draw(graphics, x + MACHINE_X, y + 34);
    }
}
