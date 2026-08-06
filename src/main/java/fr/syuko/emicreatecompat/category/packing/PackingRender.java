package fr.syuko.emicreatecompat.category.packing;

import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import fr.syuko.emicreatecompat.create.render.BasinRender;
import fr.syuko.emicreatecompat.create.render.PressRender;
import net.minecraft.client.gui.GuiGraphics;

public final class PackingRender {

    private static final int MACHINE_X = 91;

    private PackingRender() {
    }

    public static void draw(GuiGraphics graphics,
                            int x,
                            int y,
                            HeatRequirement heat,
                            boolean showHeatBar,
                            int outputRows) {
        BasinRender.drawFrame(graphics, x, y, heat, showHeatBar, outputRows);
        BasinRender.drawBlazeBurner(graphics, x + MACHINE_X, y + 55, heat);
        PressRender.draw(graphics, x + MACHINE_X, y + 34, true);
    }
}
