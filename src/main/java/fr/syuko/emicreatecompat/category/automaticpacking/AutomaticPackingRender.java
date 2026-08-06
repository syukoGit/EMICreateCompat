package fr.syuko.emicreatecompat.category.automaticpacking;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.PressRender;
import net.minecraft.client.gui.GuiGraphics;

public final class AutomaticPackingRender {

    private static final int MACHINE_X = 91;

    private AutomaticPackingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 136, y + 32);
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 81, y + 68);
        PressRender.draw(graphics, x + MACHINE_X, y + 34, true);
    }
}
