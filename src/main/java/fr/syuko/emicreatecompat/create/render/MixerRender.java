package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class MixerRender {

    private static final int SCALE = 23;

    private MixerRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        float poleOffset = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 1) / 5) + .5f;

        GuiGameElement.of(AllPartialModels.SHAFTLESS_COGWHEEL)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, KineticsRender.shaftAngle() * 2, 0)
                      .atLocal(0, 0, 0)
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_MIXER.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, 0, 0)
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.MECHANICAL_MIXER_POLE)
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, poleOffset, 0)
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.MECHANICAL_MIXER_HEAD)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, KineticsRender.shaftAngle() * 4, 0)
                      .atLocal(0, poleOffset, 0)
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.BASIN.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, 1.65, 0)
                      .scale(SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
