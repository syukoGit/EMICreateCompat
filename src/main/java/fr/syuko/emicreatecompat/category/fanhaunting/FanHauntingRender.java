package fr.syuko.emicreatecompat.category.fanhaunting;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.FanRender;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.Blocks;

public final class FanHauntingRender {

    private FanHauntingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, int arrowX) {
        FanRender.draw(graphics,
                       x,
                       y,
                       AllGuiTextures.JEI_LIGHT,
                       arrowX,
                       target -> GuiGameElement.of(Blocks.SOUL_FIRE.defaultBlockState())
                                               .scale(FanRender.SCALE)
                                               .atLocal(0, 0, 2)
                                               .lighting(KineticsRender.LIGHTING)
                                               .render(target));
    }
}
