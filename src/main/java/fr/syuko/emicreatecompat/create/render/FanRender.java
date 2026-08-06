package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public final class FanRender {

    public static final int SCALE = 24;

    private static final float BLADE_DEGREES_PER_TICK = 16f;

    private FanRender() {
    }

    public static void draw(GuiGraphics graphics,
                            int x,
                            int y,
                            AllGuiTextures blockShadow,
                            int arrowX,
                            AttachedBlock attached) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 46, y + 29);
        blockShadow.render(graphics, x + 65, y + 39);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, x + arrowX, y + 51);

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + 56, y + 33, 0);
        pose.mulPose(Axis.XP.rotationDegrees(-12.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(AllPartialModels.ENCASED_FAN_INNER)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(180, 0, KineticsRender.shaftAngle() * BLADE_DEGREES_PER_TICK)
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.ENCASED_FAN.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 180, 0)
                      .atLocal(0, 0, 0)
                      .scale(SCALE)
                      .render(graphics);

        attached.render(graphics);
        pose.popPose();
    }

    @FunctionalInterface
    public interface AttachedBlock {
        void render(GuiGraphics graphics);
    }
}
