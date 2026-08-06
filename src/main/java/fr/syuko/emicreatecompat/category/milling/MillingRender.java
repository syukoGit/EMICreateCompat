package fr.syuko.emicreatecompat.category.milling;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public final class MillingRender {

    private static final int BLOCK_SCALE = 22;

    private MillingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_ARROW.render(graphics, x + 85, y + 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 43, y + 4);
        drawMillstone(graphics, x + 48, y + 27);
    }

    private static void drawMillstone(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);
        pose.translate(-2, 18, 0);

        GuiGameElement.of(AllPartialModels.MILLSTONE_COG)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(22.5, KineticsRender.shaftAngle() * 2, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MILLSTONE.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(22.5, 22.5, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
