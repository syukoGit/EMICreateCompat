package fr.syuko.emicreatecompat.category.sawing;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.SawRender;
import net.minecraft.client.gui.GuiGraphics;

public final class SawingRender {

    private SawingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 70, y + 6);
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 55, y + 55);
        SawRender.draw(graphics, x + 72, y + 42);
    }
}
