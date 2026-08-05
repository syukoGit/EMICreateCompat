package fr.syuko.emicreatecompat.category.fanblasting;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.FanRender;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.material.Fluids;

public final class FanBlastingRender {

    private FanBlastingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, int arrowX) {
        FanRender.draw(graphics,
                       x,
                       y,
                       AllGuiTextures.JEI_LIGHT,
                       arrowX,
                       target -> GuiGameElement.of(Fluids.LAVA)
                                               .scale(FanRender.SCALE)
                                               .atLocal(0, 0, 2)
                                               .lighting(KineticsRender.LIGHTING)
                                               .render(target));
    }
}
