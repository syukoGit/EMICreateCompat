package fr.syuko.emicreatecompat.category.spoutfilling;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.SpoutRender;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

public final class SpoutFillingRender {

    private SpoutFillingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, FluidStack fluid) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 62, y + 57);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 126, y + 29);
        SpoutRender.draw(graphics, x + 75, y + 22, fluid, 0);
    }
}
