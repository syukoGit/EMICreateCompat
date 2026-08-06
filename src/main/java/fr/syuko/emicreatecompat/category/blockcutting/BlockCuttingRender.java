package fr.syuko.emicreatecompat.category.blockcutting;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.SawRender;
import net.minecraft.client.gui.GuiGraphics;

public final class BlockCuttingRender {

    private BlockCuttingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 31, y + 6);
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 16, y + 50);
        SawRender.draw(graphics, x + 33, y + 37);
    }
}
