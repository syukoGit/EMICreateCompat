package fr.syuko.emicreatecompat.category.draining;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.DrainRender;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

public final class DrainingRender {

    private DrainingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, FluidStack fluid) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 62, y + 37);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 73, y + 4);
        DrainRender.draw(graphics, x + 75, y + 40, fluid);
    }
}
