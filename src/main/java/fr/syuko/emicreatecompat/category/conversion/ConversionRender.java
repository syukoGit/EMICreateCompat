package fr.syuko.emicreatecompat.category.conversion;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.minecraft.client.gui.GuiGraphics;

public final class ConversionRender {

    private ConversionRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, x + 52, y + 20);
        AllGuiTextures.JEI_QUESTION_MARK.render(graphics, x + 77, y + 5);
    }
}
