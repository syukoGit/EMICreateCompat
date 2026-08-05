package fr.syuko.emicreatecompat.category.deploying;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.DeployerRender;
import net.minecraft.client.gui.GuiGraphics;

public final class DeployingRender {

    private DeployingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, int outputCount) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 62, y + 57);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics,
                                             x + 126,
                                             y + 29 + (outputCount > 2
                                                       ? -19
                                                       : 0));
        DeployerRender.draw(graphics, x + 75, y + 22, 0);
    }
}
